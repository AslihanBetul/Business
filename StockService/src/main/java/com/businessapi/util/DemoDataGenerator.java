package com.businessapi.util;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.CustomerSaveRequestDTO;
import com.businessapi.entities.enums.EStockMovementType;
import com.businessapi.services.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator
{
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final OrderService orderService;
    private final SupplierService supplierService;
    private final StockMovementService stockMovementService;
    private final WareHouseService wareHouseService;
    private final CustomerService customerService;

    @PostConstruct
    public void generateDemoData()
    {
        productCategoryDemoData();
        productDemoData();
        customerDemoData();
        supplierDemoData();
        wareHouseDemoData();
        orderDemoData();
        stockMovementDemoData();

    }

    private void productCategoryDemoData()
    {
        productCategoryService.save(new ProductCategorySaveRequestDTO("Electronics"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Home Appliances"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Clothing"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Books"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Toys"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Sports Equipment"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Furniture"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Beauty Products"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Jewelry"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Health & Wellness"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Automotive"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Grocery"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Pet Supplies"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Office Supplies"));
        productCategoryService.save(new ProductCategorySaveRequestDTO("Musical Instruments"));

    }

    private void productDemoData()
    {
        productService.save(new ProductSaveRequestDTO(1L,1L,1L, "iPhone 13", "Smart Phone", BigDecimal.valueOf(50000), 100, 10));
        productService.save(new ProductSaveRequestDTO(2L,1L,2L, "Samsung Galaxy S21", "Smart Phone", BigDecimal.valueOf(45000), 80, 15));
        productService.save(new ProductSaveRequestDTO(2L,5L,3L, "Sony Bravia 55", "Television", BigDecimal.valueOf(70000), 50, 100));
        productService.save(new ProductSaveRequestDTO(4L,5L,4L, "HP Pavilion Laptop", "Laptop", BigDecimal.valueOf(60000), 40, 100));
        productService.save(new ProductSaveRequestDTO(1L,4L,5L, "Canon EOS 250D", "Camera", BigDecimal.valueOf(30000), 30, 12));
        productService.save(new ProductSaveRequestDTO(4L,1L,6L, "PlayStation 5", "Gaming Console", BigDecimal.valueOf(75000), 20, 4));
        productService.save(new ProductSaveRequestDTO(1L,1L,7L, "KitchenAid Mixer", "Home Appliance", BigDecimal.valueOf(20000), 60, 10));
        productService.save(new ProductSaveRequestDTO(1L,3L,5L, "Nike Air Max", "Shoes", BigDecimal.valueOf(15000), 120, 20));
        productService.save(new ProductSaveRequestDTO(5L,1L,10L, "Levi's 501 Jeans", "Clothing", BigDecimal.valueOf(5000), 200, 30));
        productService.save(new ProductSaveRequestDTO(1L,5L,11L, "The Great Gatsby", "Book", BigDecimal.valueOf(300), 500, 50));
        productService.save(new ProductSaveRequestDTO(6L,1L,12L, "Apple Watch Series 7", "Smart Watch", BigDecimal.valueOf(30000), 75, 7));
        productService.save(new ProductSaveRequestDTO(11L,3L,2L, "Dyson V11 Vacuum", "Home Appliance", BigDecimal.valueOf(40000), 25, 100));
        productService.save(new ProductSaveRequestDTO(2L,5L,3L, "Bose QuietComfort 35", "Headphones", BigDecimal.valueOf(25000), 45, 100));
        productService.save(new ProductSaveRequestDTO(1L,1L,4L, "Adidas Soccer Ball", "Sports Equipment", BigDecimal.valueOf(1200), 150, 25));
        productService.save(new ProductSaveRequestDTO(2L,3L,5L, "Fitbit Charge 5", "Fitness Tracker", BigDecimal.valueOf(12000), 90, 10));

    }

    private void supplierDemoData()
    {

        supplierService.save(new SupplierSaveRequestDTO("Apple","Production" ,"apple@gmail.com", "USA","Apple address", "Some notes about Apple"));
        supplierService.save(new SupplierSaveRequestDTO("Samsung", "Production" ,"samsung@gmail.com","South Korea", "Samsung HQ, Seoul", "Leading tech company in mobile devices"));
        supplierService.save(new SupplierSaveRequestDTO("Sony", "Production" ,"sony@gmail.com","Japan", "Sony Tower, Tokyo", "Renowned for electronics and entertainment products"));
        supplierService.save(new SupplierSaveRequestDTO("Microsoft","Production" ,"microsoft@gmail.com", "USA", "Microsoft Campus, Redmond", "Global leader in software development"));
        supplierService.save(new SupplierSaveRequestDTO("Nike", "Production" ,"nike@gmail.com","USA", "Nike World HQ, Oregon", "Top brand for sportswear and equipment"));
        supplierService.save(new SupplierSaveRequestDTO("Adidas", "Production" ,"adidas@gmail.com","Germany", "Adidas HQ, Herzogenaurach", "Famous for sports shoes and apparel"));
        supplierService.save(new SupplierSaveRequestDTO("Canon", "Production" ,"canon@gmail.com","Japan", "Canon HQ, Tokyo", "Specializes in cameras and imaging solutions"));
        supplierService.save(new SupplierSaveRequestDTO("HP", "Production" ,"hp@gmail.com","USA", "HP HQ, Palo Alto", "Known for printers and personal computing devices"));
        supplierService.save(new SupplierSaveRequestDTO("LG", "Production" ,"lg@gmail.com","South Korea", "LG Twin Towers, Seoul", "Major producer of electronics and home appliances"));
        supplierService.save(new SupplierSaveRequestDTO("Dell","Production" ,"dell@gmail.com", "USA", "Dell Technologies, Texas", "Leading company in computers and IT infrastructure"));
        supplierService.save(new SupplierSaveRequestDTO("Can Deniz","Gumus" ,"celestialalpacastudios@gmail.com", "Turkey", "Istanbul", "Technology company"));


    }

    private void wareHouseDemoData()
    {

        wareHouseService.save(new WareHouseSaveRequestDTO("Kartal", "Main Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Pendik", "Pendik Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Kadıköy", "Kadıköy Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Ümraniye", "Ümraniye Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Maltepe", "Maltepe Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Ataşehir", "Ataşehir Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Tuzla", "Tuzla Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Beşiktaş", "Beşiktaş Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Şişli", "Şişli Warehouse address"));
        wareHouseService.save(new WareHouseSaveRequestDTO("Sarıyer", "Sarıyer Warehouse address"));




    }

    private void customerDemoData()
    {

        customerService.save(new CustomerSaveRequestDTO("John", "Doe", "johndoe@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Jane", "Doe", "janedoe@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Bob", "Smith", "bobsmith@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Alice", "Johnson", "alicejohnson@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Tom", "Lee", "tomlee@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Sarah", "Brown", "sarahbrown@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Michael", "Davis", "michaeldavis@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Emily", "Wilson", "emilywilson@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Olivia", "Martinez", "oliviamartinez@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("William", "Anderson", "williamanderson@gmail.com"));
        customerService.save(new CustomerSaveRequestDTO("Ava", "Thomas", "avathomas@gmail.com"));

    }

    private void orderDemoData()
    {

        orderService.saveSellOrder(new SellOrderSaveRequestDTO(1L, 2L, 10));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(2L, 5L, 15));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(3L, 8L, 20));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(4L, 10L, 5));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(5L, 12L, 8));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(6L, 1L, 30));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(7L, 3L, 25));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(8L, 7L, 12));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(9L, 9L, 18));
        orderService.saveSellOrder(new SellOrderSaveRequestDTO(10L, 14L, 22));





    }

    private void stockMovementDemoData()
    {

        stockMovementService.save(new StockMovementSaveDTO(1L, 2L, 10, EStockMovementType.IN));
        stockMovementService.save(new StockMovementSaveDTO(2L, 1L, 15, EStockMovementType.OUT));
        stockMovementService.save(new StockMovementSaveDTO(3L, 4L, 20, EStockMovementType.IN));
        stockMovementService.save(new StockMovementSaveDTO(4L, 3L, 22, EStockMovementType.OUT));
        stockMovementService.save(new StockMovementSaveDTO(5L, 6L, 2, EStockMovementType.IN));
        stockMovementService.save(new StockMovementSaveDTO(6L, 5L, 7, EStockMovementType.OUT));
        stockMovementService.save(new StockMovementSaveDTO(7L, 8L, 3, EStockMovementType.IN));
        stockMovementService.save(new StockMovementSaveDTO(8L, 7L, 5, EStockMovementType.OUT));
        stockMovementService.save(new StockMovementSaveDTO(9L, 10L, 15, EStockMovementType.IN));
        stockMovementService.save(new StockMovementSaveDTO(10L, 9L, 25, EStockMovementType.OUT));

    }



}
