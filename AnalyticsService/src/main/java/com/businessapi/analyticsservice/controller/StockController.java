package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.entity.stockService.entity.Order;
import com.businessapi.analyticsservice.entity.stockService.entity.StockMovement;
import com.businessapi.analyticsservice.entity.stockService.entity.Supplier;
import com.businessapi.analyticsservice.service.StockService;
import com.businessapi.analyticsservice.entity.stockService.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    /*
     * Order
     */
    @GetMapping("/get-total-sales")
    public ResponseEntity<BigDecimal> getTotalSales() throws Exception {
        String jsonOrders = stockService.getDataFromDataSource("order");
        List<Order> orders = stockService.parseOrders(jsonOrders);
        BigDecimal totalSales = stockService.analyzeTotalSalesOverTime(orders);

        return ResponseEntity.ok(totalSales);
    }

    /*
     * Product
     */
    @GetMapping("/sales-per-day")
    public ResponseEntity<Map<LocalDate, BigDecimal>> calculateTotalSalesPerDay() throws Exception {
        String jsonOrders = stockService.getDataFromDataSource("order");
        List<Order> orders = stockService.parseOrders(jsonOrders);
        Map<LocalDate, BigDecimal> totalSalesByDay = stockService.calculateTotalSalesPerDay(orders);

        return ResponseEntity.ok(totalSalesByDay);
    }

    @PostMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockAlerts() throws Exception {
        String jsonProducts = stockService.getDataFromDataSource("product");
        List<Product> products = stockService.parseProduct(jsonProducts);
        List<Product> lowStockProducts = stockService.getLowStockAlerts(products);

        return ResponseEntity.ok(lowStockProducts);
    }

    /*
     * Stock Movement
     */
    @PostMapping("/stock-per-warehouse")
    public ResponseEntity<Map<Long, Integer>> getStockPerWarehouse() throws Exception {
        String jsonStockMovements = stockService.getDataFromDataSource("stock-movement");
        List<StockMovement> stockMovements = stockService.parseStockMovement(jsonStockMovements);
        Map<Long, Integer> stockPerWarehouse = stockService.analyzeStockPerWarehouse(stockMovements);
        return ResponseEntity.ok(stockPerWarehouse);
    }

    /*
     * Supplier
     */
    @PostMapping("/num-of-suppliers-per-country")
    public ResponseEntity<Map<String, Long>> getNumOfSuppliersPerCountry() throws Exception {
        String jsonSupplier = stockService.getDataFromDataSource("supplier");
        List<Supplier> supplier = stockService.parseSupplier(jsonSupplier);
        Map<String, Long> stockPerWarehouse = stockService.analyzeNumOfSuppliersPerCountry(supplier);
        return ResponseEntity.ok(stockPerWarehouse);
    }

}
