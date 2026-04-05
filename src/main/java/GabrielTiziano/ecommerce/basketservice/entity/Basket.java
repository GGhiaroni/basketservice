package GabrielTiziano.ecommerce.basketservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "basket")
public class Basket {

    @Id
    private String id;

    private Long client;

    private BigDecimal totalPrice;

    private List<Product> products;

    private Status status;

    public void calculateTotalPrice(){
        if(this.products == null || this.products.isEmpty()){
            this.totalPrice = BigDecimal.ZERO;
            return;
        }

        this.totalPrice = products.stream()
                .map(product -> {
                    BigDecimal price = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
                    BigDecimal quantity = product.getQuantity() != null ? BigDecimal.valueOf(product.getQuantity()) : BigDecimal.ZERO;

                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PaymentMethod paymentMethod;
}
