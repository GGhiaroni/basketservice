package GabrielTiziano.ecommerce.basketservice.request;

import GabrielTiziano.ecommerce.basketservice.entity.PaymentMethod;

public record PaymentRequest(PaymentMethod paymentMethod) {
}
