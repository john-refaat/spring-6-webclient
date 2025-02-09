package guru.springframework.spring6webclient.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.webflux.LogbookExchangeFilterFunction;

/**
 * @author john
 * @since 13/11/2024
 */
@Configuration
public class WebClientConfig implements WebClientCustomizer {
    private final String rootUrl;
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;

    public WebClientConfig(@Value("${webclient.rooturl}") String rootUrl, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        this.rootUrl = rootUrl;
        this.authorizedClientManager = authorizedClientManager;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        LogbookExchangeFilterFunction logbookFilter = new LogbookExchangeFilterFunction(Logbook.builder().build());
        oauth2.setDefaultClientRegistrationId("springauth");
        webClientBuilder.filter(oauth2).filter(logbookFilter).baseUrl(rootUrl);
    }
}
