package guru.springframework.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.springframework.spring6webclient.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author john
 * @since 13/11/2024
 */
public interface BeerClient {
    Flux<String> listBeers();

    Flux<Map> listBeersMap();

    Flux<JsonNode> listBeersJson();

    Flux<BeerDTO> listBeerDTOs();

    Mono<BeerDTO> getBeerById(String id);

    Flux<BeerDTO> getBeerByStyle(String beerStyle);

    Mono<BeerDTO> createBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(BeerDTO beerDTO);

    Mono<Void> deleteBeer(String id);
}
