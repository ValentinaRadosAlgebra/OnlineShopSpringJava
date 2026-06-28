package hr.algebra.onlineshop.controller.mvc;

import hr.algebra.onlineshop.dto.order.OrderDTO;
import hr.algebra.onlineshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/mvc/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String myOrders(Model model) {

        model.addAttribute("orders", orderService.getUserOrders());

        return "myOrders";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public OrderDTO getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/allorders")
    public String adminOrders(Model model, @RequestParam(required = false) String username, @RequestParam(required = false) String from, @RequestParam(required = false) String to) {

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
}
