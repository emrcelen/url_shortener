package com.wenthor.urlshortener.repository;

import com.wenthor.urlshortener.model.LogShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogShortUrlRepository extends JpaRepository<LogShortUrl,Long> {

    List<LogShortUrl> findByAccountEmailAndShortUrlDeletedFalseOrderByUpdatedDateDesc(String email);
    List<LogShortUrl> findByAccountEmailAndShortUrlShortUrlAndShortUrlDeletedFalseOrderByUpdatedDateDesc(String email, String name);
    List<LogShortUrl> findByShortUrlShortUrlAndShortUrlDeletedFalseOrderByUpdatedDateDesc(String name);
    List<LogShortUrl> findByShortUrlDeletedFalseOrderByUpdatedDateDesc();
}
