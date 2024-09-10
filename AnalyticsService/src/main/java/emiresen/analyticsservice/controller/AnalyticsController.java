package emiresen.analyticsservice.controller;


import emiresen.analyticsservice.entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Controller
public class AnalyticsController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ChatMessage greeting(@Payload ChatMessage message) throws Exception {
        message.setTimeStamp(new Date());
        return message;

    }

}
