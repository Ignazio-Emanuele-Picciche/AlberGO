package com.ignaziopicciche.albergo.enums;

public enum HotelEnum {

    HOTEL_NOT_FOUND("HOT_NF", "L'hotel che stai cercando non è stato trovato"),
    HOTEL_ALREADY_EXISTS("HOT_AE", "L'hotel che vuoi inserire è già presente nel sistema"),
    HOTEL_DELETE_ERROR("HOT_DLE", "Errore durante l'eliminazione dell'hotel"),
    HOTEL_ID_NOT_EXIST("HOT_IDNE", "L'hotel che stai cercando non esiste"),
    HOTEL_CODICE_HOTEL_NOT_EXIST("HOT_CHNE", "L'hotel che stai cercando non esiste");

    private final String messageCode;
    private final String message;

    HotelEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static HotelEnum getHotelEnumByMessageCode(final String messageCode) {
        for (final HotelEnum t : HotelEnum.values()) {
            if (t.getMessageCode().equals(messageCode)) {
                return t;
            }
        }
        return null;
    }
}
