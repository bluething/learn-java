package io.github.bluething.java.threaddump.simulateconnectionpooldeadlock.auth;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UsernameCredentialsProvider implements CredentialsProvider {
    private final ConcurrentHashMap<AuthScope, Credentials> credMap;

    public UsernameCredentialsProvider() {
        this.credMap = new ConcurrentHashMap<>();
    }

    @Override
    public void setCredentials(final AuthScope authscope, final Credentials credentials) {
        Args.notNull(authscope, "Authentication scope");
        credMap.put(authscope, credentials);
    }

    @Override
    public Credentials getCredentials(AuthScope authscope) {
        Args.notNull(authscope, "Authentication scope");
        return matchCredentials(this.credMap, authscope);
    }

    private static Credentials matchCredentials(
            final Map<AuthScope, Credentials> map,
            final AuthScope authscope) {
        // see if we get a direct hit
        Credentials creds = map.get(authscope);
        if (creds == null) {
            // Nope.
            // Do a full scan
            int bestMatchFactor = -1;
            AuthScope bestMatch = null;
            for (final AuthScope current : map.keySet()) {
                final int factor = authscope.match(current);
                if (factor > bestMatchFactor) {
                    bestMatchFactor = factor;
                    bestMatch = current;
                }
            }
            if (bestMatch != null) {
                creds = map.get(bestMatch);
            }
        }
        return creds;
    }

    @Override
    public void clear() {

    }
}
