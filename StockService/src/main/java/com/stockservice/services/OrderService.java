package com.stockservice.services;

import com.stockservice.dto.request.OrderSaveRequestDTO;
import com.stockservice.dto.request.OrderUpdateRequestDTO;
import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.entities.Order;
import com.stockservice.entities.Product;
import com.stockservice.entities.WareHouse;
import com.stockservice.entities.enums.EStatus;
import com.stockservice.exception.ErrorType;
import com.stockservice.exception.StockServiceException;
import com.stockservice.repositories.OrderRepository;
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

    public Boolean save(OrderSaveRequestDTO dto)
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

        product.setStockCount(product.getStockCount() - dto.quantity());

        Order order = Order
                .builder()
                .customerId(dto.customerId())
                .unitPrice(product.getPrice())
                .quantity(dto.quantity())
                .productId(dto.productId())
                .build();

        orderRepository.save(order);
        return true;
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
