package com.ignaziopicciche.albergo.enums;

public enum ClienteEnum {


    CLIENTE_NOT_FOUND("CLI_NF", "Il cliente che stai cercando non è stato trovato"),
    CLIENTE_ALREADY_EXISTS("CLI_AE", "Il cliente che vuoi inserire è già presente nel sistema"),
    CLIENTE_DELETE_ERROR("CLI_DLE", "Errore durante l'eliminazione del cliente"),
    CLIENTE_ID_NOT_EXIST("CLI_IDNE", "Il cliente che stai cercando non esiste");

    private final String messageCode;
    private final String message;

    ClienteEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static ClienteEnum getClienteEnumByMessageCode(final String messageCode) {
        for (final ClienteEnum clienteEnum : ClienteEnum.values()) {
            if (clienteEnum.getMessageCode().equals(messageCode)) {
                return clienteEnum;
            }
        }
        return null;
    }
}