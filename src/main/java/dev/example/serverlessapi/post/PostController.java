package dev.example.serverlessapi.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")

public class PostController {

   private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping
    String  sendEmail(@RequestBody String email) {
        LOGGER.info("Email Request: {}", email);
        //return "sending done";
        return emailService.sendSimpleMessage("saibalakrishna7799@gmail.com","Dummy email","here is the mail");

    }
}
