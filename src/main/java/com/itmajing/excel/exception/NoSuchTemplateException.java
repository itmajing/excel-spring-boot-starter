package com.itmajing.excel.exception;


/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class NoSuchTemplateException extends RuntimeException {

    public NoSuchTemplateException() {
    }

    public NoSuchTemplateException(String message) {
        super(message);
    }

    public NoSuchTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
