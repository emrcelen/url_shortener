package com.wenthor.urlshortener.controller;

import com.wenthor.urlshortener.response.rest.RestResponse;
import com.wenthor.urlshortener.service.PermKeyService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping(path = "api/admin/key")
@Tag(name = "Perm Key Controller",
        description = "Sistem üzerinde kullanıcılara yetki ataması yapabileceğiniz, mevcut yetki anahtarlarını listeleyebileceğiniz controller alanı." )
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class PermKeyController {
    private final PermKeyService permKeyService;

    public PermKeyController(PermKeyService permKeyService) {
        this.permKeyService = permKeyService;
    }

    @PostMapping(path = "generate")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Yönetici parametreye vermiş olduğu role ait yetki anahtarı oluşturmak için kullanacağı endpoint.",
            description = "Yöneticinin 'role' parametresine vermiş olacağı parametre ile birlikte yeni kayıt yapan kullanıcılara yetki ataması sağlayacak çeşitli anahtarlar oluşturabilir.",
            parameters = {
                    @Parameter(
                            name = "role",
                            description = "İstekte bulunan yönetici 'yönetici', 'admin' veya 'premium' olarak vereceği role parametresi sonucunda random bir şekilde yetki anahtarı oluştururlur ve database üzerinde kaydı gerçekleşir.",
                            required = true,
                            examples = {
                                    @ExampleObject(
                                            name = "1) ADMIN",
                                            summary = "ADMIN",
                                            description = "Oluşturulacak yetki anahtarı 'admin' rolüne ait bir şekilde oluşturulur ve database üzerinde kaydı gerçekleşir.'",
                                            value = "admin"
                                    ),
                                    @ExampleObject(
                                            name = "1) PREMIUM",
                                            summary = "PREMIUM",
                                            description = "Oluşturulacak yetki anahtarı 'premium' rolüne ait bir şekilde oluşturulur ve database üzerinde kaydı gerçekleşir.'",
                                            value = "premium"
                                    )
                            }
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Yetki anahtarının kaydı sırasında hata oluşmazsa response ile birlikte verilecek status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    )
            }
    )
    public ResponseEntity savePermKey(@RequestParam(name = "role") String roleName,
                                      @RequestHeader HttpHeaders headers,
                                      Locale locale){
        return ResponseEntity.status(201).body(RestResponse.Companion.of(this.permKeyService.save(roleName,headers,locale)));
    }
    @GetMapping(path = "list")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "İstekte bulunan yönetici çeşitli parametreler ile birlikte database üzerinde kayıtlı yetki anahtarlarını listeler.",
            description = "Yönetici istekte bulunurken aktif edilen, aktif edilmemiş veya tüm anahtarları istediği takdirde rollere göre filtreleyerek listeleme işlemini yapabilir.",
            parameters = {
                    @Parameter(
                            name = "param",
                            description = "İstekte bulunan yönetici 'activated', 'activated-false' veya ilgili parametreyi boş bırakarak anahtarlar içerisinde filtreleme yapabilir",
                            required = false,
                            examples = {
                                    @ExampleObject(
                                            name = "1) NULL",
                                            summary = "NONE",
                                            description = "Herhangi bir filtreleme işlemi yapmadan sorgulamayı gerçekleştirmek isterseniz parametreyi null olarak gönderebilirsiniz.",
                                            value = ""
                                    ),
                                    @ExampleObject(
                                            name = "2) ACTIVATED",
                                            summary = "ACTIVATED",
                                            description = "Database üzerindeki aktifleştirilmiş yetki anahtarlarını filtreleme işlemine yarar.",
                                            value = "activated"
                                    ),
                                    @ExampleObject(
                                            name = "3) ACTIVATED-FALSE",
                                            summary = "ACTIVATED-FALSE",
                                            description = "Database üzerindeki aktifleştirilmemiş yetki anahtarlarını filtreleme işlemine yarar.",
                                            value = "activated-false"
                                    )
                            }
                    ),
                    @Parameter(
                            name = "role",
                            description = "İstekte bulunan yönetici 'yönetici', 'admin', 'premium' veya ilgili parametreyi boş bırakarak anahtarlar içerisinde filtreleme yapabilir",
                            required = false,
                            examples = {
                                    @ExampleObject(
                                            name = "1) NULL",
                                            summary = "NONE",
                                            description = "Herhangi bir filtreleme işlemi yapmadan sorgulamayı gerçekleştirmek isterseniz parametreyi null olarak gönderebilirsiniz.",
                                            value = ""
                                    ),
                                    @ExampleObject(
                                            name = "2) ADMIN",
                                            summary = "ADMIN",
                                            description = "Database üzerinde admin yetkisine sahip anahtarları filtreleme işlemine yarar.",
                                            value = "admin"
                                    ),
                                    @ExampleObject(
                                            name = "3) PREMIUM",
                                            summary = "PREMIUM",
                                            description = "Database üzerinde premium user yetkisine sahip anahtarları filtreleme işlemine yarar.",
                                            value = "premium"
                                    )
                            }
                    )
            },
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
    public ResponseEntity getKeys(@RequestParam(name = "param", required = false) String param,
                                  @RequestParam(name = "role", required = false) String roleName,
                                  @RequestHeader HttpHeaders headers,
                                  Locale locale){
        return ResponseEntity.ok(RestResponse.Companion.of(this.permKeyService.getPermKeys(param,roleName,headers,locale)));
    }
}
