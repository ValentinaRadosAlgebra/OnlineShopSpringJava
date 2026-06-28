package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.LoginHistoryDTO;

import java.util.List;

public interface LoginHistoryService {
    void saveLogin(String username,String ipAddress);
    List<LoginHistoryDTO> findAll();
    List<LoginHistoryDTO> findByUsername(String username);
}
