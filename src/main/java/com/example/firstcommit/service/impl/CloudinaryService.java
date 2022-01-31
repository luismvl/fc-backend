package com.example.firstcommit.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private String cloudinaryUrl = System.getenv("CLOUDINARY_URL");

    public CloudinaryService() {
        cloudinary = new Cloudinary(cloudinaryUrl);
    }

    public Map<?, ?> uploadImage(MultipartFile multipartFile, Map<String, String> options) throws IOException {
        File file = convert(multipartFile);
        Map<?, ?> result = cloudinary.uploader().upload(file, options);
        file.delete();
        return result;
    }

    public Map<?, ?> uploadPdf(MultipartFile multipartFile, Map<String, String> options) throws IOException {
        File file = convert(multipartFile);
        Map<?, ?> result = cloudinary.uploader().uploadLargeRaw(file, options);
        file.delete();
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
