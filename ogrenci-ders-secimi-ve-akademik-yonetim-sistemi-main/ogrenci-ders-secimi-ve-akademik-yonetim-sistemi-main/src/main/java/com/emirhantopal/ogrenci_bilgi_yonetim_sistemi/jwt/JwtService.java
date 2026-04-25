package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;



@Component
public class JwtService {

        // Değişiklik: Key HS256 (256 bit) algoritmasına uygun geçerli ve güçlü bir base64 formatına getirildi.
        private static final String SECRET_KEY = "NDQ1ZjQzMjkzMDM0NWE0MzRiMzQ2ZDU4NmYzMTY1NTk2OTU0NmU0ZTM2NmQ0MTZmNjczNTRhMzg0MjM4NTc1Yg==";

        public String generateToken(UserDetails userDetails) {
            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))

                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                    .signWith(getKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsFunction.apply(claims);
        }

        public String getUsernameByToken(String token) {
            return exportToken(token, Claims::getSubject);
        }


        public Boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = getUsernameByToken(token);
            Date expiration = exportToken(token, Claims::getExpiration);


            boolean isTokenExpired = expiration.before(new Date());
            return (username.equals(userDetails.getUsername()) && !isTokenExpired);
        }

        private Key getKey() {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }