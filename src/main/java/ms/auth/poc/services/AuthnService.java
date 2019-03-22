package ms.auth.poc.services;

import ms.auth.poc.AuthnServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthnService {

    private AuthnServerProperties authnServerProperties;

    public AuthnService(AuthnServerProperties authnServerProperties) {
        this.authnServerProperties = authnServerProperties;
    }

    public String getIdToken(String clientCredential) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + clientCredential);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = template.exchange(authnServerProperties.getTokenUrl(), HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
