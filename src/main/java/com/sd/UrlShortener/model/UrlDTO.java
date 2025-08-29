package com.sd.UrlShortener.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlDTO {
    private long id;
    private String longUrl;
    String shortUrl;
    private String expirationDate;
}
