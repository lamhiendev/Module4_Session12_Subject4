package demo.productservice.service.impl;

import demo.productservice.dto.ProductResponseDto;
import demo.productservice.entity.Product;
import demo.productservice.repository.ProductRepository;
import demo.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Override
    @CacheEvict(value = "product", key = "#id")
    public ProductResponseDto updateProduct(Long id, Product newProduct) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Not Found Product With ID " + id));
        product.setProductId(newProduct.getProductId());
        product.setProductName(newProduct.getProductName());
        product.setProductQuantity(newProduct.getProductQuantity());
        productRepository.save(product);
        return new ProductResponseDto(
                product.getProductId(),
                product.getProductName(),
                product.getProductQuantity(),
                product.getProductPrice()
        );
    }

    @Override
    @Cacheable(value = "product",key = "#id")
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Not Found Product With ID " + id));
        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productQuantity(product.getProductQuantity())
                .build();
        return productResponseDto;
    }
}
