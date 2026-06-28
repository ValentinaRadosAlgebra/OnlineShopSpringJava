package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.model.User;

public interface UserService {
    User findByUsername(String username);
}
