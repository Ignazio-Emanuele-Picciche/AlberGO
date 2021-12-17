package com.ignaziopicciche.albergo.exception.enums;

public enum AmministratoreEnum {

    AMMINISTRATORE_NOT_FOUND("AMM_NF", "L'amministratore che stai cercando non è stato trovato"),
    AMMINISTRATORE_ALREADY_EXISTS("AMM_AE", "L'amministratore che vuoi inserire è già presente nel sistema"),
    AMMINISTRATORE_DELETE_ERROR("AMM_DLE", "Errore durante l'eliminazione dell'amministratore"),
    AMMINISTRATORE_ID_NOT_EXIST("AMM_IDNE", "L'amministratore che stai cercando non esiste");

    private final String messageCode;
    private final String message;

    AmministratoreEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }


    public static AmministratoreEnum getAmministratoreEnumByMessageCode(final String messageCode) {
        for (final AmministratoreEnum amministratoreEnum : AmministratoreEnum.values()) {
            if (amministratoreEnum.getMessageCode().equals(messageCode)) {
                return amministratoreEnum;
            }
        }
        return null;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }
}
