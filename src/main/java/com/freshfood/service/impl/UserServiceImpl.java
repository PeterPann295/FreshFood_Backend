package com.freshfood.service.impl;

import com.freshfood.dto.request.UserRequestDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.model.Cart;
import com.freshfood.model.User;
import com.freshfood.repository.CartRepository;
import com.freshfood.repository.UserRepository;
import com.freshfood.repository.search.SearchUserRepository;
import com.freshfood.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final SearchUserRepository searchUserRepository;
    @Override
    public UserDetailsService userDetails() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public long saveUser(UserRequestDTO userRequestDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .password(encoder.encode(userRequestDTO.getPassword()))
                .email(userRequestDTO.getEmail())
                .phone(userRequestDTO.getPhone())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .status(userRequestDTO.getStatus())
                .build();
        userRepository.save(user).getId();
        cartRepository.save(new Cart(user, new HashSet<>()));
        return user.getId();
    }

    @Override
    public void updateUser(UserRequestDTO userRequestDTO) {

    }

    @Override
    public void deleteUser(Long id) {

    }


    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public PageResponse<?> getAllUserWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        return searchUserRepository.getAllUserWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy);
    }

    @Override
    public PageResponse<?> advanceSearchWithSpecification(Pageable pageable, String[] users, String[] address) {
        return searchUserRepository.advanceSearchWithSpecification(pageable, users, address);
    }
}
