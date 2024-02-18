package dev.magadiflo.r2dbc.app.handler;

import dev.magadiflo.r2dbc.app.dto.ProductDTO;
import dev.magadiflo.r2dbc.app.entity.Product;
import dev.magadiflo.r2dbc.app.service.IProductService;
import dev.magadiflo.r2dbc.app.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductHandler {

    private final IProductService productService;
    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        Flux<Product> productFlux = this.productService.findAllProducts();
        return ServerResponse.ok().body(productFlux, Product.class);
    }

    public Mono<ServerResponse> getProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return this.productService.findProductById(id)
                .flatMap(productDB -> ServerResponse.ok().bodyValue(productDB));
    }

    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        String path = request.requestPath().value();
        Mono<ProductDTO> productDtoMono = request.bodyToMono(ProductDTO.class)
                .doOnNext(this.objectValidator::validate);
        return productDtoMono
                .flatMap(this.productService::saveProduct)
                .flatMap(productDB -> ServerResponse.created(URI.create(path + "/" + productDB.getId())).bodyValue(productDB));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<ProductDTO> productDtoRequestMono = request.bodyToMono(ProductDTO.class)
                .doOnNext(this.objectValidator::validate);
        return productDtoRequestMono
                .flatMap(productDtoRequest -> this.productService.updateProduct(id, productDtoRequest))
                .flatMap(productDB -> ServerResponse.ok().bodyValue(productDB));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return this.productService.deleteProduct(id)
                .flatMap(wasDeleted -> ServerResponse.noContent().build());
    }


}
