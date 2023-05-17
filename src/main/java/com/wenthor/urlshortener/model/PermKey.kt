package com.wenthor.urlshortener.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "permkey", schema ="url_shortener")
data class PermKey (
    @Id
    @Column(name = "key_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @Column(name = "key_value", unique = true)
    val key: String?,
    @Column(name="key_created_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    val createdDate: LocalDateTime?,
    @Column(name="key_expiration_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    val expirationDate: LocalDateTime?,
    @Column(name="key_activation_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    var activationDate: LocalDateTime?,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", foreignKey = ForeignKey(name = "FK_key_account_id"))
    var account: Account?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = ForeignKey(name = "FK_key_role_id"))
    val role: Role,
    @Column(name= "activated")
    var activated: Boolean?
) {
    constructor(key: String, role: Role) : this (
        null,
        key = key,
        createdDate = LocalDateTime.now(),
        expirationDate = LocalDateTime.now().plusDays(LocalDateTime.now().getMonth().maxLength().toLong()),
        null,
        null,
        role = role,
        activated = false
    )
}