package com.itmajing.excel.exception;


/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class NoSuchConverterException extends RuntimeException {

    public NoSuchConverterException() {
    }

    public NoSuchConverterException(String message) {
        super(message);
    }

    public NoSuchConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
