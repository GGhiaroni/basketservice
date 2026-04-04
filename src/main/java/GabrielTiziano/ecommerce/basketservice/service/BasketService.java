package GabrielTiziano.ecommerce.basketservice.service;

import GabrielTiziano.ecommerce.basketservice.entity.Basket;
import GabrielTiziano.ecommerce.basketservice.entity.Product;
import GabrielTiziano.ecommerce.basketservice.entity.Status;
import GabrielTiziano.ecommerce.basketservice.repository.BasketRepository;
import GabrielTiziano.ecommerce.basketservice.request.BasketRequest;
import GabrielTiziano.ecommerce.basketservice.response.PlatziProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductService productService;

    public Optional<Basket> getBasketById(String id) {
        return basketRepository.findById(id);
    }

    public Basket createBasket(BasketRequest basketRequest) {

        basketRepository.findByClientAndStatus(basketRequest.clientId(), Status.OPEN)
                .ifPresent(basket -> {
                    throw new IllegalArgumentException("There is already an open basket for this client");
                });

        List<Product> productList = new ArrayList<>();

        basketRequest.products().forEach(productRequest -> {
            PlatziProductResponse platziProductResponse = productService.getProductById(
                    productRequest.id()
            );

            productList.add(Product.builder()
                    .id(String.valueOf(platziProductResponse.id()))
                    .title(platziProductResponse.title())
                    .price(platziProductResponse.price())
                    .quantity(productRequest.quantity())
                    .build());
        });

        Basket basket = Basket.builder()
                .client(basketRequest.clientId())
                .products(productList)
                .status(Status.OPEN)
                .build();

        basket.calculateTotalPrice();

        return basketRepository.save(basket);
    }

    public Optional<?> updateBasket(String id, BasketRequest basketRequest) {
        Optional<Basket> basketFound = getBasketById(id);

        if (basketFound.isPresent()) {

            List<Product> productList = new ArrayList<>();

            basketRequest.products().forEach(productRequest -> {
                PlatziProductResponse platziProductResponse = productService.getProductById(
                        productRequest.id()
                );

                productList.add(Product.builder()
                        .id(String.valueOf(platziProductResponse.id()))
                        .title(platziProductResponse.title())
                        .price(platziProductResponse.price())
                        .quantity(productRequest.quantity())
                        .build());
            });

            Basket newBasket = basketFound.get();

            newBasket.setClient(basketRequest.clientId());
            newBasket.setProducts(productList);
            newBasket.setStatus(Status.OPEN);

            newBasket.calculateTotalPrice();

            return Optional.of(basketRepository.save(newBasket));

        } else {
            return Optional.empty();
        }
    }
}
