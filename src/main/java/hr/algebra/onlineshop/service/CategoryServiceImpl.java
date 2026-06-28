package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.CategoryDTO;
import hr.algebra.onlineshop.model.Category;
import hr.algebra.onlineshop.repo.CategoryRepository;
import hr.algebra.onlineshop.repo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id)
                .map(this::toDTO);
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = toEntity(categoryDTO);
        return toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        category.setName(categoryDTO.getName());

        return toDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productRepository.deleteByCategoryId(id);
        categoryRepository.deleteById(id);
    }

    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }

    private Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }
}
