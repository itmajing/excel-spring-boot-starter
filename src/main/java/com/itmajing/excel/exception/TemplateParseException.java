package com.itmajing.excel.exception;


/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class TemplateParseException extends RuntimeException {
    public TemplateParseException() {
        super();
    }

    public TemplateParseException(String message) {
        super(message);
    }

    public TemplateParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
