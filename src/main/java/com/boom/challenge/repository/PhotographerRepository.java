package com.boom.challenge.repository;

import com.boom.challenge.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Long> {
    Optional<Photographer> findById(Long id);
    List<Photographer> findAll();
}
