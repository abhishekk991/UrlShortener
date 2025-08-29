package com.sd.UrlShortener.service;

import com.sd.UrlShortener.model.UrlDTO;
import com.sd.UrlShortener.model.UrlModel;
import org.springframework.stereotype.Service;


public interface UrlService {

    String getShortUrl();

    UrlDTO createShortUrl(UrlDTO urlDTO);
    UrlModel getLongUrl(String shortUrl);
}
