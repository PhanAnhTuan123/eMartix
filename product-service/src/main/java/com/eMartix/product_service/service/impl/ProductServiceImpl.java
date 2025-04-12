package com.eMartix.product_service.service.impl;


import com.eMartix.commons.advice.ResourceNotFoundException;

import com.eMartix.product_service.dto.mapper.*;
import com.eMartix.product_service.dto.message.CategoryResponseDto;
import com.eMartix.product_service.dto.message.ProductDetailResponseDto;
import com.eMartix.product_service.dto.model.ProductDto;
import com.eMartix.product_service.dto.model.ProductOptionDto;
import com.eMartix.product_service.dto.response.ObjectResponse;
import com.eMartix.product_service.dto.response.ProductWithOptionForCartDto;
import com.eMartix.product_service.entity.Category;
import com.eMartix.product_service.entity.Product;
import com.eMartix.product_service.entity.ProductOption;
import com.eMartix.product_service.entity.ProductSpecification;

import com.eMartix.product_service.repository.CategoryRepository;
import com.eMartix.product_service.repository.ProductOptionRepository;
import com.eMartix.product_service.repository.ProductRepository;
import com.eMartix.product_service.repository.ProductSpecificationRepository;
import com.eMartix.product_service.service.ProductService;
import com.eMartix.product_service.utils.SlugConvert;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    //Repositories
    private ProductRepository productRepository;
    private ProductOptionRepository optionRepository;
    private ProductSpecificationRepository specificationRepository;
    private CategoryRepository categoryRepository;

    //Mappers
    private ProductMapper productMapper;
    private CategoryMapper categoryMapper;
    private OptionMapper optionMapper;
    private SpecificationMapper specificationMapper;
    private ProductWithOptionForCartMapper productWithOptionMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product newProduct = productMapper.mapToEntity(productDto);
        if(!newProduct.getCategoryUrl().equals("")){
            String categoryUrl = SlugConvert.convert(newProduct.getCategoryUrl());
            Category category = this.categoryRepository.findByUrlKey(categoryUrl);
            if(category == null){
                throw new ResourceNotFoundException("Category", "urlKey", categoryUrl);
            }
            newProduct.setCategory(category);
        }
        String productSlug = SlugConvert.convert(productDto.getName());
        newProduct.setProductSlug(productSlug);

        // Gán các option vào product
        List<ProductOption> options = newProduct.getOptions();
        if(options == null)  {
            options = new ArrayList<>();
        }
        for(ProductOption option : options){
            newProduct.addOption(option);
        }


        // Gán các specification vào product
        Set<ProductSpecification> specifications = newProduct.getSpecifications();
        if(specifications == null){
            specifications = new HashSet<>();
        }
        for(ProductSpecification specification : specifications){
            newProduct.addSpecification(specification);
        }

        Product productResponse = this.productRepository.save(newProduct);

        return productMapper.mapToDto(productResponse);
    }

    @Override
    public ObjectResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<Product> pages = this.productRepository.findAll(pageable);

        // Lay ra gia tri (content) cua page
        List<Product> products = pages.getContent();


        // Ep kieu sang dto
        List<ProductDto> content = products.stream().map(product -> productMapper.mapToDto(product)).collect(Collectors.toList());

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        ObjectResponse<ProductDto> response = new ObjectResponse();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return response;
    }

    @Override
    public ProductDetailResponseDto getProductById(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        Category category = this.categoryRepository.findByUrlKey(product.getCategoryUrl());
        CategoryResponseDto categoryResponseDto = this.categoryMapper.mapToResponseDto(category);

        ProductDetailResponseDto productDetailResponseDto = productMapper.mapToResponseDetailDto(product);
        productDetailResponseDto.setCategory(categoryResponseDto);
        return productDetailResponseDto;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setDescription(productDto.getDescription());
        product.setDiscountRate(productDto.getDiscountRate());
        product.setPrice(productDto.getPrice());
        product.setQuantitySold(productDto.getQuantitySold());
        product.setReviewCount(productDto.getReviewCount());
        product.setRatingAverage(productDto.getRatingAverage());
        product.setThumbnailUrl(productDto.getThumbnailUrl());
        product.setCategoryUrl(productDto.getCategoryUrl());
        product.setProductSlug(SlugConvert.convert(product.getName()));
        product.setOptions(new ArrayList<>());

        if(productDto.getOptions()!=null) {
            List<ProductOption> options = productDto.getOptions()
                    .stream()
                    .map((option) -> this.optionMapper.mapToEntity(option))
                    .toList();
            for(ProductOption option : options) {
                product.updateOption(option);
            }
        }
        if(!product.getCategoryUrl().isEmpty()){
            String categoryUrl = SlugConvert.convert(product.getCategoryUrl());
            Category category = this.categoryRepository.findByUrlKey(categoryUrl);
            product.setCategory(category);
        }
        if(productDto.getSpecifications()!= null) {
            Set<ProductSpecification> specifications = productDto.getSpecifications().stream().map(specification -> specificationMapper.mapToEntity(specification)).collect(Collectors.toSet());
            Set<ProductSpecification> productSpecifications = new HashSet<>(product.getSpecifications());
            for(ProductSpecification specification : productSpecifications){
                boolean isContain = specifications.contains(specification);
                if(!isContain && product.dismissSpecification(specification)){
                    ProductSpecification deletedSpecification = this.specificationRepository.findById(specification.getId()).orElseThrow(() -> new ResourceNotFoundException("Specification", "id", specification.getId()));
                    this.specificationRepository.delete(deletedSpecification);
                }
            }
            for(ProductSpecification specification : specifications){
                product.updateSpecification(specification);
            }
        }

        this.productRepository.save(product);

        return productMapper.mapToDto(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        this.productRepository.delete(product);
    }

    @Override
    public ProductWithOptionForCartDto getProductByProductOptionId(String productOptionIds){

        String[] ids = productOptionIds.split(",");

        ProductOption firstOption = this.optionRepository.findById(Long.valueOf(ids[0]).longValue()).get();
        Product product = firstOption.getProduct();

        List<ProductOptionDto> optionDtoList = new ArrayList<>();

        for(String productOptionId : ids){
            ProductOptionDto option = optionMapper.mapToDto(this.optionRepository.findById(Long.valueOf(productOptionId).longValue()).get());
            optionDtoList.add(option);
        }

        ProductWithOptionForCartDto productWithOptionForCartDto;
        productWithOptionForCartDto = this.productWithOptionMapper.mapToProductOptionDto(product);
        productWithOptionForCartDto.setOption(optionDtoList);
        return productWithOptionForCartDto;
    }

    @Override
    public ObjectResponse<ProductDto> searchProduct(String name, int pageNo, int pageSize, String sortBy, String sortDir){
        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<Product> pages = this.productRepository.searchProductByName(name, pageable);

        // Lay ra gia tri (content) cua page
        List<Product> products = pages.getContent();


        // Ep kieu sang dto
        List<ProductDto> content = products.stream().map(product -> productMapper.mapToDto(product)).collect(Collectors.toList());

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        ObjectResponse<ProductDto> response = new ObjectResponse();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return response;
    }
}
