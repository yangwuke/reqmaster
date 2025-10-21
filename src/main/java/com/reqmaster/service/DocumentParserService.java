package com.reqmaster.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 文档解析服务
 */
@Service
@Slf4j
public class DocumentParserService {

    /**
     * 解析上传的文档
     */
    public String parseDocument(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = getFileType(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            return switch (fileType.toLowerCase()) {
                case "pdf" -> parsePdf(inputStream);
                case "docx" -> parseDocx(inputStream);
                case "txt" -> parseText(inputStream);
                default -> throw new IllegalArgumentException("不支持的文件类型: " + fileType);
            };
        }
    }

    /**
     * 解析PDF文档
     */
//    private String parsePdf(InputStream inputStream) throws IOException {
//        // 补充 MemoryUsageSetting 参数，使用默认的内存加载模式
////        try (PDDocument document = PDDocument.load(inputStream, MemoryUsageSetting.MEMORY_ONLY))
//        // 使用静态方法创建内存设置（3.0.1版本唯一正确方式）
//        try (PDDocument document = PDDocument.load(inputStream, MemoryUsageSetting.createNormalMemoryUsageSetting()))
//        {
//            PDFTextStripper stripper = new PDFTextStripper();
//            stripper.setSortByPosition(true);
//            return stripper.getText(document);
//        }
//    }
    /**
     * 解析PDF文档
     */
    private String parsePdf(InputStream inputStream) throws IOException {
        // 将 InputStream 包装为 RandomAccessRead
        try (RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(inputStream);
             PDDocument document = Loader.loadPDF(buffer)) { // ✅ 去掉 MemoryUsageSetting

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document);
        }
    }

    /**
     * 解析Word文档
     */
    private String parseDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    /**
     * 解析文本文件
     */
    private String parseText(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes());
    }

    /**
     * 获取文件类型
     */
    private String getFileType(String fileName) {
        if (fileName == null) {
            return "txt";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "txt";
    }

    /**
     * 检查文件类型是否支持
     */
    public boolean isSupportedFileType(String fileName) {
        String fileType = getFileType(fileName);
        return fileType.equalsIgnoreCase("pdf") ||
                fileType.equalsIgnoreCase("docx") ||
                fileType.equalsIgnoreCase("txt");
    }
}