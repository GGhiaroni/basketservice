package GabrielTiziano.ecommerce.basketservice.controller;

import GabrielTiziano.ecommerce.basketservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping
    public ResponseEntity<Void> getAllProducts(){

    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getProductById(@PathVariable Long id){

    }
}
