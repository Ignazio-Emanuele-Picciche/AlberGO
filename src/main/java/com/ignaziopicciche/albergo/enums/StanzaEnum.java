package com.ignaziopicciche.albergo.enums;

public enum StanzaEnum {

    STANZA_NOT_FOUND("STA_NF", "La stanza che stai cercando non è stata trovata"),
    STANZA_ALREADY_EXISTS("STA_AE", "La stanza che vuoi inserire è già presente nel sistema"),
    STANZA_DELETE_ERROR("STA_DLE", "Errore durante l'eliminazione della stanza"),
    STANZA_ID_NOT_EXIST("STA_IDNE", "La stanza che stai cercando non esiste");


    private final String messageCode;
    private final String message;

    StanzaEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static StanzaEnum getStanzaEnumByMessageCode(final String messageCode) {
        for (final StanzaEnum t : StanzaEnum.values()) {
            if (t.getMessageCode().equals(messageCode)) {
                return t;
            }
        }
        return null;
    }

}
