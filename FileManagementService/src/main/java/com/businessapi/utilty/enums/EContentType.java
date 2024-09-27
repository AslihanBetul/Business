package com.businessapi.utilty.enums;

public enum EContentType {
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    ;

    private final String type;

    EContentType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public static EContentType fromString(String type) {
        for (EContentType contentType : EContentType.values()) {
            if (contentType.getType().equalsIgnoreCase(type)) {
                return contentType;
            }
        }
        throw new IllegalArgumentException("No enum constant for type: " + type);
    }
}
