package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.media.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OrderController {

//    @PostMapping("testing")
//    public ResponseEntity<WebResponse<String>> soutdelete(@RequestHeader String url) throws IOException {
//        cloudinaryService.deleteFile(url);
//        return ResponseEntity.ok(WebResponse.<String>builder().data("cek").build());
//    }
}
