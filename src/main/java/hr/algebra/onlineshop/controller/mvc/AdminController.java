package hr.algebra.onlineshop.controller.mvc;

import hr.algebra.onlineshop.dto.CategoryDTO;
import hr.algebra.onlineshop.dto.ProductDTO;
import hr.algebra.onlineshop.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Controller
@RequestMapping("mvc/admin")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final LoginHistoryService loginHistoryService;

    public AdminController (ProductService productService, CategoryService categoryService, OrderService orderService, LoginHistoryService loginHistoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.loginHistoryService = loginHistoryService;
    }

    private static final String FORM_PRODUCT = "formProduct";
    private static final String FORM_CATEGORIES = "formCategory";
    private static final String CATEGORIES = "categories";
    private static final String REDIRECT_ADMIN = "redirect:/mvc/admin";

    @GetMapping
    public String adminHome(Model model) {

        model.addAttribute("products", productService.findAll());
        model.addAttribute(CATEGORIES, categoryService.findAll());

        return "admin";
    }

    @GetMapping("/orders")
    public String adminOrders(
            Model model,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        LocalDateTime begin = null;
        LocalDateTime end = null;

        if(from != null && !from.isBlank()){
            begin = LocalDate.parse(from).atStartOfDay();
        }

        if(to != null && !to.isBlank()){
            end = LocalDate.parse(to).atTime(23,59,59);
        }

        model.addAttribute("orders",
                orderService.searchOrders(username, begin, end));

        model.addAttribute("username", username);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "adminOrders";
    }

    @GetMapping("/logins")
    public String logins(Model model) {

        model.addAttribute("logs", loginHistoryService.findAll());

        return "loginHistory";
    }

    @GetMapping("/products/new")
    public String createProduct(Model model) {

        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute(CATEGORIES, categoryService.findAll());

        return FORM_PRODUCT;
    }

    @PostMapping("/products/new")
    public String saveProduct(@Valid @ModelAttribute ProductDTO dto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(CATEGORIES, categoryService.findAll());
            return FORM_PRODUCT;
        }

        productService.save(dto);
        return REDIRECT_ADMIN;
    }

    @GetMapping("/products/edit")
    public String editProduct(Long id, Model model) {

        ProductDTO product =
                productService.findById(id)
                        .orElseThrow();

        model.addAttribute("productDTO", product);
        model.addAttribute(CATEGORIES, categoryService.findAll());

        return FORM_PRODUCT;
    }

    @PostMapping("/products/edit")
    public String updateProduct(@Valid @ModelAttribute ProductDTO dto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(CATEGORIES, categoryService.findAll());
            return FORM_PRODUCT;
        }

        productService.update(dto.getId(), dto);
        return REDIRECT_ADMIN;
    }

    @PostMapping("/products/delete")
    public String deleteProduct(Long id) {

        productService.delete(id);

        return REDIRECT_ADMIN;
    }

    @GetMapping("/categories/new")
    public String createCategory(Model model) {

        model.addAttribute("categoryDTO", new CategoryDTO());

        return FORM_CATEGORIES;
    }

    @PostMapping("/categories/new")
    public String saveCategory(@Valid @ModelAttribute CategoryDTO dto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return FORM_CATEGORIES;
        }
        categoryService.save(dto);

        return REDIRECT_ADMIN;
    }

    @GetMapping("/categories/edit")
    public String editCategory(Long id, Model model) {

        CategoryDTO category =
                categoryService.findById(id)
                        .orElseThrow();

        model.addAttribute("categoryDTO", category);

        return FORM_CATEGORIES;
    }

    @PostMapping("/categories/edit")
    public String updateCategory(   @Valid @ModelAttribute CategoryDTO dto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return FORM_CATEGORIES;
        }

        categoryService.update(dto.getId(), dto);
        return REDIRECT_ADMIN;
    }

    @PostMapping("/categories/delete")
    public String deleteCategory(Long id) {

        categoryService.delete(id);

        return REDIRECT_ADMIN;
    }


}
