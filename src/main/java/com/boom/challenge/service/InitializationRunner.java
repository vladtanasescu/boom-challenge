package com.boom.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class InitializationRunner implements CommandLineRunner {

    @Autowired
    private PhotographerService photographerService;

    @Override
    public void run(String... args) {
        photographerService.addPhotographers();
    }
}
