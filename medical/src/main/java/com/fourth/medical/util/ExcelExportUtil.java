package com.fourth.medical.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Excel导出工具类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供基于EasyExcel的数据导出功能</li>
 *   <li>支持自定义表头和样式配置</li>
 *   <li>自动生成带时间戳的文件名</li>
 *   <li>支持HTTP响应流式下载</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>简单易用：提供多个重载方法简化调用</li>
 *   <li>样式配置：支持表头和内容样式自定义</li>
 *   <li>文件名生成：自动生成带时间戳的随机文件名</li>
 *   <li>编码处理：支持中文文件名URL编码</li>
 *   <li>类型转换：内置Long类型转String转换器</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>用户数据导出</li>
 *   <li>报表数据下载</li>
 *   <li>统计信息导出</li>
 *   <li>数据备份和迁移</li>
 *   <li>批量数据处理</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于EasyExcel框架</li>
 *   <li>支持Apache POI样式配置</li>
 *   <li>HTTP响应流式输出</li>
 *   <li>自动资源管理和清理</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展支持更多数据类型转换</li>
 *   <li>可添加自定义样式策略</li>
 *   <li>可支持多Sheet导出</li>
 *   <li>可集成文件存储服务</li>
 * </ul>
 *
 * @author geekidea
 * @date 2022/8/22
 **/
public class ExcelExportUtil {

    private static final String CONTENT_TYPE = "application/vnd.ms-excel";
    private static final String CONTENT_DISPOSITION = "Content-disposition";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String EXCEL_SUFFIX = ".xlsx";


    /**
     * 导出Excel文件（使用默认文件名）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>将数据列表导出为Excel文件</li>
     *   <li>自动生成带时间戳的文件名</li>
     *   <li>使用默认样式配置</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * List<User> userList = userService.getUserList();
     * ExcelExportUtil.export(userList, User.class, response);
     * }</pre>
     *
     * @param list 要导出的数据列表
     * @param headClass 表头类，用于定义Excel列结构
     * @param response HTTP响应对象
     */
    public static void export(List<?> list, Class<?> headClass, HttpServletResponse response) {
        export(list, headClass, null, null, response);
    }

    /**
     * 导出Excel文件（自定义文件名）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>将数据列表导出为Excel文件</li>
     *   <li>使用指定的文件名</li>
     *   <li>使用默认样式配置</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * List<User> userList = userService.getUserList();
     * ExcelExportUtil.export(userList, User.class, "用户列表", response);
     * }</pre>
     *
     * @param list 要导出的数据列表
     * @param headClass 表头类，用于定义Excel列结构
     * @param exportFileName 导出文件名
     * @param response HTTP响应对象
     */
    public static void export(List<?> list, Class<?> headClass, String exportFileName, HttpServletResponse response) {
        export(list, headClass, exportFileName, null, response);
    }

    /**
     * 导出Excel文件（完整配置）
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>将数据列表导出为Excel文件</li>
     *   <li>支持自定义文件名和Sheet名称</li>
     *   <li>配置表头和内容样式</li>
     *   <li>自动处理文件下载响应头</li>
     * </ul>
     * 
     * <p><strong>样式配置：</strong></p>
     * <ul>
     *   <li>表头：白色背景</li>
     *   <li>内容：水平居中对齐</li>
     *   <li>类型转换：Long类型自动转为String</li>
     * </ul>
     * 
     * <p><strong>文件名处理：</strong></p>
     * <ul>
     *   <li>如果文件名为空，自动生成时间戳+随机数</li>
     *   <li>自动进行URL编码处理中文</li>
     *   <li>自动添加.xlsx后缀</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * List<User> userList = userService.getUserList();
     * ExcelExportUtil.export(userList, User.class, "用户数据", "用户信息", response);
     * }</pre>
     *
     * @param list 要导出的数据列表
     * @param headClass 表头类，用于定义Excel列结构
     * @param exportFileName 导出文件名，为空时自动生成
     * @param sheetName Sheet名称，可为空
     * @param response HTTP响应对象
     */
    public static void export(List<?> list, Class<?> headClass, String exportFileName, String sheetName, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        ExcelWriter excelWriter = null;
        try {
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置内容水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            if (StringUtils.isBlank(exportFileName)) {
                String currentTime = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
                String random = RandomStringUtils.randomNumeric(6);
                exportFileName = currentTime + random;
            }
            exportFileName = URLEncoder.encode(exportFileName, CHARACTER_ENCODING);
            response.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + exportFileName + EXCEL_SUFFIX);
            excelWriter = EasyExcel.write(response.getOutputStream())
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .registerConverter(new LongStringConverter())
                    .build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, sheetName).head(headClass).build();
            excelWriter.write(list, mainSheet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

}
