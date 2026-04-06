package GabrielTiziano.ecommerce.basketservice.client;

import GabrielTiziano.ecommerce.basketservice.exceptions.CustomErrorDecoder;
import GabrielTiziano.ecommerce.basketservice.response.PlatziProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PlatziStoreClient", url = "${basket.client.platzi}", configuration = CustomErrorDecoder.class)
public interface PlatziStoreClient {

    @GetMapping("/products")
    List<PlatziProductResponse> getAllProducts();

    @GetMapping("/products/{id}")
    PlatziProductResponse getProductById(@PathVariable Long id);
}
