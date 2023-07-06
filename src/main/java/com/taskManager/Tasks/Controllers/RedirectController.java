package com.taskManager.Tasks.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @RequestMapping("api/projects/**")
    public String redirect(){
        return "redirect:/api/project/";
    }
}
