package guru.springframework.spring6webclient.client;

import guru.springframework.spring6webclient.model.BeerDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author john
 * @since 13/11/2024
 */
@Slf4j
@SpringBootTest
class BeerClientImplTest {

    public static final BeerDTO BEER_DTO = BeerDTO.builder().beerName("Stella").beerStyle("Wheet").quantityOnHand(100)
            .upc("2345678")
            .price(12.5).build();
    @Autowired
    BeerClient beerClient;


    @BeforeEach
    void setUp() {
    }

    @Test
    void listBeers() {
        AtomicBoolean state = new AtomicBoolean(false);
        beerClient.listBeers().subscribe(s -> {
            log.info("Response: " + s);
            state.set(true);
        });
        await().untilTrue(state);
    }

    @Test
    void listBeersMap() {
        AtomicBoolean state = new AtomicBoolean(false);
        beerClient.listBeersMap().subscribe(s -> {
            System.out.println(s);
            state.set(true);
        });
        await().untilTrue(state);
    }

    @Test
    void listBeersJson() {
        AtomicBoolean state = new AtomicBoolean(false);
        beerClient.listBeersJson().subscribe(s -> {
            System.out.println(s.toPrettyString());
            state.set(true);
        });
        await().untilTrue(state);
    }

    @Test
    void listBeerDTOs() {
        AtomicBoolean state = new AtomicBoolean(false);
        beerClient.listBeerDTOs().subscribe(s -> {
            System.out.println(s);
            state.set(true);
        });
        await().untilTrue(state);
    }

    @Test
    void listBeersDTOs() {
        AtomicBoolean state = new AtomicBoolean(false);
        beerClient.listBeerDTOs().flatMap(beerDTO -> beerClient.getBeerById(beerDTO.getId()))
                .subscribe(beerDTO -> {
                    System.out.println(beerDTO.getBeerName());
                    state.set(true);
                });

        await().untilTrue(state);
    }

    @Test
    void getBeerByStyle() {
        AtomicBoolean state = new AtomicBoolean(false);
        beerClient.getBeerByStyle("PALE_ALE").subscribe(beerDTO -> {
            System.out.println(beerDTO);
            state.set(true);
        });

        await().untilTrue(state);
    }

    @Test
    void createBeer() {

        AtomicBoolean state = new AtomicBoolean(false);
        beerClient.createBeer(BEER_DTO).subscribe(beerDTO -> {
            System.out.println(beerDTO);
            state.set(true);
        });
        await().untilTrue(state);
    }

    @Test
    void updateBeer() throws InterruptedException {
        final String NEW_NAME = "Updated";
        final Double NEW_PRICE = 21.2;

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDTO savedBeer = saveBeer();
        System.out.println(savedBeer);
        savedBeer.setBeerName(NEW_NAME);
        savedBeer.setPrice(NEW_PRICE);
        beerClient.updateBeer(savedBeer)
                .subscribe(byIdDto -> {
                    System.out.println(byIdDto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void patchBeer() {
        final Integer NEW_QUANTITY = 50;

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDTO savedBeer = saveBeer();
        System.out.println(savedBeer);

        beerClient.patchBeer(BeerDTO.builder().id(savedBeer.getId()).quantityOnHand(50).build())
                .subscribe(byIdDto -> {
                    System.out.println(byIdDto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void deleteBeer() {
        BeerDTO savedBeer = saveBeer();
        System.out.println(savedBeer);

        //beerClient.deleteBeer(savedBeer.getId()).block();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.deleteBeer(savedBeer.getId())
                .doOnSuccess(unused -> atomicBoolean.set(true)).subscribe();
        await().untilTrue(atomicBoolean);
        assertThrows(WebClientResponseException.NotFound.class,
                () -> beerClient.getBeerById(savedBeer.getId()).block());

        assertThrows(WebClientResponseException.NotFound.class,
                () -> beerClient.deleteBeer(savedBeer.getId()).block());

    }

    private BeerDTO saveBeer() {
        return beerClient.createBeer(BEER_DTO).block();
    }
}