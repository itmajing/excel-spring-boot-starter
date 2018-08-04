package com.itmajing.excel.model;


import java.util.List;

/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class Template {

    private String header;

    private List<Column> columns;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
