package com.wenthor.urlshortener.controller;

import com.wenthor.urlshortener.request.PremiumShortUrlRequest;
import com.wenthor.urlshortener.request.UpdateShortUrlRequest;
import com.wenthor.urlshortener.request.UserShortUrlRequest;
import com.wenthor.urlshortener.response.rest.RestResponse;
import com.wenthor.urlshortener.service.ShortUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Locale;

@RestController
@Validated
@RequestMapping(path = "api/shorturl")
@Tag(name = "Short Url Controller",
        description = "Kullanıcıların url kısaltabileceği, kısalttığı urller hakkında bilgi alacağı, güncelleyeceği ve sileceği controller kısmı.")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping(path = "urls")
    @Secured({"ROLE_USER","ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "İstekte bulunan kullanıcının sistem üzerinde aktif olan kısa url adreslerinin listesi iletilir.",
            description = "Request ile birlikte gelen Header içeriğindeki JWT token içeriği parse edilir ve kullanıcı teyit edilir daha sonra ilgili kullanıcının database üzerindeki aktif shorturl dataları  response olarak iletilir.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanıcının database üzerinde aktif olarak gösterilecek datası mevcutsa data ile birlikte döndürülecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcının database üzerinde gösterilecek herhangi bir datası bulunmuyorsa kullanıcıya hata mesajı ile birlikte döndürülecek status kodu.",
                            content =  {
                                    @Content(
                                            mediaType = "application/json",
                                            array =  @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    public ResponseEntity getByUserShortUrlList(@RequestHeader HttpHeaders headers,
                                                Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of(this.shortUrlService.findByUserAccountEmail(headers,locale)));
    }
    @PostMapping(path = "create")
    @Secured({"ROLE_USER","ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Kullanıcı sistem üzerinde istediği bir url'i kısaltmak için istekte bulunacağı endpoint.",
            description = "Sistem üzerindeki default tanımlanan 3 rolünde kullanabileceği basic create metodu ile birlikte url kısaltılır",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Kullanıcının isteği doğrultusunda yapılan kontroller geçerli ise ve database üzerinde kayıt gerçekleşmişse kullanıcıya oluşturulan response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Kullanıcının isteği doğrultusunda oluşturulacak short-url database üzerinde mevcutsa kullanıcıya 'BadRequest' status kodu ile birlikte response döner.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "418",
                            description = "Kullanıcının mevcut rolü gözetilerek elde edilen url kısaltma hakkı bulunmuyorsa kullanıcıya response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )

            }
    )
    public ResponseEntity saveUserShortUrl(@RequestBody @Valid UserShortUrlRequest request,
                                           @RequestHeader HttpHeaders headers,
                                           Locale locale){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.shortUrlService.saveUserShortUrl(request,headers,locale));
    }
    @DeleteMapping(path = "delete/{url}")
    @Secured({"ROLE_USER","ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Kullanıcı kendisine ait mevcut kısaltmış url datalarını silmek istediğinde istek atacağı endpoint.",
            description = "Path variable üzerinden gelen data database üzerinde mevcutsa ve ilgili data istekte bulunan kullanıcıya aitse database üzerinden ilgili data silinir.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "İlgili data kontrolleri gerçekleştirildikten sonra database üzerinden ilgili data siliniyorsa kullanıcıya silinen data ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Silinmeye çalışılan short url database üzerinde mevcut değilse kullanıcıya ilgili hata mesajı ile iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    public ResponseEntity deleteShortUrl(@PathVariable(name = "url") String url,
                                         @RequestHeader HttpHeaders headers,
                                         Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of(this.shortUrlService.deleteUserShortUrl(url,headers,locale)));
    }
    @GetMapping(path = "premium/urls")
    @Secured({"ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "İstekte bulunan 'premium_user' rolüne sahip kullanıcının sistem üzerinde aktif olan kısa url adreslerinin listesi iletilir.",
            description = "İstekte bulunan kullanıcı teyit edilirerek kimliği belirlenir ve database üzerindeki aktif shorturl dataları  response olarak iletilir.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanıcının database üzerinde aktif olarak gösterilecek datası mevcutsa data ile birlikte döndürülecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "İstekte bulunan kullanıcının request'i geçersiz olduğu takdirde ilgili hata mesajı ile birlikte iletilecek status kodu",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcının database üzerinde gösterilecek herhangi bir datası bulunmuyorsa kullanıcıya hata mesajı ile birlikte döndürülecek status kodu.",
                            content =  {
                                    @Content(
                                            mediaType = "application/json",
                                            array =  @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    public ResponseEntity getByPremiumShortUrlList(@RequestHeader HttpHeaders headers,
                                                   Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of(this.shortUrlService.findByPremiumAccountEmail(headers,locale)));
    }
    @PostMapping(path = "premium/create")
    @Secured({"ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Premium kullanıcının sistem üzerinde istediği bir url'i kısaltmak için istekte bulunacağı endpoint.",
            description = "Sistem üzerinde Premium User rolüne sahip kullanıcıların kullanabileceği özelleştirilmiş create metodu ile birlikte url kısaltılır.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Kullanıcının isteği doğrultusunda yapılan kontroller geçerli ise ve database üzerinde kayıt gerçekleşmişse kullanıcıya oluşturulan response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Kullanıcının isteği doğrultusunda oluşturulacak short-url database üzerinde mevcutsa kullanıcıya 'BadRequest' status kodu ile birlikte response döner.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "418",
                            description = "Kullanıcının mevcut rolü gözetilerek elde edilen url kısaltma hakkı bulunmuyorsa kullanıcıya response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )

            }
    )
    public ResponseEntity savePremiumShortUrl(@RequestBody @Valid PremiumShortUrlRequest request,
                                              @RequestHeader HttpHeaders headers,
                                              Locale locale){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.shortUrlService.savePremiumUserShortUrl(request,headers,locale));
    }
    @PostMapping(path = "premium/create/{url}")
    @Secured({"ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Premium kullanıcının sistem üzerinde istediği bir url'i random kısaltmak için istekte bulunacağı endpoint.",
            description = "Sistem üzerinde Premium User rolüne sahip kullanıcıların kullanabileceği özelleştirilmiş create metodu ile birlikte url kısaltılır.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Kullanıcının isteği doğrultusunda yapılan kontroller geçerli ise ve database üzerinde kayıt gerçekleşmişse kullanıcıya oluşturulan response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Kullanıcının isteği doğrultusunda oluşturulacak short-url database üzerinde mevcutsa kullanıcıya 'BadRequest' status kodu ile birlikte response döner.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "418",
                            description = "Kullanıcının mevcut rolü gözetilerek elde edilen url kısaltma hakkı bulunmuyorsa kullanıcıya response ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )

            }
    )
    public ResponseEntity autoSavePremiumShortUrl(@PathVariable(name = "url")  @Pattern(regexp = "^[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*?)?$") String url,
                                                  @RequestParam(name = "security", required = false) Boolean security,
                                                  @RequestHeader HttpHeaders headers,
                                                  Locale locale){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.Companion.of(this.shortUrlService.savePremiumUserAutoShortUrl(url, security,headers,locale)));
    }
    @PutMapping(path = "premium/update")
    @Secured({"ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Premium kullanıcının mevcut kısaltılmış url adreslerini değiştirmek istedikleri zaman kullanabilecekleri endpoint.",
            description = "Kullanıcının mevcut short url adreslerinin süresini uzatarak güncellemek istediği zaman istekte bulunarak gerekli işlemlerin yapılmasını sağlayabilir.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Kullanııcının isteği gerçekleştirildiğinde güncellenen data ile birlikte kullanıcıya iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Kullanıcının güncellemek istediği kısa url isteği sonucunda ilgili data başka bir url için tahsis edildiyse kullanıcıya hata mesajı ile birlikte iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kullanıcının güncellemek istediği kısa url mevcut değilse hata mesajı ile kullanıcıya iletilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class))
                                    )
                            }
                    )
            }
    )
    public ResponseEntity updatePremiumShortUrl(@RequestBody UpdateShortUrlRequest request,
                                                @RequestHeader HttpHeaders headers,
                                                Locale locale){
       return ResponseEntity.ok(RestResponse.Companion.of(this.shortUrlService.updateUserShortUrl(request,headers,locale)));
    }

}
