package com.businessapi.constants;

public class EndPoints {
    public static final String VERSION="/v1";
    //profiles:
    public static final String API="/api";
    public static final String DEV="/dev";
    public static final String TEST="/test";

    public static final String ROOT=DEV+VERSION;


    //entities:
    public static final String CUSTOMER=ROOT+"/customer";
    public static final String USER=ROOT+"/user";
    public static final String TICKET=ROOT+"/ticket";
    public static final String OPPORTUNITY=ROOT+"/opportunity";
    public static final String SALESACTIVITY=ROOT+"/salesactivity";
    public static final String MARKETINGCAMPAIGN=ROOT+"/marketingcampaign";
    public static final String SAVE = "/save";
    public static final String FINDALL = "/find-all";
    public static final String FINDBYID = "/find-by-id";
    public static final String FINDBYNAME = "/find-by-name";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String FINDCUSTOMERBYUSERID = "/find-customer-by-user-id" ;
    public static final String CREATETOKEN = "/create-token";
    public static final String FINDALLBYTOKEN = "/find-all-by-token";
    public static final String FINDALLBYTOKEN2 = "/find-all-by-token2";
}
