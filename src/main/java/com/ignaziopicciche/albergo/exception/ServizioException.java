package com.ignaziopicciche.albergo.exception;

public class ServizioException extends RuntimeException{

    public enum ServizioExcpetionCode{
        SERVIZIO_NOT_FOUND,
        SERVIZIO_ALREADY_EXISTS,
        SERVIZIO_DELETE_ERROR,
        SERVIZIO_ID_NOT_EXIST
    }

    public ServizioException(ServizioException.ServizioExcpetionCode message){
        super(message.toString());
    }
}
