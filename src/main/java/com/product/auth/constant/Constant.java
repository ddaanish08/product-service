package com.product.auth.constant;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class Constant {
//    public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 Hour
    public static final String BEARER = "Bearer ";
    public static final String ROLES = "roles";

}
