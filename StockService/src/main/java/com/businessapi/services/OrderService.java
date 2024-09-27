package com.businessapi.services;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.BuyOrderResponseDTO;
import com.businessapi.dto.response.SellOrderResponseDTO;
import com.businessapi.dto.response.SupplierOrderResponseDTO;
import com.businessapi.entities.Customer;
import com.businessapi.entities.Order;
import com.businessapi.entities.Product;
import com.businessapi.entities.Supplier;
import com.businessapi.entities.enums.EOrderType;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.OrderRepository;
import com.businessapi.util.SessionManager;
import lombok.RequiredArgsConstructor;
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
    private final CustomerService customerService;
    private SupplierService supplierService;

    @Autowired
    private void setService(@Lazy SupplierService supplierService)
    {
        this.supplierService = supplierService;
    }

    public Boolean saveSellOrder(SellOrderSaveRequestDTO dto)
    {

        Product product = productService.findById(dto.productId());
        if (product.getStockCount() <= dto.quantity())
        {
            SessionManager.additionalErrorMessage = product.getName() +" Stock count is: " + product.getStockCount();
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
                .memberId(SessionManager.memberId)
                .customerId(dto.customerId())
                .unitPrice(product.getPrice())
                .quantity(dto.quantity())
                .productId(dto.productId())
                .orderType(EOrderType.SELL)
                .build();

        orderRepository.save(order);
        return true;
    }

    public Boolean saveSellOrderForDemoData(SellOrderSaveRequestDTO dto)
    {

        Product product = productService.findByIdForDemoData(dto.productId());
        if (product.getStockCount() <= dto.quantity())
        {
            SessionManager.additionalErrorMessage = product.getName() +" Stock count is: " + product.getStockCount();
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
                .memberId(2L)
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
                .memberId(SessionManager.memberId)
                .supplierId(dto.supplierId())
                .unitPrice(product.getPrice())
                .quantity(dto.quantity())
                .productId(dto.productId())
                .orderType(EOrderType.BUY)
                .build();

        orderRepository.save(order);
        return true;
    }

    public Boolean saveBuyOrderForAutoScheduler(BuyOrderSaveRequestDTO dto, Long memberId)
    {
        Product product = productService.findById(dto.productId());
        if (product.getStatus() != EStatus.ACTIVE)
        {
            throw new StockServiceException(ErrorType.PRODUCT_NOT_ACTIVE);
        }

        Order order = Order
                .builder()
                .memberId(memberId)
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

        // Authorization check whether the member is authorized to do that or not
        SessionManager.authorizationCheck(order.getMemberId());

        order.setStatus(EStatus.DELETED);
        orderRepository.save(order);
        return true;
    }


    public Boolean updateBuyOrder(BuyOrderUpdateRequestDTO dto)
    {
        Order order = orderRepository.findById(dto.id()).orElseThrow(() -> new StockServiceException(ErrorType.ORDER_NOT_FOUND));

        // Authorization check whether the member is authorized to do that or not
        SessionManager.authorizationCheck(order.getMemberId());
        order.setQuantity(dto.quantity());
        order.setProductId(dto.productId());
        order.setSupplierId(dto.supplierId());
        orderRepository.save(order);
        return true;
    }

    public Boolean updateSellOrder(SellOrderUpdateRequestDTO dto) {

        Order order = orderRepository.findById(dto.id())
                .orElseThrow(() -> new StockServiceException(ErrorType.ORDER_NOT_FOUND));


        SessionManager.authorizationCheck(order.getMemberId());


        Product product = productService.findById(dto.productId());


        Integer stockDifference = dto.quantity() - order.getQuantity();


        if (stockDifference < 0) {
            product.setStockCount(product.getStockCount() + Math.abs(stockDifference));
        } else if (stockDifference > 0) {

            if (product.getStockCount() < stockDifference) {
                SessionManager.additionalErrorMessage = product.getName() +" Stock count is: " + product.getStockCount();
                throw new StockServiceException(ErrorType.INSUFFICIENT_STOCK);
            }
            product.setStockCount(product.getStockCount() - stockDifference);
        }

        order.setQuantity(dto.quantity());
        order.setProductId(dto.productId());
        order.setCustomerId(dto.customerId());


        orderRepository.save(order);
        productService.save(product);

        return true;
    }

    public List<Order> findAll(PageRequestDTO dto)
    {

        return orderRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();

    }

    public Order findById(Long id)
    {
        Order order = orderRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.ORDER_NOT_FOUND));

        // Authorization check whether the member is authorized to do that or not
        SessionManager.authorizationCheck(order.getMemberId());
        return order;
    }


    /**
     * Finds products with name containing search text
     * Finds buy orders with respect to pagination
     * Converts orders to BuyOrderResponseDTO
     *
     * @param dto search text , page number , page size parameters
     * @return List of BuyOrderResponseDTO
     */
    public List<BuyOrderResponseDTO> findAllBuyOrders(PageRequestDTO dto)
    {
        //Finds products with name containing search text
        List<Product> products = productService.findAllByNameContainingIgnoreCaseAndMemberIdOrderByNameAsc(dto.searchText(), SessionManager.memberId);
        //Mapping products to their ids
        List<Long> productIdList = products.stream().map(Product::getId).collect(Collectors.toList());
        //Finds buy orders with respect to pagination, order type and product ids
        List<Order> orderList = orderRepository.findAllByProductIdInAndMemberIdAndStatusIsNotAndOrderType(productIdList, SessionManager.memberId, EStatus.DELETED, EOrderType.BUY, PageRequest.of(dto.page(), dto.size()));
        List<BuyOrderResponseDTO> buyOrderResponseDTOList = new ArrayList<>();
        //Converting orders to BuyOrderResponseDTO and finding productName + supplierName
        orderList.stream().forEach(order ->
        {
            String productName = products.stream().filter(product -> product.getId() == order.getProductId()).findFirst().get().getName();
            Supplier supplier = supplierService.findById(order.getSupplierId());
            buyOrderResponseDTOList.add(new BuyOrderResponseDTO(order.getId(), supplier.getName(), supplier.getEmail(), productName, order.getUnitPrice(), order.getQuantity(), order.getTotal(), order.getOrderType(), order.getCreatedAt(), order.getStatus()));
        });
        return buyOrderResponseDTOList.stream()
                .sorted(Comparator.comparing(BuyOrderResponseDTO::productName))
                .collect(Collectors.toList());
    }

    /**
     * Finds products with name containing search text
     * Finds sell orders with respect to pagination
     * Converts orders to SellOrderResponseDTO
     *
     * @param dto search text , page number , page size parameters
     * @return List of SellOrderResponseDTO
     */
    public List<SellOrderResponseDTO> findAllSellOrders(PageRequestDTO dto)
    {
        //Finds products with name containing search text
        List<Product> products = productService.findAllByNameContainingIgnoreCaseAndMemberIdOrderByNameAsc(dto.searchText(), SessionManager.memberId);
        //Mapping products to their ids
        List<Long> productIdList = products.stream().map(Product::getId).collect(Collectors.toList());
        //Finds buy orders with respect to pagination, order type and product ids
        List<Order> orderList = orderRepository.findAllByProductIdInAndMemberIdAndStatusIsNotAndOrderType(productIdList, SessionManager.memberId, EStatus.DELETED, EOrderType.SELL, PageRequest.of(dto.page(), dto.size()));
        List<SellOrderResponseDTO> sellOrderResponseDTOList = new ArrayList<>();
        //Converting orders to BuyOrderResponseDTO and finding productName + supplierName
        orderList.stream().forEach(order ->
        {
            String productName = products.stream().filter(product -> product.getId() == order.getProductId()).findFirst().get().getName();
            Customer customer = customerService.findById(order.getCustomerId());

            sellOrderResponseDTOList.add(new SellOrderResponseDTO(order.getId(), customer.getName() + " " + customer.getSurname(), customer.getEmail(), productName, order.getUnitPrice(), order.getTotal(), order.getQuantity(), order.getOrderType(), order.getCreatedAt(), order.getStatus()));
        });
        return sellOrderResponseDTOList.stream()
                .sorted(Comparator.comparing(SellOrderResponseDTO::productName))
                .collect(Collectors.toList());

    }

    public List<SupplierOrderResponseDTO> findOrdersOfSupplier(PageRequestDTO dto)
    {
        //TODO LOOK AT THIS LATER THERE MIGHT BE PROBLEM
        Supplier supplier = supplierService.findByAuthId(SessionManager.memberId);

        return orderRepository.findAllByProductNameContainingIgnoreCaseAndsupplierIdAndStatusNot(dto.searchText(), supplier.getId(), EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));

    }
}
