package com.wenthor.urlshortener.repository;

import com.wenthor.urlshortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl,Long>{
    Optional<ShortUrl> findByAccountEmailAndShortUrlAndDeletedFalse(String email, String shortUrl);
    Optional<ShortUrl> findByShortUrlAndDeletedFalse(String shortUrl);
    Boolean existsByShortUrlAndDeletedFalse(String shortUrl);
    List<ShortUrl> findByAccountEmailAndDeletedFalseOrderByUrlIdAsc(String email);
}
