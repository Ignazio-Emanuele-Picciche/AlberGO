package com.ignaziopicciche.albergo.exception;

public class CategoriaException extends RuntimeException{

    public enum CategoriaExcpetionCode{
        CATEGORIA_NOT_FOUND,
        CATEGORIA_ALREADY_EXISTS,
        CATEGORIA_DELETE_ERROR,
        CATEGORIA_ID_NOT_EXIST
    }

    public CategoriaException(CategoriaExcpetionCode message){
        super(message.toString());
    }

}
