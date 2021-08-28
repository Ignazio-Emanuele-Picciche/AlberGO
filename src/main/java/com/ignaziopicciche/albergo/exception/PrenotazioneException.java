package com.ignaziopicciche.albergo.exception;

public class PrenotazioneException extends RuntimeException{
    public enum PrenotazioneExceptionCode{
        PRENOTAZIONE_NOT_FOUND,
        PRENOTAZIONE_ALREADY_EXISTS,
        PRENOTAZIONE_DELETE_ERROR,
        PRENOTAZIONE_ID_NOT_EXIST
    }

    public PrenotazioneException(PrenotazioneExceptionCode message){
        super(message.toString());
    }
}
