package com.freshfood.controller;

import com.freshfood.dto.request.ProductRequestDTO;
import com.freshfood.dto.response.ResponseData;
import com.freshfood.service.CloudinaryService;
import com.freshfood.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/product")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<?> addProduct(@RequestPart("thumbnail") MultipartFile thumbnail ,@RequestPart("images") MultipartFile[] images, @Valid @RequestPart("product") ProductRequestDTO productRequestDTO) throws IOException {
        productService.saveProduct(productRequestDTO, cloudinaryService.uploadImage(thumbnail) ,cloudinaryService.uploadArrImage(images));
        return new ResponseData<>(HttpStatus.OK.value(), "Product added successfully");
    }
    @GetMapping("/list")
    public ResponseData<?> getProducts(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        return new ResponseData<>(HttpStatus.OK.value(), "Get product list  successfully", productService.getProducts(pageNo, pageSize));
    }
    @GetMapping("/{id}")
    public ResponseData<?> getProduct(@PathVariable @Min(1) int id){
        return new ResponseData<>(HttpStatus.OK.value(), "Get product list  successfully", productService.getProductById(id));
    }
    @GetMapping("/list-with-sort-and-search")
    public ResponseData<?> getAllProductsWithSortAndSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,@RequestParam(defaultValue = "5", required = false)  int pageSize,@RequestParam(required = false)  String sort,@RequestParam(required = false) String search) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get list products success", productService.getAllProductsWithSortAndSearch(pageNo, pageSize, sort, search));
    }
    @GetMapping("/list-with-advance-search")
    public ResponseData<?> getAllProductsWithAdvanceSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,@RequestParam(defaultValue = "5", required = false)  int pageSize, @RequestParam(required = false)  String category ,@RequestParam(required = false)  String sort,@RequestParam(required = false) String... search) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get list products success", productService.advanceSearchByCriteria(pageNo, pageSize,category, sort, search));
    }
    @GetMapping("/list-with-search")
    public ResponseData<?> getAllProductsWithSpecification(Pageable pageable, @RequestParam(required = false) String[] product, @RequestParam(required = false) String[] category) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get list products success", productService.advanceSearchWithSpecification(pageable, product, category));
    }
}
