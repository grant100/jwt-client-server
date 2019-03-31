package ms.auth.poc.controllers;

import ms.auth.poc.ServiceNodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {
    private ServiceNodeService serviceNodeService;

    public Index(ServiceNodeService serviceNodeService) {
        this.serviceNodeService = serviceNodeService;
    }

    @RequestMapping(value = "/")
    public String initiate() {
        // generate CC
        // get ID Token
        // send ID Token to Service
        return serviceNodeService.getData();
    }
}
