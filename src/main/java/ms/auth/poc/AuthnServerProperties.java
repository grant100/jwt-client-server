package ms.auth.poc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthnServerProperties {
    @Value("${authn.server.url.base}")
    private String baseUrl;

    @Value("${authn.server.url.token}")
    private String tokenUrl;

    @Value("${authn.server.url.authenticate}")
    private String authenticateUrl;

    @Value("${authn.server.audience}")
    private String audience;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getAuthenticateUrl() {
        return authenticateUrl;
    }

    public String getAudience() {
        return audience;
    }
}
