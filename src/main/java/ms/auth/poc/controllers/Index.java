package ms.auth.poc.controllers;


import ms.auth.poc.services.AuthnService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {
    private AuthnService authnService;

    public Index(AuthnService authnService) {
        this.authnService = authnService;
    }

    @RequestMapping(value = "/")
    public String initiate() {

        String idToken = authnService.loadIdToken();

        return idToken;
        // generate CC
        // get ID Token
        // send ID Token to Service
    }
}
