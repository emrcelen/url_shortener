package com.wenthor.urlshortener.model

import javax.persistence.*

@Entity
@Table(name = "role", schema = "url_shortener")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    val roleId: Long?,
    @Column(name = "name", unique = true, nullable = false)
    val name: String? = "ROLE_USER",
    @Column(name = "max_urls")
    val maxUrls: Int? = 5,
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val keys: List<PermKey>?
)
