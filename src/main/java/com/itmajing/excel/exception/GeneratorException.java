package com.itmajing.excel.exception;


/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class GeneratorException extends RuntimeException {

    public GeneratorException() {
        super();
    }

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
