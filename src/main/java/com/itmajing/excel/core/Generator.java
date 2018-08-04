package com.itmajing.excel.core;


import java.io.File;
import java.util.Collection;

/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public interface Generator<T> {

    File generate();

    void generate(File file);

    void writeData(Collection<T> data);
}
