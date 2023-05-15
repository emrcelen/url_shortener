package com.wenthor.urlshortener.controller;

import com.wenthor.urlshortener.response.rest.RestResponse;
import com.wenthor.urlshortener.service.LogShortUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping(path = "api/shorturl/log")
@Tag(name = "Log Short Url Controller",
        description = "Sistem üzerindeki silinen ve güncellenen url hakkında detaylı bilgi alabileceğiniz controller alanı" )
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class LogShortUrlController {
    private final LogShortUrlService logShortUrlService;

    public LogShortUrlController(LogShortUrlService logShortUrlService) {
        this.logShortUrlService = logShortUrlService;
    }

    @GetMapping(path = "info")
    @Secured(value = {"ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Premium kullanıcıların güncellemiş olduğu urller hakkında gerekli bilgileri bulabileceği endpoint.",
            description = "Kullanıcılar isterse tüm url geçmişlerini listeleyebilir isterlerse parametre olarak verdikleri kısaltılmış url değerine göre sorgulama yaparak dataları listeleyebilir.",
            parameters = {
                    @Parameter(
                            name = "url",
                            description = "Url isimli request param içeriğine geçerli bir değer girildiği takdirde filtreleme yapabilir veya tüm dataları görüntüleyebilirsiniz.",
                            required = false,
                            examples = {
                                    @ExampleObject(
                                            name = "1) All",
                                            summary = "ALL",
                                            description = "Herhangi bir parametre girmediğiniz takdirde kullanıcıya ait tüm kısaltılmış url dataları için oluşan log kayıtlarını görüntüler.",
                                            value = ""
                                    ),
                                    @ExampleObject(
                                            name = "2) Short Url",
                                            summary = "SHORT URL",
                                            description = "Parametreye vereceğiniz güncel short-url'in geçmiş datalarını görebilmek için filtreleme yaparak arama yapabilirsiniz.",
                                            value =  "short-url"
                                    )
                            }
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sistem üzerinde kullanıcıya gösterilecek herhangi bir data mevcutsa kullanıcıya response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcının sistem üzerinde sorguladığı formata ait herhangi bir data mevcut değilse kullancıya response ile iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    public ResponseEntity getShortUrlLogInfo(@RequestParam(name = "url",required = false) String shortUrl,
                                             @RequestHeader HttpHeaders headers,
                                             Locale locale){
        return ResponseEntity.ok().body(RestResponse.Companion.of(
                this.logShortUrlService.getLogShortUrl(shortUrl,headers,locale)));
    }

    @GetMapping(path = "admin/info")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Yöneticilerin database üzerindeki log geçmişlerini görüntüleyeceği endpoint.",
            description = "Yöneticilerin database üzerindeki log kayıtlarını çeşitli filtreleme yapıları ile sorgulayabileceği veya tüm dataları listeleyebilmek için kullanacağı alan.",
            parameters = {
                    @Parameter(
                            name = "url",
                            description = "İlgili request param içeriğine sorgulanacak kısaltılmış url girildiği takdirde data mevcutsa filtrelenerek log kayıtları listelenir.",
                            required = false,
                            examples = {
                                    @ExampleObject(
                                            name = "1) None",
                                            summary = "NONE",
                                            value = ""
                                    ),
                                    @ExampleObject(
                                            name = "2) Short Url",
                                            summary = "SHORT URL",
                                            description = "Sistem üzerindeki kullanılan aktif bir url'in mevcut kısaltılmış url adresini girerek sorgulama yapabilirsiniz.",
                                            value = "short-url"
                                    )
                            }
                    ),
                    @Parameter(
                            name = "account",
                            description = "İlgili request param içeriğine sorgulanacak kullanıcının mail adresi girildiği takdirde data mevcutsa filtrelenerek log kayıtları listelenir.",
                            required = false,
                            examples = {
                                    @ExampleObject(
                                            name = "1) None",
                                            summary = "NONE",
                                            value = ""
                                    ),
                                    @ExampleObject(
                                            name = "2) Account",
                                            summary = "ACCOUNT",
                                            description = "Sistem üzerindeki bir kullanıcının mevcut kısaltılmış url adresini girerek sorgulama yapabilirsiniz.",
                                            value = "admin@admin.com"
                                    )
                            }
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sistem üzerinde kullanıcıya gösterilecek herhangi bir data mevcutsa kullanıcıya response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcının sistem üzerinde sorguladığı formata ait herhangi bir data mevcut değilse kullancıya response ile iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    public ResponseEntity getShortUrlLogAdminInfo(@RequestParam(name = "url", required = false) String shortUrl,
                                                  @RequestParam(name = "account", required = false) String accountName,
                                                  @RequestHeader HttpHeaders headers,
                                                  Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of
                (this.logShortUrlService.getLogShortUrlAdmin(shortUrl,accountName,headers,locale)));
    }
}
