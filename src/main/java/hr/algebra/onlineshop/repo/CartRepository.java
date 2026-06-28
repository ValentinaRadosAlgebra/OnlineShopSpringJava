package hr.algebra.onlineshop.repo;

import hr.algebra.onlineshop.model.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Carts, Long> {
    Optional<Carts> findByUserId(Long userId);
}
