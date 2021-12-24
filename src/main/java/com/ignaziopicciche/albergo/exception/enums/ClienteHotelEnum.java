package com.ignaziopicciche.albergo.exception.enums;

public enum ClienteHotelEnum {

    CLIENTE_HOTEL_PM_NOT_FOUND("PM_NF", "La card che stai ceracando non è presente nel sistema"),
    CLIENTE_HOTEL_ATTACH_CARD_CLIENT_ERROR("ATTCARD", "Errore durante l'assegnazione della card al cliente"),
    CLIENTE_HOTEL_CREATE_CARD_ERROR("CREATE_PM", "Errore durante la creazione della card"),
    CLIENTE_HOTEL_DELETE_CARD_ERROR("DELETE_PM", "Errore durante l'eliminazione della card");


    private final String messageCode;
    private final String message;

    ClienteHotelEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static ClienteHotelEnum getClienteHotelEnumByMessageCode(final String messageCode) {
        for (final ClienteHotelEnum t : ClienteHotelEnum.values()) {
            if (t.getMessageCode().equals(messageCode)) {
                return t;
            }
        }
        return null;
    }
}
