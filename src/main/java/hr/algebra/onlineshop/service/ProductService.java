package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> findAll();
    Optional<ProductDTO> findById(Long id);
    List<ProductDTO> findByCategory(Long categoryId);
    ProductDTO save(ProductDTO productDTO);
    ProductDTO update(Long id, ProductDTO productDTO);
    void delete(Long id);
}
