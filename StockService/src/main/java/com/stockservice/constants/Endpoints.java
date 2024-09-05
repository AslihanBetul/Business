package com.stockservice.constants;

public class Endpoints
{

    // version
    public static final String VERSION = "/v1";

    //profiles
    public static final String DEV = "/dev";


    public static final String ROOT = DEV + VERSION;

    //controllers

    public static final String ORDER = "/order";
    public static final String PRODUCT = "/product";
    public static final String PRODUCTCATEGORY = "/product-category";
    public static final String STOCKMOVEMENT = "/stock-movement";
    public static final String SUPPLIER = "/supplier";
    public static final String WAREHOUSE = "/ware-house";

    //methods

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";
    public static final String FIND_ALL = "/find-all";

    public static final String FIND_BY_ID = "/find-by-id";
}
