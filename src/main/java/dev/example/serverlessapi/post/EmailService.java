package dev.example.serverlessapi.post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage; // Import MimeMessage from Jakarta Mail

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public String sendSimpleMessage(String to, String subject, String text) {
        try {
            Workbook workbook = new XSSFWorkbook();

            // Create a blank sheet
            Sheet sheet = workbook.createSheet("Sheet1");

            // Create a row and put some cells in it
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Name");
            row.createCell(1).setCellValue("Age");

            // Write the workbook content to a file
            File file = new File("workbook.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                System.out.println("Excel file created successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Attach Excel file
            MimeMessage message = emailSender.createMimeMessage();

            // Enable multipart mode
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set recipient, subject, and body
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            // Attach Excel file using ByteArrayResource
            helper.addAttachment("excel_sheet.xlsx", new ByteArrayResource(outputStream.toByteArray()));

            // Send email
            emailSender.send(message);
            if (file.exists()) {
                file.delete();
                System.out.println("Excel file deleted successfully!");
            }
            return "email sent";
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return "failed to send email";
        }
    }
}

