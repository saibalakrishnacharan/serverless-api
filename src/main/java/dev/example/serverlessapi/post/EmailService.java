package dev.example.serverlessapi.post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import dev.example.serverlessapi.post.Entities.Post;
import dev.example.serverlessapi.post.Repositories.PostRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    @Autowired
    private PostRepository postRepository;

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(EmailService.class);
    public List<Post> getAllUsers() {
        return postRepository.findAll();
    }
    public String sendSimpleMessage(String to, String subject, String text) {
        try {

            List<Post> posts = getAllUsers();
            for (Post post : posts) {
                LOGGER.info(post.toString());
            }
            Workbook workbook = new XSSFWorkbook();

            // Create a blank sheet
            Sheet sheet = workbook.createSheet("Sheet1");
            for (int i = 0; i < posts.toArray().length; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(posts.get(i).getName());
                row.createCell(1).setCellValue(posts.get(i).getAge());
            }
            // Create a row and put some cells in it

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

