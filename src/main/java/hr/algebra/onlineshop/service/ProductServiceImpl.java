package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.ProductDTO;
import hr.algebra.onlineshop.model.Category;
import hr.algebra.onlineshop.model.Product;
import hr.algebra.onlineshop.repo.CategoryRepository;
import hr.algebra.onlineshop.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO);
    }

    @Override
    public List<ProductDTO> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {

        Product product = toEntity(productDTO);

        return toDTO(
                productRepository.save(product)
        );
    }

    @Override
    public ProductDTO update(Long id, ProductDTO productDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());

        Category category =
                categoryRepository.findById(productDTO.getCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException("Category not found"));

        product.setCategory(category);

        return toDTO(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {

        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategoryId(product.getCategory().getId());

        return dto;
    }

    private Product toEntity(ProductDTO dto) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);

        return product;
    }
}
