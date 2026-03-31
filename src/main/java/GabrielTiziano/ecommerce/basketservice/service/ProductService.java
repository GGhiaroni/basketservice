package GabrielTiziano.ecommerce.basketservice.service;

import GabrielTiziano.ecommerce.basketservice.client.PlatziStoreClient;
import GabrielTiziano.ecommerce.basketservice.response.PlatziProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final PlatziStoreClient platziStoreClient;

    public List<PlatziProductResponse> getAllProducts(){
        return platziStoreClient.getAllProducts();
    }

    public PlatziProductResponse getProductById(Long id){
        return platziStoreClient.getProductById(id);
    }
}
