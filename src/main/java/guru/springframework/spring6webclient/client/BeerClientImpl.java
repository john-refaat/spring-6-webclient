package guru.springframework.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.springframework.spring6webclient.model.BeerDTO;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author john
 * @since 13/11/2024
 */
@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private WebClient webClient;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    @Override
    public Flux<String> listBeers() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> listBeersMap() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> listBeersJson() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> listBeerDTOs() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getBeerById(String id) {
                return webClient.get().uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(id))
                        .retrieve()
                        //.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> getBeerByStyle(String beerStyle) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path(BEER_PATH)
                .queryParam("style", beerStyle).build()).retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
        return webClient.post().uri(uriBuilder -> uriBuilder.path(BEER_PATH).build())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve().toEntity(BeerDTO.class)
                .map(HttpEntity::getBody);
        /*.map(beerDTOResponseEntity -> beerDTOResponseEntity.getHeaders().get("Location").getFirst())
                .flatMap(location -> webClient.get().uri(location).retrieve().bodyToMono(BeerDTO.class));*/

    }

    @Override
    public Mono<BeerDTO> updateBeer(BeerDTO beerDTO) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDTO.getId()))
                .bodyValue(beerDTO)
                .retrieve()
                .toEntity(BeerDTO.class)
                .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }


    @Override
    public Mono<BeerDTO> patchBeer(BeerDTO beerDTO) {
        return webClient.patch()
               .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDTO.getId()))
               .bodyValue(beerDTO)
               .retrieve()
               .toBodilessEntity()
               .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }

    @Override
    public Mono<Void> deleteBeer(String id) {
        return webClient.delete().uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(id)).retrieve()
                .toBodilessEntity().then();
//               .bodyToMono(Void.class);
    }
}