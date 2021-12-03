package com.ignaziopicciche.albergo.enums;

public enum CategoriaEnum {

    CATEGORIA_NOT_FOUND("CAT_NF", "La categoria che stai cercando non è stata trovata"),
    CATEGORIA_ALREADY_EXISTS("CAT_AE", "La categoria che vuoi inserire è già presente nel sistema"),
    CATEGORIA_DELETE_ERROR("CAT_DLE", "Errore durante l'eliminazione della categoria"),
    CATEGORIA_ID_NOT_EXIST("CAT_IDNE", "La categoria che stai cercando non esiste");


    private final String messageCode;
    private final String message;

    CategoriaEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }


    public static CategoriaEnum getCategoriaEnumByMessageCode(final String messageCode) {
        for (final CategoriaEnum categoriaEnum : CategoriaEnum.values()) {
            if (categoriaEnum.getMessageCode().equals(messageCode)) {
                return categoriaEnum;
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
