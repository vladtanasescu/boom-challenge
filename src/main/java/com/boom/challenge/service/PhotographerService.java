package com.boom.challenge.service;

import com.boom.challenge.model.Photographer;
import com.boom.challenge.repository.PhotographerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PhotographerService {

    @Autowired
    private PhotographerRepository repository;

    private final Random random = new Random();

    public void addPhotographers() {
        for (int i = 0; i < 3; i++) {
            Photographer photographer = Photographer.builder().name("Vlad " + random.nextLong()).build();
            repository.save(photographer);
        }
    }

    public List<Photographer> findAll() {
        return repository.findAll();
    }

    public Photographer findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("photographer not found"));
    }
}
