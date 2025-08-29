package com.sd.UrlShortener.repositories;

import com.sd.UrlShortener.model.UrlModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<UrlModel, Long> {
    Optional<UrlModel> findByLongUrl(String longUrl);

    Optional<UrlModel> findByShortUrl(String shortUrl);
}
