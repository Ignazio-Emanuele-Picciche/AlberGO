package com.ignaziopicciche.albergo.enums;

public enum ServizioEnum{

    SERVIZIO_NOT_FOUND("SERV_NF", "Il servizio che stai cercando non è stato trovato"),
    SERVIZIO_ALREADY_EXISTS("SERV_AE", "Il servizio che vuoi inserire è già presente nel sistema"),
    SERVIZIO_DELETE_ERROR("SERV_DLE", "Errore durante l'eliminazione del servizio"),
    SERVIZIO_ID_NOT_EXIST("SERV_IDNE", "Il servizio che stai cercando non esiste");

    private final String messageCode;
    private final String message;

    ServizioEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static ServizioEnum getServizioEnumByMessageCode(final String messageCode) {
        for (final ServizioEnum t : ServizioEnum.values()) {
            if (t.getMessageCode().equals(messageCode)) {
                return t;
            }
        }
        return null;
    }
}
