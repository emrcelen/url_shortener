package com.wenthor.urlshortener.repository;

import com.wenthor.urlshortener.model.PermKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PermKeyRepository extends JpaRepository<PermKey, Long> {
    Optional<PermKey> findByKeyAndActivatedFalse(String key);
    List<PermKey> findByRoleName(String roleName);
    List<PermKey> findByActivatedTrue();
    List<PermKey> findByRoleNameAndActivatedTrue(String roleName);
    List<PermKey> findByActivatedFalse();
    List<PermKey> findByRoleNameAndActivatedFalse(String roleName);
}
