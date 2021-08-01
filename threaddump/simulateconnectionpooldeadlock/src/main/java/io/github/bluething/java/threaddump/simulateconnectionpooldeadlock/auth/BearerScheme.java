package io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.auth.RFC2617Scheme;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;

public class BearerScheme extends RFC2617Scheme {
    private static TokenAuthenticationCache tokenAuthenticationCache;

    BearerScheme() {
        tokenAuthenticationCache = new TokenAuthenticationCache();
    }

    @Override
    public String getSchemeName() {
        return "Bearer";
    }

    @Override
    public boolean isConnectionBased() {
        return false;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
        return authenticate(credentials, request, new BasicHttpContext());
    }

    @Override
    public Header authenticate(Credentials credentials, HttpRequest httpRequest, HttpContext context)
            throws AuthenticationException {
        // Authenticate using a bearer token mapped to the username
        System.out.println("\n["+ Thread.currentThread().getName() +"]" + "| Received an HTTP Challenge response." +
                " About to authenticate using Bearer token...");
        String key = credentials.getUserPrincipal().getName();
        String token = tokenAuthenticationCache.getToken(key);
        final CharArrayBuffer buffer = new CharArrayBuffer(32);
        buffer.append(AUTH.WWW_AUTH_RESP);
        buffer.append(": Bearer ");
        buffer.append(token);
        return new BufferedHeader(buffer);
    }
}
