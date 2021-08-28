package com.ignaziopicciche.albergo.exception;

public class ClienteException extends RuntimeException{

    public enum ClienteExcpetionCode{
        CLIENTE_NOT_FOUND,
        CLIENTE_ALREADY_EXISTS,
        CLIENTE_DELETE_ERROR,
        CLIENTE_ID_NOT_EXIST
    }

    public ClienteException(ClienteExcpetionCode message){
        super(message.toString());
    }
}
