package com.wenthor.urlshortener.response.converter;

import com.wenthor.urlshortener.model.LogShortUrl;
import com.wenthor.urlshortener.response.LogShortUrlInfoResponse;
import com.wenthor.urlshortener.response.LogShortUrlUserInfoResponse;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class LogShortUrlResponseConverter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final LogShortUrlUserInfoResponse convertToUserLog(LogShortUrl logShortUrl){
        return new LogShortUrlUserInfoResponse(
                logShortUrl.getShortUrl().getUrl(),
                logShortUrl.getPreviousUrl(),
                logShortUrl.getUpdateUrl(),
                formatter.format(logShortUrl.getUpdatedDate())
        );
    }
    private final LogShortUrlInfoResponse convertToLog(LogShortUrl logShortUrl){
        return new LogShortUrlInfoResponse(
                logShortUrl.getAccount().getEmail(),
                logShortUrl.getShortUrl().getUrl(),
                logShortUrl.getPreviousUrl(),
                logShortUrl.getUpdateUrl(),
                formatter.format(logShortUrl.getUpdatedDate())
        );
    }

    public final List<LogShortUrlUserInfoResponse> convertToUserLog(List<LogShortUrl> logShortUrls){
        return logShortUrls.stream()
                .map(this::convertToUserLog)
                .collect(Collectors.toList());
    }
    public final List<LogShortUrlInfoResponse> convertToLog(List<LogShortUrl> logShortUrls){
        return logShortUrls.stream()
                .map(this::convertToLog)
                .collect(Collectors.toList());
    }
}
