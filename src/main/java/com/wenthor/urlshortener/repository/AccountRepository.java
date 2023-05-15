package com.wenthor.urlshortener.repository;

import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.response.AccountInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Optional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);
    @Query(value = "SELECT R.MAX_URLS - S.COUNT AS remaining_urls FROM URL_SHORTENER.ACCOUNT A\n" +
            "INNER JOIN URL_SHORTENER.ROLE R ON R.ROLE_ID = A.ROLE_ID\n" +
            "LEFT JOIN URL_SHORTENER.SHORTURL S ON S.ACCOUNT_ID = A.ACCOUNT_ID AND S.IS_DELETED = false\n" +
            "WHERE A.EMAIL = :email\n" +
            "GROUP BY A.ACCOUNT_ID, A.EMAIL, R.NAME, R.MAX_URLS\n" +
            "ORDER BY A.ACCOUNT_ID", nativeQuery = true)
    int findRemaingUrls(@Param("email") String email);
    @Query(value = "SELECT A.EMAIL, R.NAME AS role_name, R.MAX_URLS - S.COUNT AS remaining_urls FROM URL_SHORTENER.ACCOUNT A\n" +
            "INNER JOIN URL_SHORTENER.ROLE R ON R.ROLE_ID = A.ROLE_ID\n" +
            "LEFT JOIN URL_SHORTENER.SHORTURL S ON S.ACCOUNT_ID = A.ACCOUNT_ID AND S.IS_DELETED = false\n" +
            "WHERE A.EMAIL = :email\n" +
            "GROUP BY A.ACCOUNT_ID, A.EMAIL, R.NAME, R.MAX_URLS\n" +
            "ORDER BY A.ACCOUNT_ID",nativeQuery = true)
    Tuple findAccountInfo(@Param("email") String email);
    @Query(value = "SELECT A.EMAIL, R.NAME AS role_name, R.MAX_URLS - S.COUNT AS remaining_urls FROM URL_SHORTENER.ACCOUNT A\n" +
            "INNER JOIN URL_SHORTENER.ROLE R ON R.ROLE_ID = A.ROLE_ID\n" +
            "LEFT JOIN URL_SHORTENER.SHORTURL S ON S.ACCOUNT_ID = A.ACCOUNT_ID AND S.IS_DELETED = false\n" +
            "WHERE R.NAME = :roleName\n" +
            "GROUP BY A.ACCOUNT_ID, A.EMAIL, R.NAME, R.MAX_URLS\n" +
            "ORDER BY A.ACCOUNT_ID",nativeQuery = true)
    List<Tuple>findAllAccountInfoWithRole(@Param("roleName")String roleName);
    @Query(value = "SELECT A.EMAIL, R.NAME AS role_name, R.MAX_URLS - S.COUNT AS remaining_urls FROM URL_SHORTENER.ACCOUNT A\n" +
            "INNER JOIN URL_SHORTENER.ROLE R ON R.ROLE_ID = A.ROLE_ID\n" +
            "LEFT JOIN URL_SHORTENER.SHORTURL S ON S.ACCOUNT_ID = A.ACCOUNT_ID AND S.IS_DELETED = false\n" +
            "GROUP BY A.ACCOUNT_ID, A.EMAIL, R.NAME, R.MAX_URLS\n" +
            "ORDER BY A.ACCOUNT_ID", nativeQuery = true)
    List<Tuple> findAllAccountInfo();

}
