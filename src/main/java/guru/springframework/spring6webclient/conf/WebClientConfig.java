package guru.springframework.spring6webclient.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author john
 * @since 13/11/2024
 */
@Configuration
public class WebClientConfig implements WebClientCustomizer {
    private final String rootUrl;

    public WebClientConfig(@Value("${webclient.rooturl}") String rootUrl) {
        this.rootUrl = rootUrl;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.baseUrl(rootUrl);
    }
}
