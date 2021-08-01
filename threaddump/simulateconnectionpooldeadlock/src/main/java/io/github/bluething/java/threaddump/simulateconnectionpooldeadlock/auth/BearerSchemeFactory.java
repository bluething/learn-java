package io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.protocol.HttpContext;

public class BearerSchemeFactory implements AuthSchemeProvider {
    @Override
    public AuthScheme create(HttpContext context) {
        return new BearerScheme();
    }
}
