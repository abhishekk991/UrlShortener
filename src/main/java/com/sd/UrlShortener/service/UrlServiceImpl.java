package com.sd.UrlShortener.service;

import com.sd.UrlShortener.model.UrlDTO;
import com.sd.UrlShortener.model.UrlModel;
import com.sd.UrlShortener.repositories.UrlRepository;
import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService{

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    UrlRepository urlRepository;

    @Override
    public String getShortUrl() {
        return null;
    }

    @Override
    public UrlDTO createShortUrl(UrlDTO urlDTO) {

        if(StringUtils.isNotBlank(urlDTO.getLongUrl())){
            UrlModel urlModel = modelMapper.map(urlDTO, UrlModel.class);
            String shortUrl = generateShortUrl(urlDTO);
            urlModel.setShortUrl(shortUrl);
            urlModel.setLongUrl(urlDTO.getLongUrl());

            UrlModel savedUrl = urlRepository.save(urlModel);

            return modelMapper.map(savedUrl, UrlDTO.class);
        }
        return null;
    }

    @Override
    public UrlModel getLongUrl(String shortUrl) {
        UrlModel urlModel = chekIfShortUrlAlreadyPresentInDB(shortUrl);
        return urlModel;
    }

    private UrlModel chekIfShortUrlAlreadyPresentInDB(String shortUrl){
        Optional<UrlModel> entity = urlRepository.findByShortUrl(shortUrl);
        return entity.orElse(null);
    }

    private UrlModel chekIfLongUrlAlreadyPresentInDB(String longUrl){
        Optional<UrlModel> entity = urlRepository.findByLongUrl(longUrl);
        return entity.orElse(null);
    }

    private String generateShortUrl(UrlDTO urlDTO) {
        Random random = new Random(1000000);
        long id = urlDTO.getId() + random.nextInt();
        StringBuilder result = new StringBuilder();

        while(id > 0){
            int remainder = (int) (id % 62);
            result.append(BASE62_CHARS.charAt(remainder));
            id = id / 62;
        }

        return result.toString();
    }

}
