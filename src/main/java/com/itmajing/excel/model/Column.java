package com.itmajing.excel.model;


/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class Column {

    /**
     * 列名称
     */
    private String name;

    /**
     * 字段名
     */
    private String key;

    /**
     * 转换器
     */
    private String converter;

    /**
     * 列宽度
     */
    private Integer width;

    /**
     * 自动换行
     */
    private Boolean wrap = true;

    /**
     * 对齐
     */
    private Integer align = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getWrap() {
        return wrap;
    }

    public void setWrap(Boolean wrap) {
        this.wrap = wrap;
    }

    public Integer getAlign() {
        return align;
    }

    public void setAlign(Integer align) {
        this.align = align;
    }
}
