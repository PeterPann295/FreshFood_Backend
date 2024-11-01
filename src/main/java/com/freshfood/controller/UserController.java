package com.freshfood.controller;

import com.freshfood.dto.request.UserRequestDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.dto.response.ResponseData;
import com.freshfood.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/")
    public ResponseData<?> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        userService.saveUser(userRequestDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully");
    }

    @GetMapping("/{id}")
    public ResponseData<?> getUserById(@PathVariable int id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get user success",userService.getUserById(id));
    }
    @GetMapping("/list-with-sort-and-search")
    public ResponseData<?> getAllUserWithSortByColumnAndSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,@RequestParam(defaultValue = "10", required = false) int pageSize,@RequestParam(required = false)  String search,@RequestParam(required = false) String sortBy) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get list user success", userService.getAllUserWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy));
    }
    @GetMapping("/list-advance-search-specification")
    public ResponseData<?> advanceSearchWithSpecification(Pageable pageable,@RequestParam(required = false) String[] users,@RequestParam(required = false) String[] address) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get list user success", userService.advanceSearchWithSpecification(pageable, users, address));
    }
}
