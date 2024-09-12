package com.businessapi.services;

import com.businessapi.dto.request.BuyOrderSaveRequestDTO;
import com.businessapi.dto.request.SellOrderSaveRequestDTO;
import com.businessapi.dto.request.OrderUpdateRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.entities.Order;
import com.businessapi.entities.Product;
import com.businessapi.entities.enums.EOrderType;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import com.businessapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService
{
    private final OrderRepository orderRepository;
    private final ProductService productService;

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

            return orderRepository.findAll( PageRequest.of(dto.page(), dto.size())).getContent();

    }

    public Order findById(Long id)
    {
        return orderRepository.findById(id).orElseThrow(() -> new StockServiceException(ErrorType.ORDER_NOT_FOUND));
    }



}
