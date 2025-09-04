package com.sd.UrlShortener.service;

import com.sd.UrlShortener.model.UrlDTO;
import com.sd.UrlShortener.model.UrlModel;
import com.sd.UrlShortener.repositories.UrlRepository;
import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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
            UrlModel urlModel = chekIfLongUrlAlreadyPresentInDB(urlDTO.getLongUrl());
            if(urlModel != null){
                return modelMapper.map(urlModel, UrlDTO.class);
            }

            UrlModel urlModelToSave = modelMapper.map(urlDTO, UrlModel.class);
            String longUrl = urlDTO.getLongUrl();
            String sha256Value = convertToSHA256(longUrl);
            String shortUrl = generateShortUrl(sha256Value);
            urlModelToSave.setShortUrl(shortUrl);
            urlModelToSave.setSha256Value(sha256Value);
            urlModelToSave.setLongUrl(urlDTO.getLongUrl());

            UrlModel savedUrl = urlRepository.save(urlModelToSave);

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

    private String generateShortUrl(String lastId) {
        long numericValue = 0;
        for (int i = 0; i < lastId.length(); i++) {
            numericValue = numericValue * 256 + (int) lastId.charAt(i); // Convert char to ASCII
        }

        if (numericValue == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();

        while (numericValue > 0) {
            int remainder = (int) (numericValue % 62);
            result.append(BASE62_CHARS.charAt(remainder)); // Get corresponding character
            numericValue /= 62; // Integer division
        }

        return result.reverse().toString();
    }

    public String convertToSHA256(String input){
        // Get the SHA-256 message digest instance
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Convert the input string to bytes
        byte[] encodedhash = digest.digest(input.getBytes());

        // Convert the byte array to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            hexString.append(String.format("%02x", b));  // Format as hexadecimal
        }

        return hexString.toString();
    }

}
