package hr.algebra.onlineshop.repo;


import hr.algebra.onlineshop.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    @Modifying
    @Transactional
    @Query("delete from Product p where p.category.id = :categoryId")
    void deleteByCategoryId(Long categoryId);
}
