package com.itmajing.excel.core;


import com.itmajing.excel.exception.GeneratorException;
import com.itmajing.excel.exception.NoSuchConverterException;
import com.itmajing.excel.model.Column;
import com.itmajing.excel.model.Template;
import com.itmajing.excel.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author <a href="itmajing.com">MaJing</a>
 */
public class SimpleGenerator<T> implements Generator<T> {

    private Map<String,Converter> converters;

    private Template template;

    private List<Column> columns;

    private Integer columnNum;

    private SXSSFWorkbook workbook;

    private Sheet sheet;

    private Integer rowCount;

    private List<CellStyle> columnStyles;

    public SimpleGenerator(String template) {
        /*init*/
        this.converters = SpringContextUtil.getBeansOfType(Converter.class);
        this.template = TemplateFactory.getTemplate(template);
        this.columns = this.template.getColumns();
        this.columnNum = this.columns.size();
        this.workbook = new SXSSFWorkbook();
        this.sheet = this.workbook.createSheet();
        this.rowCount = 0;
        this.columnStyles = new ArrayList<>();

        /*prepare write*/
        this.preWriteData();
    }

    @Override
    public File generate() {
        File file = this.createNewFile();
        this.generate(file);
        return file;
    }

    @Override
    public void generate(File file) {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            workbook.dispose();
        } catch (Exception e) {
            throw new GeneratorException("write excel file failed", e);
        }
    }

    @Override
    public void writeData(Collection<T> data) {
        for (T t : data) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 0; i < columnNum; i++) {
                Cell cell = row.createCell(i);
                Column column = columns.get(i);
                if (columnStyles.size() <= i) {
                    CellStyle style = this.createContentStyle();
                    Integer align = column.getAlign();
                    switch (align) {
                        case -1:
                            style.setAlignment(CellStyle.ALIGN_LEFT);
                            break;
                        case 0:
                            style.setAlignment(CellStyle.ALIGN_CENTER);
                            break;
                        case 1:
                            style.setAlignment(CellStyle.ALIGN_RIGHT);
                            break;
                        default:
                            style.setAlignment(CellStyle.ALIGN_LEFT);
                            break;
                    }
                    style.setWrapText(column.getWrap());
                    columnStyles.add(style);
                    cell.setCellStyle(style);
                } else {
                    cell.setCellStyle(columnStyles.get(i));
                }
                Object object = null;
                for(Class<?> clazz = t.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
                    try {
                        Field field = clazz.getDeclaredField(column.getKey());
                        field.setAccessible(true);
                        object = field.get(t);
                        if (object == null) {
                            object = "";
                        }
                        break;
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        //don't handle this exception

                    }
                }
                if (object == null) {
                    throw new GeneratorException(String.format("can't find the field with name: %s", column.getKey()));
                }
                String converterName = column.getConverter();
                if (StringUtils.isNotBlank(converterName)) {
                    Converter converter = converters.get(converterName);
                    if (converter == null) {
                        throw new NoSuchConverterException(String.format("can't find the converter with name: %s", converterName));
                    }
                    cell.setCellValue(converter.convert(object));
                }
                cell.setCellValue(object.toString());
            }
        }
    }

    protected void writeHeader() {
        CellRangeAddress region = new CellRangeAddress(rowCount, rowCount, 0, columnNum - 1);
        sheet.addMergedRegion(region);
        Row row = sheet.createRow(rowCount++);
        Cell cell = row.createCell(0);
        cell.setCellValue(template.getHeader());
        cell.setCellStyle(this.createHeaderStyle());
    }

    protected void writeInfo() {
        //rewrite this method to show custom info
    }

    protected void writeTitle() {
        Row row = sheet.createRow(rowCount++);
        for (int i = 0; i < columnNum; i++) {
            Cell cell = row.createCell(i);
            Column column = columns.get(i);
            cell.setCellValue(column.getName());
            cell.setCellStyle(this.createTitleStyle());
        }
    }

    protected void setWidth() {
        for (int i = 0; i < columnNum; i++) {
            Column column = columns.get(i);
            Integer width = column.getWidth();
            if (width != null && !width.equals(0)) {
                sheet.setColumnWidth(i, width * 256);
            } else {
                sheet.setColumnWidth(i, column.getName().getBytes().length * 256);
            }
        }
    }

    protected void preWriteData() {
        this.writeHeader();
        this.writeInfo();
        this.writeTitle();
        this.setWidth();
    }

    protected File createNewFile() {
        try {
            File home = SystemUtils.getUserHome();
            String dirPath = String.format("%s%s%s", home, File.separator, "temp");
            File dir = new File(dirPath);
            if (!dir.exists()) {
                boolean mkdir = dir.mkdir();
                if (!mkdir) {
                    throw new GeneratorException("create temp directory failed");
                }
            }
            String filePath = String.format("%s%s%s.xlsx", dirPath, File.separator, UUID.randomUUID());
            File file = new File(filePath);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    throw new GeneratorException("create excel file failed");
                }
            }
            return file;
        } catch (Exception e) {
            throw new GeneratorException("create new file failed", e);
        }
    }

    protected Font createHeaderFont() {
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        return font;
    }

    protected Font createTitleFont() {
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 12);
        return font;
    }

    protected Font createContentFont() {
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        return font;
    }

    protected CellStyle createHeaderStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFont(this.createHeaderFont());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        return style;
    }

    protected CellStyle createTitleStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFont(this.createTitleFont());
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_80_PERCENT.index);
        style.setRightBorderColor(IndexedColors.GREY_80_PERCENT.index);
        style.setBottomBorderColor(IndexedColors.GREY_80_PERCENT.index);
        style.setLeftBorderColor(IndexedColors.GREY_80_PERCENT.index);
        return style;
    }

    protected CellStyle createContentStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFont(this.createContentFont());
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
        return style;
    }
}
