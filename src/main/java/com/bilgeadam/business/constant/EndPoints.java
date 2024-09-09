package com.bilgeadam.business.constant;

public class EndPoints {
    public static final String VERSION = "/v1";

    //profiller
    public static final String API = "/api";  //-->sanırım bu canlı ortam
    public static final String DEV = "/dev";
    public static final String TEST = "/test";
    public static final String ROOT = DEV + VERSION;

    //entities
    public static final String MUSTERI = "/musteri";
    public static final String AUTH = "/auth";
    public static final String URUN = "/urun";
    public static final String SATIS = "/satis";
    public static final String PROJECT = "/project";
    public static final String TASK = "/task";


    //metods:
    public static final String SAVE = "/save-project";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String FINDALL = "/findall";
    public static final String FINDBYID = "/findbyid";
    public static final String FINDALLORDERBYFIYAT = "/findallorderbyfiyat";

//    dev/v1 ile başlasın
//    lcoalhost:8080/dev/v1/user/save-user (örnek oalrak)
//    Endpointler;
//    Örnek : Personal Documents
//    Yazım: personel-documents


    //    Database Tablo adları;
//    Örnek Personals
//    Database adı: tblpersonals
//    Sabit değerleri constantta tutmaya çalışalım



}
