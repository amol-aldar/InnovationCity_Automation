package org.rakdao.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteToExcel {

    private static final String FILE_PATH = System.getProperty("user.dir") + "/target/LeadRunData.xlsx";

    public static synchronized void writeRunData(
            String url,
            String enquiryType,
            String firstName,
            String lastName,
            String email,
            String mobile,
            boolean status
    ) {
        try {
            File file = new File(FILE_PATH);
            Workbook workbook;
            Sheet sheet;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("RunData");

                // Create header row
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Timestamp");
                header.createCell(1).setCellValue("URL");
                header.createCell(2).setCellValue("Enquiry Type");
                header.createCell(3).setCellValue("First Name");
                header.createCell(4).setCellValue("Last Name");
                header.createCell(5).setCellValue("Email");
                header.createCell(6).setCellValue("Mobile");
                header.createCell(7).setCellValue("Status");
            }

            // Append new row
            int lastRow = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRow + 1);

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            row.createCell(0).setCellValue(timestamp);
            row.createCell(1).setCellValue(url);
            row.createCell(2).setCellValue(enquiryType);
            row.createCell(3).setCellValue(firstName);
            row.createCell(4).setCellValue(lastName);
            row.createCell(5).setCellValue(email);
            row.createCell(6).setCellValue(mobile);
            row.createCell(7).setCellValue(status ? "SUCCESS" : "FAIL");

            // Write file
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
