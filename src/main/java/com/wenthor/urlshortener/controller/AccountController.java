package com.wenthor.urlshortener.controller;

import com.wenthor.urlshortener.request.AccountLoginRequest;
import com.wenthor.urlshortener.request.AccountRegisterRequest;
import com.wenthor.urlshortener.response.rest.RestResponse;
import com.wenthor.urlshortener.service.AccountService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@Validated
@RequestMapping(path = "api/user")
@Tag(name = "Account Controller",
        description = "Veritabanı üzerinde kullanıcı kaydı, token alma ve kullanıcı hakkında bilgi alma işlemleri yapabileceğiniz controller alanı.")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "login")
    @Operation(
            summary = "Kullanıcı girişi sonucunda jwt token ve çeşitli kullanıcı bilgilerine erişebilirsiniz.",
            description = "Request body içerisinde istenilen şablonda giriş yaptığınız zaman data türü veritabanı ile eşleşiyor ise " +
                    "kullanıcı email'i ve jwt token olarak geri response alırsınız."
    )
    public ResponseEntity accountLogin(@RequestBody AccountLoginRequest request,
                                       @RequestHeader HttpHeaders headers,
                                       Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of(this.accountService.login(request,headers,locale)));
    }
    @PostMapping(path = "register")
    @Operation(
            summary = "Veritabanı üzerine istediğiniz kullanıcı bilgileri ile kaydın oluşmasını sağlayan endpoint.",
            description = "Kullanıcı Mail Adresi ve Şifresini girdiğiniz zaman isteğin geçerlilik durumuna göre kaydınız db üzerine gerçekleşecektir.",
            parameters = {
                    @Parameter(name = "key",
                            description = "Key isimli request param içeriğine geçerli bir key girişi yaptığınız da rol ataması gerçekleştirebilirsiniz. Boş veya geçersiz bir key girişi olursa default user olarak oluşturur.",
                            required = false,
                            examples = {
                                    @ExampleObject(name = "1) User",
                                            summary = "USER",
                                            description = "Kayıt olurken kullanıcı herhangi bir key veya geçersiz bir key girişi yaptığı zaman kullanıcıya 'USER' rolü verilir.",
                                            value = ""
                                    ),
                                    @ExampleObject(name = "2) Premium User",
                                            summary = "PREMIUM",
                                            description = "Kullanıcı elindeki mevcut 'PREMIUM' key'i database üzerinde mevcut, kullanılmadı olarak gözüküyorsa ve hala aktifse kullanıcıya 'PREMIUM' rolü verilir.",
                                            value = "PRE-AHX43S-422"
                                    ),
                                    @ExampleObject(name = "3) admin",
                                            summary = "ADMIN",
                                            description = "Kullanıcı elindeki mevcut 'ADMIN' key'i database üzerinde mevcut, kullanılmadı olarak gözüküyorsa ve hala aktifse kullanıcıya 'ADMIN' rolü verilir.",
                                            value = "ADM-01VKL-147"
                                    )
                            })
            },
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Kullanıcı kaydı sırasında hata almazsanız karşılaşacağınız response kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    )
            }
    )
    public ResponseEntity accountRegister(@RequestBody  @Valid AccountRegisterRequest request,
                                          @RequestParam(name = "key", required = false) String key,
                                          Locale locale){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.Companion.of(this.accountService.register(request,key,locale)));
    }


    @GetMapping(path = "info")
    @Secured({"ROLE_USER","ROLE_PREMIUM_USER","ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Kullanıcı mevcut token'ı ile birlikte istekte bulunduğunda hesabı hakkında bilgi alabileceği endpoint.",
            description = "JWT ile birlikte istekte bulunduğunuz zaman kullanıcının çeşitli info bilgileri geri dönüş olarak döner.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Kullanıcı isteği sırasında hata almazsa karşılaşacağı response kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    )
            }
    )
    public ResponseEntity accountInfoResponse(@RequestHeader HttpHeaders headers, Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of(this.accountService.accountInfoResponse(headers,locale)));
    }
    @GetMapping(path = "info/list")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Admin rolündeki kullanıcılar istekte bulunduğunda sistemdeki kayıtlı hesaplar hakkında bilgi alabileceği endpoint.",
            description = "İstekte bulunduğunuz zaman sistemdeki kullanıcıların çeşitli info bilgileri  list olarak döner.",
            parameters = {

            @Parameter(name = "role",
                    description = "Request param içeriğine geçerli bir rol tanımlaması yapıldığı zaman ilgili role sahip kullanıcılar filtrelenir. Boş veya geçersiz bir rol girişi olursa tüm kullanıcılar response olarak döner.",
                    required = false,
                    examples = {
                            @ExampleObject(name = "-1) All",
                                    summary = "ALL",
                                    description = "Kullanıcı herhangi bir role değeri girmez veya geçersiz bir role tanımlaması yaparsa default olarak tüm kullanıcıları listeler.",
                                    value = ""
                            ),
                            @ExampleObject(name = "1) User",
                                    summary = "USER",
                                    description = "Kullanıcı role parametresine 'user' veya 'kullanıcı' yazdığı takdirde 'USER' rolüne sahip kullanıcılar listelenir.",
                                    value = "USER"
                            ),
                            @ExampleObject(name = "2) Premium User",
                                    summary = "PREMIUM",
                                    description = "Kullanıcı role parametresine 'premium' veya 'premium-kullanıcı' yazdığı takdirde 'PREMIUM' rolüne sahip kullanıcılar listelenir.",
                                    value = "PREMIUM"
                            ),
                            @ExampleObject(name = "3) admin",
                                    summary = "ADMIN",
                                    description = "Kullanıcı role parametresine 'admin' veya 'yönetici' yazdığı takdirde 'ADMIN' rolüne sahip kullanıcılar listelenir.",
                                    value = "ADMIN"
                            )
                    })
    },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Kullanıcı isteği sırasında hata almazsa karşılaşacağı response kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    )
            }
    )
    public ResponseEntity accountInfoListResponse(@RequestParam(name = "role",required = false)String role,
                                                  @RequestHeader HttpHeaders headers,
                                                  Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of(this.accountService.accountInfoResponseList(role,headers,locale)));
    }
}
