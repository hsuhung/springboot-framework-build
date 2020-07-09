package com.springboot.framework.build.example.utils;

import com.springboot.framework.build.example.utils.annotation.ExcelField;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**************************************************************
 * 创建日期：2020/1/13 14:25
 * 作    者：lixuhong
 * 功能描述：excel工具
 * HSSFWorkbook: 操作Excel2003以前（包括2003）的版本，扩展名是.xls
 * XSSFWorkbook: 是操作Excel2007的版本，扩展名是.xlsx
 * SXSSFWorkbook: 操作Excel2007后的版本，扩展名是.xlsx；
 * 自定义注解：
 * String value() default ""; Excel标题
 * int col() default 0; Excel从左往右排列位置
 **************************************************************/
public class ExcelUtils {

    /**
     * excel导出
     * @param list 需要导出的数据
     * @param clazz list的包装类
     * @param response
     * @param <T>
     */
    public static <T> void exportExcel(List<T> list, Class<T> clazz, HttpServletResponse response) throws Exception {

        // 获取被注解的字段并排序
        List<Field> fieldList = fieldList(clazz);
        // 用于记录excel行
        AtomicInteger ai = new AtomicInteger();
        // 写入表头
        Workbook workbook = writeHeader(fieldList, ai);
        // 写入数据
        workbook = writeData(list, fieldList, workbook, ai);
        // 通知浏览器下载
        buildExcelDocument("about.xlsx", workbook, response);
    }

