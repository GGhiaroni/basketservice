package GabrielTiziano.ecommerce.basketservice.controller;

import GabrielTiziano.ecommerce.basketservice.entity.Basket;
import GabrielTiziano.ecommerce.basketservice.request.BasketRequest;
import GabrielTiziano.ecommerce.basketservice.request.PaymentRequest;
import GabrielTiziano.ecommerce.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBasketById(@PathVariable String id) {
        Optional<Basket> basket = basketService.getBasketById(id);
        if(basket.isPresent()){
            return ResponseEntity.ok(basket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Basket> createBasket(@RequestBody BasketRequest basketRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(basketService.createBasket(basketRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBasket(@PathVariable String id, @RequestBody BasketRequest basketRequest){
            return basketService.updateBasket(id, basketRequest)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<?> payBasket(@PathVariable String id, @RequestBody PaymentRequest paymentRequest){
        return basketService.payBasket(id, paymentRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

