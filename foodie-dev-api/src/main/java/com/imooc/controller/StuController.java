package com.imooc.controller;

import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StuController {

    private StuService stuService;

    @Autowired
    public void setStuService(StuService stuService) {
        this.stuService = stuService;
    }

    @GetMapping("/getStu")
    public Object getStu(int id){
        System.out.println(stuService.getStuInfo(id));
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public String saveStu(){
        stuService.saveStu();
        return "OK";
    }

    @PostMapping("/updateStu")
    public String updateStu(int id){
        stuService.updateStu(id);
        return "OK";
    }

    @PostMapping("/deleteStu")
    public String deleteStu(int id){
        stuService.deleteStu(id);
        return "OK";
    }
}