    /**
     * excel导入
     * @param file  excel文件
     * @param clazz 返回数据包装类（该类必须包含无参数构造方法）
     * @param <T>
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz) throws Exception {

        InputStream is = file.getInputStream();
        // 输出到excel文档对象
        Workbook workbook = new XSSFWorkbook(is);
        // 获取表单对象
        Sheet sheet = workbook.getSheetAt(0);

        int firstRowNum = sheet.getFirstRowNum();

        // 获取字段
        List<Field> fieldList = fieldList(clazz);
        // 获取表头
        Row head = sheet.getRow(firstRowNum);
        // 检查
        if(!checkField(head, fieldList)){
            throw new Exception("Excel表头错误");
        }
        // 写入数据到对象
        List<T> list = wirteToObj(sheet, fieldList, clazz);

        return list;
    }

    /**
     * 检查excel表头和待转对象字段是否一致
     * @param head excel表头
     * @param fieldList 须转换的类字段
     * @return true：通过；false：不通过
     */
    private static boolean checkField(Row head, List<Field> fieldList){

        int cellNum = head.getLastCellNum();

        for (int i = 0; i < cellNum; i++) {
            Cell row = head.getCell(i);
            Field field = fieldList.get(i);
            if (field == null) {
                return false;
            }
            // 表头文字与注解描述一致
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField == null) {
                return false;
            }
            if (!excelField.value().equals(row.getStringCellValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 写入数据到对象
     * @param sheet 表单
     * @param fieldList 字段
     * @param clazz 返回数据包装类
     * @param <T>
     * @return
     */
    private static <T> List<T> wirteToObj(Sheet sheet, List<Field> fieldList, Class<T> clazz) throws Exception {

        List<T> list = new ArrayList<>();

        // 由于表头不是需要的数据，因此直接从第二行开始读
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            // 实例化对象
            T obj = null;
            try {
                obj = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new Exception("实例化对象的类缺少构造方法");
            }
            if (obj == null) {
                throw new Exception("实例化失败");
            }

            // 组装数据
            Row row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++){
                Cell cell = row.getCell(j);
                String fieldName = fieldList.get(j).getName();
                // 获取对象字段
                Field field = getFieldByName(fieldName, clazz);
                if (field == null) {
                    throw new Exception("未找到类属性: " + fieldName);
                }
                // 赋值
                obj = setFieldValueByName(cell, field, obj);
            }
            list.add(obj);
        }

        return list;
    }

    /**
     * 根据字段名给对象字段赋值
     * @param cell  单元格
     * @param field 字段
     * @param obj 对象
     * @param <T>
     * @return
     */
    private static <T> T setFieldValueByName(Cell cell, Field field, T obj) throws Exception {

        if (field != null) {
            field.setAccessible(true);
            // 字段类型
            Class<?> fieldType = field.getType();

            // 根据字段类型给字段赋值
            try {
                if (String.class == fieldType) {
                    field.set(obj, String.valueOf(cell.getStringCellValue()));
                } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                    field.set(obj, Integer.parseInt(cell.getStringCellValue()));
                } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                    field.set(obj, Long.valueOf(cell.getStringCellValue()));
                } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                    field.set(obj, Float.valueOf(cell.getStringCellValue()));
                } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                    field.set(obj, Short.valueOf(cell.getStringCellValue()));
                } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                    field.set(obj, Double.valueOf(cell.getStringCellValue()));
                }  else if ((Boolean.TYPE == fieldType) || (Boolean.class == fieldType)) {
                    field.set(obj, Boolean.valueOf(cell.getStringCellValue()));
                } else if (Character.TYPE == fieldType) {
                    if (cell.getStringCellValue().length() > 0) {
                        field.set(obj, Character.valueOf(cell.getStringCellValue().charAt(0)));
                    }
                } else if (Date.class == fieldType) {
                    field.set(obj, cell.getDateCellValue());
                } else if (LocalDateTime.class == fieldType) {
                    field.set(obj, LocalDateTime.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else {
                    field.set(obj, cell.getStringCellValue());
                }
            } catch (Exception e) {
                throw new Exception("对象字段赋值异常");
            }
        }

        return obj;
    }

    /**
     * 根据字段名获取字段
     * @param fieldName 字段名
     * @param clazz 包装类
     * @return
     */
    private static <T> Field getFieldByName(String fieldName, Class<T> clazz) {

        // 获取所有字段
        Field[] selfFields = clazz.getDeclaredFields();
        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        // 如果本类和父类都没有，则返回空
        return null;
    }

    /**
     * 获取被注解的字段并排序
     * @param clazz 被注解的类
     * @return
     */
    private static <T> List<Field> fieldList(Class<T> clazz){

        // 获取calzz类中被注解的字段
        Field[] fields = clazz.getDeclaredFields();

        List<Field> fieldList = Arrays.stream(fields)
                // 过滤未被注解的字段
                .filter(field -> {

                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    if (excelField != null) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                })
                // 对被注解的字段进行排序
                .sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    if (excelField != null) {
                        col = excelField.column();
                    }
                    return col;
                }))
                // 数组转列表
                .collect(Collectors.toList());

        return fieldList;
    }

    /**
     * 写入表头
     * @param fieldList 被注解的字段
     * @param ai 用于记录excel行
     * @return  返回写入表头后的表单对象
     */
    private static Workbook writeHeader(List<Field> fieldList, AtomicInteger ai){

        // 创建excel文档对象
        Workbook workbook = new XSSFWorkbook();
        // 创建表单对象
        Sheet sheet = workbook.createSheet("Sheet1");
        // 创建行对象
        Row row = sheet.createRow(ai.getAndIncrement());
        // 用于记录excel行中的单元格位置
        AtomicInteger aj = new AtomicInteger();
        // 写入头部数据
        fieldList.forEach(field -> {

            // 获取被注解字段的value
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            String columValue = "";
            if (excelField != null) {
                columValue = excelField.value();
            }

            // 创建单元格对象
            Cell cell = row.createCell(aj.getAndIncrement());
            // 单元格样式
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 单元格样式
            Font font = workbook.createFont();
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columValue);
        });

        return workbook;
    }

    /**
     * 写入数据
     * @param list 需要写入的数据
     * @param fieldList 被注解的字段
     * @param workbook 文档对象
     * @param ai 用于记录excel行
     * @return  返回写入表头后的表单对象
     */
    private static <T> Workbook writeData(List<T> list, List<Field> fieldList, Workbook workbook, AtomicInteger ai){

        // 获取表单对象
        Sheet sheet = workbook.getSheetAt(0);
        // 写入数据
        if (list != null && list.size() > 0) {
            list.forEach(data -> {

                // 创建行
                Row row = sheet.createRow(ai.getAndIncrement());
                // 用于记录excel行中的单元格位置
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {

                    // 获取字段类型
                    Class<?> fieldType = field.getType();
                    // 获取字段数据
                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(data);
                    } catch (Exception e) {
                        System.out.println("--- excel取值异常 ---");
                        e.printStackTrace();
                    }

                    // 创建单元格
                    Cell cell = row.createCell(aj.getAndIncrement());

                    // 写入数据
                    if(fieldValue != null){

                        // 日期格式转换
                        if (fieldType == Date.class) {
                            Date date = (Date) fieldValue;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateStr = format.format(date);
                            cell.setCellValue(dateStr);
                        } else if (fieldType == LocalDateTime.class) {
                            LocalDateTime dateTime = (LocalDateTime) fieldValue;
                            String dateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);
                            cell.setCellValue(dateStr);
                        } else {
                            cell.setCellValue(fieldValue.toString());
                        }
                    }
                });
            });
        }

        return workbook;
    }

    /**
     * 浏览器下载excel
     * @param fileName excel文件名称
     * @param workbook excel文档对象
     * @param response 返回
     */
    private static  void  buildExcelDocument(String fileName, Workbook workbook, HttpServletResponse response) throws Exception {

        try {
            // 冻结窗口
            workbook.getSheetAt(0).createFreezePane(0, 1, 0, 1);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());

        } catch (Exception e) {
            throw new Exception("Excel下载错误");
        }
    }
}
