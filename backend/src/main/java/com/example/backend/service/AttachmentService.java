package com.example.backend.service;

import com.example.backend.entity.Attachment;
import com.example.backend.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    public HttpEntity<?> getFile(Long id) throws IOException {
        Optional<Attachment> attachment = attachmentRepository.findById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.get().getContentType()))
                .body(Files.readAllBytes(Paths.get(attachment.get().getPath())));
    }

}
