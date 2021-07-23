package com.boom.challenge.repository;

import com.boom.challenge.model.File;
import org.springframework.stereotype.Service;

@Service
public class FileRepository {

    public String storeFile(File file) {
        // store the file somewhere and generate an url from where the file can be downloaded
        return "http://storage/" + file.getFileName();
    }
}
