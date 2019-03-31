package ms.auth.poc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceNodeProperties {
    @Value("${service.node.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
