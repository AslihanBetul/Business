package com.businessapi.analyticsservice.service;

import com.businessapi.analyticsservice.entity.DataSource;
import com.businessapi.analyticsservice.entity.stockService.entity.Order;
import com.businessapi.analyticsservice.entity.stockService.entity.Product;
import com.businessapi.analyticsservice.entity.stockService.entity.StockMovement;
import com.businessapi.analyticsservice.entity.stockService.entity.Supplier;
import com.businessapi.analyticsservice.entity.stockService.enums.EStockMovementType;
import com.businessapi.analyticsservice.repository.DataSourceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final DataSourceRepository dataSourceRepository;

    public StockService(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    // fetch data from the DataSource by serviceType
    public String getDataFromDataSource(String serviceType) {
        DataSource dataSource = dataSourceRepository.findByServiceType(serviceType)
                .orElseThrow(() -> new RuntimeException("DataSource not found for service type: " + serviceType));
        return dataSource.getData();
    }

    /*
     * Order
     */
    // parse orders from JSON string
    public List<Order> parseOrders(String jsonOrders) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode dataNode = objectMapper.readTree(jsonOrders).get("data");
        return Arrays.asList(objectMapper.treeToValue(dataNode, Order[].class));
    }

    // total sales over time
    public BigDecimal analyzeTotalSalesOverTime(List<Order> orders) {
        return orders.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // total sales per day
    public Map<LocalDate, BigDecimal> calculateTotalSalesPerDay(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedAt().toLocalDate(),  // group by date (without time)
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotal, BigDecimal::add)  // sum total for each date
                ));
    }

    /*
     * Product
     */
    // parse product from JSON string
    public List<Product> parseProduct(String jsonProduct) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode dataNode = objectMapper.readTree(jsonProduct).get("data");
        return Arrays.asList(objectMapper.treeToValue(dataNode, Product[].class));
    }

    // Get products with stock below their minimum level
    public List<Product> getLowStockAlerts(List<Product> products) {
        return products.stream()
                .filter(product -> product.getStockCount() < product.getMinimumStockLevel())
                .collect(Collectors.toList());
    }

    /*
     * Stock
     */
    // parse stock movement from JSON string
    public List<StockMovement> parseStockMovement(String jsonStockMovements) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode dataNode = objectMapper.readTree(jsonStockMovements).get("data");
        return Arrays.asList(objectMapper.treeToValue(dataNode, StockMovement[].class));
    }

    // Analyze stock allocation per warehouse
    public Map<Long, Integer> analyzeStockPerWarehouse(List<StockMovement> stockMovements) {
        return stockMovements.stream()
                .collect(Collectors.groupingBy(
                        StockMovement::getWarehouseId,
                        Collectors.summingInt(movement -> movement.getStockMovementType() == EStockMovementType.IN
                                ? movement.getQuantity()
                                : -movement.getQuantity())
                ));
    }

    /*
     * Supplier
     */
    // parse supplier from JSON string
    public List<Supplier> parseSupplier(String jsonSupplier) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode dataNode = objectMapper.readTree(jsonSupplier).get("data");
        return Arrays.asList(objectMapper.treeToValue(dataNode, Supplier[].class));
    }

    // Analyze number of suppliers per country
    public Map<String, Long> analyzeNumOfSuppliersPerCountry(List<Supplier> suppliers) {
        return suppliers.stream()
                .collect(Collectors.groupingBy(
                        Supplier::getContactInfo,
                        Collectors.counting()
                ));
    }
}
