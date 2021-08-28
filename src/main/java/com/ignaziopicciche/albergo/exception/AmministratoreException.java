package com.ignaziopicciche.albergo.exception;

public class AmministratoreException extends RuntimeException {

    public enum AmministratoreExceptionCode{
        AMMINISTRATORE_NOT_FOUND,
        AMMINISTRATORE_ALREADY_EXISTS,
        AMMINISTRATORE_DELETE_ERROR,
        AMMINISTRATORE_ID_NOT_EXIST
    }

    public AmministratoreException(AmministratoreExceptionCode message){
        super(message.toString());
    }
}
