package com.ignaziopicciche.albergo.exception;

public class StanzaException extends RuntimeException{

    public enum StanzaExceptionCode{
        STANZA_NOT_FOUND,
        STANZA_ALREADY_EXISTS,
        STANZA_DELETE_ERROR,
        STANZA_ID_NOT_EXIST
    }

    public StanzaException(StanzaExceptionCode message){
        super(message.toString());
    }
}
