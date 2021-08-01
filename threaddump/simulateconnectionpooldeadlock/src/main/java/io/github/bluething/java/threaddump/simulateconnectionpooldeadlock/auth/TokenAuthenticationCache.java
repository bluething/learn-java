package io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenAuthenticationCache {
    private final String TOKEN_PROVIDER_URL = "https://auth.docker.io/token";

    private static LoadingCache<String, String> tokens;

    TokenAuthenticationCache() {
        initTokenCache();
    }

    private void initTokenCache() {
        int TOKEN_EXPIRATION_TIME = 600;
        tokens = CacheBuilder.newBuilder()
                .initialCapacity(1000)
                .expireAfterWrite(TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(@Nonnull String key) throws Exception {
                        String token = fetchNewToken();
                        if (StringUtils.isBlank(token)) {
                            throw new Exception("Couldn't fetch token with key: " + key);
                        }
                        return token;
                    }
                });
    }

    private String fetchNewToken() {
        CloseableHttpResponse response = null;
        try {
            System.out.println("\n["+ Thread.currentThread().getName() +"]" + "| About to request token from auth server: " + TOKEN_PROVIDER_URL);
            BearerAuthenticator authenticator = new BearerAuthenticator();
            HttpGet getMethod = new HttpGet(TOKEN_PROVIDER_URL);
            response = authenticator.executeMethod(getMethod);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                InputStream stream = response.getEntity().getContent();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(stream);
                String token = jsonResponse.get("token").asText();
                String obfuscatedToken = token.substring(token.length() - 7);
                System.out.println("\n["+ Thread.currentThread().getName() +"]" +
                        "| Successfully received token from auth server: " + "..." + obfuscatedToken);
                return jsonResponse.get("token").asText();
            }
        } catch (Exception e) {
            System.out.println("Error occurred while retrieving token from: " + TOKEN_PROVIDER_URL + " " + e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return "";
    }

    String getToken(String key) {
        try {
            System.out.println("\n[" + Thread.currentThread().getName() + "]" +
                    "| Pulling unique token for username: " + key);
            String token = tokens.get(key);
            String obfuscatedToken = token.substring(token.length() - 7);
            System.out.println("\n[" + Thread.currentThread().getName() + "]" +
                    "| About to authenticate using token: " + "..." + obfuscatedToken);
            return token;
        } catch (ExecutionException e) {
            throw new RuntimeException("Could not get token from cache for: " + key, e);
        }
    }
}
