package com.businessapi.util;

import com.businessapi.dto.request.BuyOrderSaveRequestDTO;
import com.businessapi.entities.Product;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.services.OrderService;
import com.businessapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoOrderScheduler
{
    private final ProductService productService;
    private final OrderService orderService;

    /**
     * Every minute database will be checked and if product is below minimum stock level it will be auto ordered
     */
    //TODO CHANGE 1 MIN SCHEDULER TO 1 HOUR LATER
    @Scheduled(cron = "0 * * * * ?")
    public void AutoOrderByStockLevel() {

        List<Product> productList = productService.findAllByMinimumStockLevelAndStatus(EStatus.ACTIVE);
        System.out.println("Scheduler worked");
        productList.forEach(product ->
        {
            if (!product.getIsProductAutoOrdered() && product.getIsAutoOrderEnabled())
            {
                //TODO AUTO ORDER COUNT SET TO MINSTOCKLEVEL*2 MAYBE IT CAN BE CHANGED LATER
                orderService.saveBuyOrder(new BuyOrderSaveRequestDTO(product.getSupplierId(), product.getId(), product.getMinimumStockLevel() * 2));
                product.setIsProductAutoOrdered(true);
                productService.save(product);
            }
        });


    }
}
