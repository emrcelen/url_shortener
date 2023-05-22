package com.wenthor.urlshortener.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "shorturl", schema ="url_shortener")
data class ShortUrl(
    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val urlId: Long?,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = ForeignKey(name = "FK_url_account_id"))
    val account: Account?,
    @Column(name = "url", nullable = false)
    val url: String,
    @Column(name = "short_url", nullable = false, unique = true)
    var shortUrl: String?,
    @Column(name = "visitor")
    var visitor: Int?,
    @Column(name="url_created_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    val createdDate: LocalDateTime?,
    @Column(name="url_updated_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    var updatedDate: LocalDateTime?,
    @Column(name="url_expiration_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    var expirationDate: LocalDateTime?,
    @Column(name= "is_deleted")
    var deleted: Boolean?,
    @OneToMany(mappedBy = "shortUrl", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val logShortUrls: List<LogShortUrl>?
){
    constructor(url:String,
                shortUrl: String,
                account: Account) : this(
        null,
        account= account,
        url = url,
        shortUrl =  shortUrl,
        visitor = 0,
        createdDate = LocalDateTime.now(),
        null,
        expirationDate = LocalDateTime.now().plusDays(7),
        deleted = false,
        null
    )
}