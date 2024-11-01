package com.freshfood.service;

import com.freshfood.dto.request.UserRequestDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetails();
    long saveUser(UserRequestDTO userRequestDTO);
    void updateUser(UserRequestDTO userRequestDTO);
    void deleteUser(Long id);
    User getUserById(Integer id);
    PageResponse<?> getAllUserWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy);
    PageResponse<?> advanceSearchWithSpecification(Pageable pageable, String[] users, String[] address);
}
