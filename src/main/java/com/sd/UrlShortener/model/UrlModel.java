package com.sd.UrlShortener.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "long_url", nullable = false)
    String longUrl;

    @Column(name = "short_url", nullable = false)
    String shortUrl;

    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
}
