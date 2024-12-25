package com.product.auth.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();

        // Use reflection to set the private field `secretKey`
        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        String secretKey = "my_secret_key_for_testing_purpose_should_be_long";
        secretKeyField.set(jwtService, getSignInKey(secretKey));

        // Use reflection to set the private field `jwtExpiration`
        Field jwtExpirationField = JwtService.class.getDeclaredField("jwtExpiration");
        jwtExpirationField.setAccessible(true);
        // 1 hour
        long jwtExpiration = 1000 * 60 * 60;
        jwtExpirationField.set(jwtService, jwtExpiration); // 1 hour in milliseconds

    }

    @Test
    void testExtractUsername() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("testUser", extractedUsername);
    }

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);


        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testExtractClaim() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testBuildTokenWithExtraClaims() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verify the claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtService.getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testUser", claims.getSubject());
        assertEquals("USER", claims.get("role"));
    }

    @Test
    void testGetSignInKey() {
        Key key = jwtService.getSignInKey();
        assertNotNull(key);
    }

    private String getSignInKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
