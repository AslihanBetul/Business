package com.businessapi.constants;

public class EndPoints {
    private static final String VERSION = "/v1";
    private static final String DEV = "/dev";
    private static final String ROOT = DEV + VERSION;

    public static final String NOTIFICATIONS = ROOT + "/notifications";

    public static final String GET_ALL_NOTIFICATIONS = "/getallnotifications";
    public static final String GET_ALL_UNREAD_NOTIFICATIONS = "/getallunreadnotifications";
    public static final String GET_UNREAD_COUNT = "/getunreadcount"; // Changed to unique path
    public static final String GET_NOTIFICATION_FOR_USERID = "/getnotificationforuserid"; // Fixed typo
    public static final String CREATE_NOTIFICATION = "/createnotification";
    public static final String DELETE = "/delete";
    public static final String READ = "/read";
}
