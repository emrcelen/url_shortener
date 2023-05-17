package com.wenthor.urlshortener.model

import javax.persistence.*

@Entity
@Table(name = "account", schema ="url_shortener")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    val id: Long?,
    @Column(name = "email",unique = true,nullable = false,)
    val email: String?,
    @Column(name="password", nullable = false)
    val password: String?,
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val shortUrls: List<ShortUrl>?,
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val logShortUrls: List<LogShortUrl>?,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    val role: Role?,
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val key: PermKey?
){
    constructor(email: String,
                password: String,
                role: Role) : this(
        null,
        email = email,
        password = password,
        null,
        null,
        role = role,
        null
    )
}
