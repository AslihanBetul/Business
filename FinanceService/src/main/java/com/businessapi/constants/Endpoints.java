package com.businessapi.constants;

public class Endpoints {

    // version
    public static final String VERSION = "/v1";

    //profiles
    public static final String DEV = "/dev";

    public static final String MICROSERVICE = "/finance";


    public static final String ROOT = DEV + VERSION + MICROSERVICE;

    //controllers

    public static final String BUDGET = "/budget";
    public static final String EXPENSE = "/expense";
    public static final String FINANCIALREPORT = "/financial-report";
    public static final String INVOICE = "/invoice";
    public static final String TAX = "/tax";
    public static final String INCOME = "/income";
    public static final String DECLARATION = "/declaration";


    //methods

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";
    public static final String FIND_ALL = "/find-all";
    public static final String FIND_BY_ID = "/find-by-id";
    public static final String FIND_BY_CATEGORY = "/find-by-category";
    public static final String APPROVE = "/approve";
    public static final String REJECT = "/reject";
    public static final String CALCULATE = "/calculate";
    public static final String FIND_BY_DATE = "/find-by-date";
    public static final String CREATE_FOR_INCOME_TAX = "/create-for-income-tax";
    public static final String CREATE_FOR_VAT = "/create-for-vat";
    public static final String CREATE_FOR_CORPORATE_TAX = "/create-for-corporate-tax";
    public static final String CREATE = "/create";
    public static final String COMPARE = "/compare";
    public static final String GET_ALL_CATEGORIES = "/get-all-categories";
    public static final String GET_FOR_MONTHS = "/get-for-months";
}