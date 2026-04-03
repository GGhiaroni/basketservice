package GabrielTiziano.ecommerce.basketservice.request;

import GabrielTiziano.ecommerce.basketservice.entity.Product;

import java.util.List;

public record ProductRequest(Long id, Integer quantity) {
}
