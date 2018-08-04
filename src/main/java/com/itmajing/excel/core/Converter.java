package com.itmajing.excel.core;


/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public interface Converter {

    /**
     * 转换数据
     * @param o 源数据
     * @return String
     */
    String convert(Object o);
}
