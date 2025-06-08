package com.boardgameenjoyers.boardgamebuddy.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
public class SecurityConstants {
    public static final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
