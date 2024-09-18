package com.businessapi.services;

import com.businessapi.RabbitMQ.Model.CustomerNameLastNameResponseModel;
import com.businessapi.dto.request.BuyOrderSaveRequestDTO;
import com.businessapi.dto.request.SellOrderSaveRequestDTO;
import com.businessapi.dto.request.OrderUpdateRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.response.BuyOrderResponseDTO;
import com.businessapi.dto.response.SellOrderResponseDTO;
import com.businessapi.entities.Order;
import com.businessapi.entities.Product;
import com.businessapi.entities.enums.EOrderType;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService
{
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;
    private  SupplierService supplierService;

    @Autowired
    private void setService (@Lazy SupplierService supplierService){
        this.supplierService = supplierService;
    }

    public Boolean saveSellOrder(SellOrderSaveRequestDTO dto)
    {
        Product product = productService.findById(dto.productId());
        if (product.getStockCount() <= dto.quantity())
        {
            throw new StockServiceException(ErrorType.INSUFFICIENT_STOCK);
        }
        if (product.getStatus() != EStatus.ACTIVE)
        {
            throw new StockServiceException(ErrorType.PRODUCT_NOT_ACTIVE);
        }
        //TODO IT CAN BE MOVED TO SOMEWHERE LIKE APPROVEOFFER.
        product.setStockCount(product.getStockCount() - dto.quantity());

        Order order = Order
                .builder()
                .customerId(dto.customerId())
                .unitPrice(product.getPrice())
                .quantity(dto.quantity())
                .productId(dto.productId())
                .orderType(EOrderType.SELL)
                .build();

        orderRepository.save(order);
        return true;
    }

    public Boolean saveBuyOrder(BuyOrderSaveRequestDTO dto)
    {
        Product product = productService.findById(dto.productId());
        if (product.getStatus() != EStatus.ACTIVE)
        {
            throw new StockServiceException(ErrorType.PRODUCT_NOT_ACTIVE);
        }

        Order order = Order
                .builder()
                .supplierId(dto.supplierId())
                .unitPrice(product.getPrice())
                .quantity(dto.quantity())
                .productId(dto.productId())
                .orderType(EOrderType.BUY)
                .build();

        orderRepository.save(order);
        return true;
    }

    public void save(Order order)
    {
        orderRepository.save(order);
    }

    public Boolean delete(Long id)
    {
        Order order = orderRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.ORDER_NOT_FOUND));
        order.setStatus(EStatus.DELETED);
        orderRepository.save(order);
        return true;
    }

    public Boolean update(OrderUpdateRequestDTO dto)
    {
        Order order = orderRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.ORDER_NOT_FOUND));
        order.setQuantity(dto.quantity());
        orderRepository.save(order);
        return true;
    }

    public List<Order> findAll(PageRequestDTO dto)
    {

        return orderRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();

    }

    public Order findById(Long id)
    {
        return orderRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.ORDER_NOT_FOUND));
    }


    /**
     * Finds products with name containing search text
     * Finds buy orders with respect to pagination
     * Converts orders to BuyOrderResponseDTO
     * @param dto search text , page number , page size parameters
     * @return List of BuyOrderResponseDTO
     */
    public List<BuyOrderResponseDTO> findAllBuyOrders(PageRequestDTO dto)
    {
        //Finds products with name containing search text
        List<Product> products = productService.findAllByProductNameContainingIgnoreCase(dto.searchText());
        //Mapping products to their ids
        List<Long> productIdList = products.stream().map(Product::getId).collect(Collectors.toList());
        //Finds buy orders with respect to pagination, order type and product ids
        List<Order> orderList = orderRepository.findAllByProductIdInAndOrderType(productIdList,EOrderType.BUY, PageRequest.of(dto.page(), dto.size()));
        List<BuyOrderResponseDTO> buyOrderResponseDTOList = new ArrayList<>();
        //Converting orders to BuyOrderResponseDTO and finding productName + supplierName
        orderList.stream().forEach(order ->
        {
            String productName = products.stream().filter(product -> product.getId() == order.getProductId()).findFirst().get().getName();
            String supplierName = supplierService.findById(order.getSupplierId()).getName();
            buyOrderResponseDTOList.add(new BuyOrderResponseDTO(order.getId(), supplierName , productName ,order.getUnitPrice(),order.getQuantity(), order.getTotal(), order.getOrderType(),order.getCreatedAt(),order.getStatus()));
        });
        return buyOrderResponseDTOList.stream()
                .sorted(Comparator.comparing(BuyOrderResponseDTO::productName))
                .collect(Collectors.toList());
    }

    /**
     * Finds products with name containing search text
     * Finds sell orders with respect to pagination
     * Converts orders to SellOrderResponseDTO
     * @param dto search text , page number , page size parameters
     * @return List of SellOrderResponseDTO
     */
    public List<SellOrderResponseDTO> findAllSellOrders(PageRequestDTO dto)
    {
        //Finds products with name containing search text
        List<Product> products = productService.findAllByProductNameContainingIgnoreCase(dto.searchText());
        //Mapping products to their ids
        List<Long> productIdList = products.stream().map(Product::getId).collect(Collectors.toList());
        //Finds buy orders with respect to pagination, order type and product ids
        List<Order> orderList = orderRepository.findAllByProductIdInAndOrderType(productIdList,EOrderType.SELL, PageRequest.of(dto.page(), dto.size()));
        List<SellOrderResponseDTO> sellOrderResponseDTOList = new ArrayList<>();
        //Converting orders to BuyOrderResponseDTO and finding productName + supplierName
        orderList.stream().forEach(order ->
        {
            String productName = products.stream().filter(product -> product.getId() == order.getProductId()).findFirst().get().getName();
            CustomerNameLastNameResponseModel customer = (CustomerNameLastNameResponseModel)rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyFindNameAndLastNameById", order.getCustomerId());

            sellOrderResponseDTOList.add(new SellOrderResponseDTO(order.getId(), customer.getFirstName() + " " + customer.getLastName() , productName ,order.getUnitPrice(), order.getTotal(),order.getQuantity(), order.getOrderType(),order.getCreatedAt(),order.getStatus()));
        });
        return sellOrderResponseDTOList.stream()
                .sorted(Comparator.comparing(SellOrderResponseDTO::productName))
                .collect(Collectors.toList());

    }
}
