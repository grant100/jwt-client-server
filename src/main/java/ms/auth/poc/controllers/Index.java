package ms.auth.poc.controllers;

import ms.auth.poc.security.KeyService;
import ms.auth.poc.services.AuthnService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {
    private KeyService keyService;
    private AuthnService authnService;

    public Index(KeyService keyService, AuthnService authnService) {
        this.keyService = keyService;
        this.authnService = authnService;
    }

    @RequestMapping(value = "/")
    public void initiate() {

        String clientCredential = keyService.getClientCredential();
        String idToken = authnService.getIdToken(clientCredential);

        return;
        // generate CC
        // get ID Token
        // send ID Token to Service
    }
}
