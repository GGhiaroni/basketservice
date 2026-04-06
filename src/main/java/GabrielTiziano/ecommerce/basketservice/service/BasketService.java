package GabrielTiziano.ecommerce.basketservice.service;

import GabrielTiziano.ecommerce.basketservice.entity.Basket;
import GabrielTiziano.ecommerce.basketservice.entity.Product;
import GabrielTiziano.ecommerce.basketservice.entity.Status;
import GabrielTiziano.ecommerce.basketservice.exceptions.BusinessException;
import GabrielTiziano.ecommerce.basketservice.exceptions.DataNotFoundException;
import GabrielTiziano.ecommerce.basketservice.repository.BasketRepository;
import GabrielTiziano.ecommerce.basketservice.request.BasketRequest;
import GabrielTiziano.ecommerce.basketservice.request.PaymentRequest;
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

    public Basket getBasketById(String id) {
        return basketRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(("Carrinho de id " + id + " não encontrado.")));
    }

    public Basket createBasket(BasketRequest basketRequest) {

        basketRepository.findByClientAndStatus(basketRequest.clientId(), Status.OPEN)
                .ifPresent(basket -> {
                    throw new BusinessException("Já existe um carrinho aberto para esse cliente.");
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

    public Basket updateBasket(String id, BasketRequest basketRequest) {
        Basket newBasket = getBasketById(id);

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

        newBasket.setClient(basketRequest.clientId());
        newBasket.setProducts(productList);
        newBasket.setStatus(Status.OPEN);

        newBasket.calculateTotalPrice();

        return basketRepository.save(newBasket);
    }

    public Basket payBasket(String id, PaymentRequest paymentRequest) {
        Basket basketUpdated = getBasketById(id);

        basketUpdated.setPaymentMethod(paymentRequest.paymentMethod());
        basketUpdated.setStatus(Status.SOLD);

        return basketRepository.save(basketUpdated);
    }

    public void deleteBasket(String id) {
        Basket basketFound = getBasketById(id);
        basketRepository.delete(basketFound);
    }
}
