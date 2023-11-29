package com.finalproject.mooc.service.media;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;
    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String uploadFile(MultipartFile file) throws IOException {

//        cloudinary.uploader().upload(new File("https://upload.wikimedia.org/wikipedia/commons/a/ae/Olympic_flag.jpg"),
//                ObjectUtils.asMap("public_id", "olympic_flag"));

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }


    public void deleteFile(String url) throws IOException {
        String publicId = extractPublicId(url);
        log.info("(Cloudinary) Melakukan delete File, dengan PublicID : {}",publicId);
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//        Map<?, ?> destroyResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

    }

    private String extractPublicId(String url) {
        URI uri = URI.create(url);
        Path path = FileSystems.getDefault().getPath(uri.getPath());

        //ambil nama file tanpa ekstensi
        String fileName = path.getFileName().toString().replaceFirst("[.][^.]+$", "");

        return Optional.of(fileName)
                .filter(s -> !s.isEmpty())
                .orElse(null);
    }
}
