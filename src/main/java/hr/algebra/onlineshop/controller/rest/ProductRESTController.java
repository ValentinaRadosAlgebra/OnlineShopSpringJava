package hr.algebra.onlineshop.controller.rest;

import hr.algebra.onlineshop.dto.ProductDTO;
import hr.algebra.onlineshop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductRESTController {

    private final ProductService productService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        Optional<ProductDTO> productOptional = productService.findById(id);

        return productOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        ProductDTO createdProduct = productService.save(dto);
        return ResponseEntity.ok(createdProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
