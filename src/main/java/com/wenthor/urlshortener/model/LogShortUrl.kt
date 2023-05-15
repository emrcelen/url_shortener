package com.wenthor.urlshortener.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="shorturl_audit_log", schema = "url_shortener")
data class LogShortUrl(
    @Id
    @Column(name = "url_audit_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = ForeignKey(name = "FK_log_account_id"))
    val account: Account,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "short_url_id", nullable = false, foreignKey = ForeignKey(name = "FK_log_short_url_id"))
    val shortUrl: ShortUrl,
    @Column(name = "previous_url", nullable = false)
    val previousUrl: String,
    @Column(name = "update_url", nullable = false)
    val updateUrl: String,
    @Column(name="url_updated_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    var updatedDate: LocalDateTime? = LocalDateTime.now()
)
