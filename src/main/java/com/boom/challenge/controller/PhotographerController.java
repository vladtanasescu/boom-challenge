package com.boom.challenge.controller;

import com.boom.challenge.model.Photographer;
import com.boom.challenge.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @GetMapping("photographers")
    @ResponseBody
    public List<Photographer> findAll() {
        return this.photographerService.findAll();
    }

    @GetMapping("photographers/add")
    public ResponseEntity<String> add() {
        this.photographerService.addPhotographers();
        return ResponseEntity.ok("photographers added");
    }
}
