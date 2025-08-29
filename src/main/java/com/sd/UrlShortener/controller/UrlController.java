package com.sd.UrlShortener.controller;

import com.sd.UrlShortener.model.UrlDTO;
import com.sd.UrlShortener.model.UrlModel;
import com.sd.UrlShortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<UrlDTO> createShortUrl(@RequestBody UrlDTO urlDTO){
        UrlDTO urldto = urlService.createShortUrl(urlDTO);
        return new ResponseEntity<>(urldto, HttpStatus.OK);
    }

    @GetMapping("/getShortUrl/{shortUrl}")
    public ResponseEntity<String> getActualUrl(@PathVariable ("shortUrl") String shortUrl){
        UrlModel urlModel = urlService.getLongUrl(shortUrl);
        String actualUrl = "";
        if(urlModel != null){
            actualUrl = urlModel.getLongUrl();
        }
        else{
            actualUrl = "Invalid short url.";
        }
        return new ResponseEntity<>(actualUrl, HttpStatus.OK);
    }



    @GetMapping("/uss/{shortUrl}")
    public RedirectView redirectToActualUrl(@PathVariable ("shortUrl") String shortUrl){
        UrlModel urlModel = urlService.getLongUrl(shortUrl);
        if(urlModel == null){
            return new RedirectView("/getLongUrl/{shortUrl}");
        }
        return new RedirectView(urlModel.getLongUrl());
    }
}
