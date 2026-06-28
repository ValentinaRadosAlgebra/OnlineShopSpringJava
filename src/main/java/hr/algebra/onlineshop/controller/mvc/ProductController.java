package hr.algebra.onlineshop.controller.mvc;

import hr.algebra.onlineshop.dto.ProductDTO;
import hr.algebra.onlineshop.service.CategoryService;
import hr.algebra.onlineshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("mvc/home")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String home(Model model) {

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("products", productService.findAll());

        return "home";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {

        ProductDTO product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        model.addAttribute("product", product);

        return "product";
    }

    @GetMapping("/category/{id}")
    public String productsByCategory(@PathVariable Long id, Model model) {

        model.addAttribute("products", productService.findByCategory(id));
        model.addAttribute("categories", categoryService.findAll());

        return "home";
    }
}
