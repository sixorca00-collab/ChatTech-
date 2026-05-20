package com.chattech.chattech.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String redirectToChat() {
        // Redirección HTTP 302 explícita y correcta hacia tu endpoint real
        return "redirect:/ui/chat";
    }
}