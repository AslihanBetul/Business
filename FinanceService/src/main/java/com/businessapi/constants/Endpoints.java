package com.businessapi.constants;

public class Endpoints {

    // version
    public static final String VERSION = "/v1";

    //profiles
    public static final String DEV = "/dev";


    public static final String ROOT = DEV + VERSION;

    //controllers

    public static final String BUDGET = "/budget";
    public static final String EXPENSE = "/expense";
    public static final String FINANCIALREPORT = "/financial-report";
    public static final String INVOICE = "/invoice";
    public static final String TAX = "/tax";


    //methods

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";
    public static final String FIND_ALL = "/find-all";

    public static final String FIND_BY_ID = "/find-by-id";
}