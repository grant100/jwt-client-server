package ms.auth.poc;

import ms.auth.poc.services.AuthnService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceNodeService {
    private AuthnService authnService;
    private ServiceNodeProperties serviceNodeProperties;


    public ServiceNodeService(AuthnService authnService,ServiceNodeProperties serviceNodeProperties) {
        this.authnService = authnService;
        this.serviceNodeProperties = serviceNodeProperties;
    }

    public String getData(){
        String token = authnService.loadIdToken();
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = template.exchange(serviceNodeProperties.getUrl(), HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

}
