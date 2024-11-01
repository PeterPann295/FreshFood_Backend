package com.freshfood.controller;

import com.freshfood.dto.request.ParentCategoryRequestDTO;
import com.freshfood.dto.response.ResponseData;
import com.freshfood.model.ParentCategory;
import com.freshfood.service.CloudinaryService;
import com.freshfood.service.ParentCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/parent-category")
@RequiredArgsConstructor
@Validated
public class ParentCategoryController {

    private final ParentCategoryService parentCategoryService;
    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<?> addParentCategory(
            @RequestPart("file") MultipartFile file, @RequestPart("parentCategory") String name) throws IOException {
        long id = parentCategoryService.saveParentCategory(new ParentCategoryRequestDTO(name), cloudinaryService.uploadImage(file));
        return new ResponseData<>(HttpStatus.CREATED.value(), "Parent category added successfully", id );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<?> updateParentCategory(@PathVariable int id,@RequestPart("file") MultipartFile file, @RequestPart("parentCategory") ParentCategoryRequestDTO requestDTO) throws IOException {
        parentCategoryService.updateParentCategory(id, requestDTO, cloudinaryService.uploadImage(file));
        return new ResponseData<>(HttpStatus.CREATED.value(), "Parent category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deleteParentCategory(@PathVariable int id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Parent category deleted successfully");
    }

    @GetMapping("/list")
    public ResponseData<?> getAllParentCategory(@RequestParam(defaultValue = "0") int PageNo, @RequestParam(defaultValue = "10") int PageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), "get list parent category successfully" ,parentCategoryService.getParentCategory(PageNo, PageSize));
    }

}
