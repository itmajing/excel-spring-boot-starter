package com.itmajing.excel.exception;


/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class ConverterException extends RuntimeException {

    public ConverterException() {
    }

    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
