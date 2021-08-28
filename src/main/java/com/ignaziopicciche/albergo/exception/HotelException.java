package com.ignaziopicciche.albergo.exception;

public class HotelException extends RuntimeException{

    public enum HotelExceptionCode{
        HOTEL_NOT_FOUND,
        HOTEL_ALREADY_EXISTS,
        HOTEL_DELETE_ERROR,
        HOTEL_ID_NOT_EXIST
    }

    public HotelException(HotelExceptionCode message){
        super(message.toString());
    }
}
