package io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth;

import org.apache.http.auth.BasicUserPrincipal;
import org.apache.http.auth.Credentials;

import java.io.Serializable;
import java.security.Principal;

public class UsernameOnlyCredentials implements Credentials, Serializable {
    private final BasicUserPrincipal principal;

    UsernameOnlyCredentials(String username) {
        this.principal = new BasicUserPrincipal(username);
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
