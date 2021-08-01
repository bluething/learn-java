package io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BearerAuthenticator {
    private static CloseableHttpClient client;
    private static ThreadPoolExecutor threadPoolExecutor;

    private String username;

    private BearerAuthenticator(String username) {
        this.username = username;
    }

    public BearerAuthenticator() {}

    public static void main(String[] args) {
        suppressLogger(); // Disable log4j warnings
        BearerAuthenticator bearerAuthenticator = new BearerAuthenticator("Jeff");
        initHttpClientAndExecutors(bearerAuthenticator, Constants.NUM_OF_EXECUTORS);
        int numberOfParallelRequests = 2;
        // Dispatch n parallel requests to a protected resource
        for (int i = 0; i < numberOfParallelRequests; i++) {
            System.out.println("\n[" + Thread.currentThread().getName() + "]" + "| Dispatching request from: "
                    + bearerAuthenticator.getUsername());

            threadPoolExecutor.execute(new AuthenticatorRunnable(bearerAuthenticator));
        }

        // Wait for workers to terminate & bye
        try {
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(120, TimeUnit.SECONDS);
            threadPoolExecutor.shutdownNow();
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not await Executors termination", e);
        } finally {
            System.out.println("\n[" + Thread.currentThread().getName() + "]" + "| Terminating program...");
            closeHttpClient();
        }
    }

    private static void suppressLogger() {
        Logger.getRootLogger().setLevel(Level.OFF);
    }

    private static void closeHttpClient() {
        System.out.println("[" + Thread.currentThread().getName() + "]" + "| Closing HTTP client");
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUsername() {
        return this.username;
    }

    private static void initHttpClientAndExecutors(BearerAuthenticator bearerAuthenticator, int executors) {
        // Initialize a static HTTP client
        initHttpClient(bearerAuthenticator.getUsername());

        // Initialize a thread pool with a fixed size
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(executors);
    }

    /**
     * Initializes a static CloseableHttpClient that can respond to an HTTP Challenge with Bearer authentication
     *
     * @param username a username that represents a principal that is mapped to some secret token value
     */

    private static void initHttpClient(String username) {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        client = clientBuilder.
                setDefaultRequestConfig(getRequestConfig()).
                setDefaultAuthSchemeRegistry(createBearerRegistry()).
                setConnectionManager(configureConnectionManager()).
                setDefaultCredentialsProvider(configureCredentialsProvider(username)).
                build();
    }

    /* Start HTTP configuration methods */
    private static Registry<AuthSchemeProvider> createBearerRegistry() {
        return RegistryBuilder.<AuthSchemeProvider>create()
                .register("Bearer", new BearerSchemeFactory())
                .build();
    }

    private static RequestConfig getRequestConfig() {
        RequestConfig.Builder config = RequestConfig.custom();
        config.setTargetPreferredAuthSchemes(Collections.singletonList("Bearer"));
        return config.build();
    }

    private static HttpClientConnectionManager configureConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(2);
        connectionManager.setDefaultMaxPerRoute(2);
        return connectionManager;
    }

    private static UsernameCredentialsProvider configureCredentialsProvider(String username) {
        // Set username-only based credentials
        UsernameCredentialsProvider usernameCredentialsProvider = new UsernameCredentialsProvider();
        UsernameOnlyCredentials usernameOnlyCredentials = new UsernameOnlyCredentials(username);
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        usernameCredentialsProvider.setCredentials(authScope, usernameOnlyCredentials);
        return usernameCredentialsProvider;
    }
    /* END HTTP configuration methods */

    /**
     * Dispatches a request to a protected resource given a URL
     *
     * @param url the URL of the protected resource
     * @return A response
     */
    private CloseableHttpResponse requestProtectedResource(String url) {
        CloseableHttpResponse response = null;
        try {
            System.out.println("\n[" + Thread.currentThread().getName() + "]" + "| Requesting protected resource from url: " + url);
            HttpGet getMethod = new HttpGet(url);
            response = executeMethod(getMethod);
            return response;
        } catch (Exception e) {
            System.out.println("Error occurred while sending request to: " + url + " " + e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return null;
    }

    public CloseableHttpResponse executeMethod(HttpRequestBase method) throws IOException {
        return client.execute(method);
    }

    /**
     * A username-based authenticator Runnable. In a healthy lifecycle, this runnable may acquire two connections.
     * One for an initial attempt at a protected resource, and if needed, another one for retrieving an authorization
     * token
     */
    private static class AuthenticatorRunnable implements Runnable {

        private BearerAuthenticator bearerAuthenticator;

        AuthenticatorRunnable(BearerAuthenticator bearerAuthenticator) {
            this.bearerAuthenticator = bearerAuthenticator;
        }

        @Override
        public void run() {
            CloseableHttpResponse response = bearerAuthenticator.
                    requestProtectedResource(Constants.PROTECTED_RESOURCE_URL);
            if (statusCodeOk(response)) {
                System.out.println("\nAuthenticated successfully!");
            }
        }

        private boolean statusCodeOk(CloseableHttpResponse response) {
            return response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode();
        }
    }
}
