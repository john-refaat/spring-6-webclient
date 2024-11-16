package guru.springframework.spring6webclient.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author john
 * @since 13/11/2024
 */
@Data
@Builder
public class BeerDTO {
    private String id;
    private String beerName;
    private String beerStyle;
    private Double price;
    private String upc;
    private Integer quantityOnHand;
    private String createdDate;
    private String lastModifiedDate;
}
