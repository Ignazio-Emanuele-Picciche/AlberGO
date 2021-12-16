package com.ignaziopicciche.albergo.exception.enums;

public enum PrenotazioneEnum {


    PRENOTAZIONE_NOT_FOUND("PREN_NF", "La prenotazione che stai cercando non è stata trovata"),
    PRENOTAZIONE_ALREADY_EXISTS("PREN_AE", "La prenotazione che vuoi inserire è già presente nel sistema"),
    PRENOTAZIONE_DELETE_ERROR("PREN_DLE", "Errore durante l'eliminazione della prenotazione"),
    PRENOTAZIONE_ID_NOT_EXIST("PREN_IDNE", "La prenotazione che stai cercando non esiste"),
    PRENOTAZIONE_DATE_NOT_COMPATIBLE("PREN_DNC", "Le date della prenotazioni non sono compatibili"),
    DATE_ERROR("PREN_DE", "Date inserite non corrette"),
    PRENOTAZIONE_LIMITE_UPDATE("PREN_LUP", "Impossibile modificare la prenotazione per i limiti");

    private final String messageCode;
    private final String message;

    PrenotazioneEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }


    public static PrenotazioneEnum getPrenotazioneEnumByMessageCode(final String messageCode) {
        for (final PrenotazioneEnum t : PrenotazioneEnum.values()) {
            if (t.getMessageCode().equals(messageCode)) {
                return t;
            }
        }
        return null;
    }


}
