package hr.algebra.onlineshop.specification;

import hr.algebra.onlineshop.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {

    private OrderSpecification(){}

    public static Specification<Order> containsUsername(String username){
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("user").get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<Order> purchaseAfter(LocalDateTime from){
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("purchaseDate"), from);
    }

    public static Specification<Order> purchaseBefore(LocalDateTime to){
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("purchaseDate"), to);
    }

}
