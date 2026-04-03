package GabrielTiziano.ecommerce.basketservice.service;

import GabrielTiziano.ecommerce.basketservice.client.PlatziStoreClient;
import GabrielTiziano.ecommerce.basketservice.response.PlatziProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final PlatziStoreClient platziStoreClient;

    @Cacheable(value = "products")
    public List<PlatziProductResponse> getAllProducts() {
        log.info("Getting all products: ");
        return platziStoreClient.getAllProducts();
    }

    @Cacheable(value="products", key = "#productId")
    public PlatziProductResponse getProductById(Long productId) {
        log.info("Getting product with id: {}", productId);
        return platziStoreClient.getProductById(productId);
    }
}
