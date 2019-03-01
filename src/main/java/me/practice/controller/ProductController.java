package me.practice.controller;

import java.util.ArrayList;
import java.util.List;
import me.practice.entity.Company;
import me.practice.entity.Product;
import me.practice.exception.DataNotFoundException;
import me.practice.repository.CompanyRepository;
import me.practice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {
    
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, CompanyRepository companyRepository) {
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
    }
    
    // Get
    
    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable Integer productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product", productId));
    }
    
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        this.productRepository.findAll().forEach(product -> list.add(product));
        return list;
    }
    
    @GetMapping("/companies/{companyId}/products")
    public List<Product> getAllProductsByCompany(@PathVariable Integer companyId) {
        if ( !this.companyRepository.existsById(companyId) ) {
            throw new DataNotFoundException("Company", companyId);   
        }
        return this.productRepository.findProductsByCompanyId(companyId);
    }
    
    // Post
    // curl -H "Content-Type: application/json" -d '{"name":"Notebook","productNumber": "p0009", "madeIn":"Japan"}' -X POST localhost:8080/api/companies/3/products
    
    @PostMapping("/companies/{companyId}/products")
    public Product addProduct(@PathVariable Integer companyId, @RequestBody Product product) {
        Company company = this.companyRepository.findById(companyId)
                .orElseThrow(() -> new DataNotFoundException("Company", companyId));
        product.setCompany(company);
        return this.productRepository.save(product);
    }
    
    // Put
    
    @PutMapping("/products/{productId}")
    public Product updateProduct(@PathVariable Integer productId, @RequestBody Product newProduct) {
        return this.productRepository.findById(productId)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setProductNumber(newProduct.getProductNumber());
                    product.setMadeIn(newProduct.getMadeIn());
                    //product.setCompany(newProduct.getCompany());
                    return this.productRepository.save(product);
                })
                .orElseThrow(() -> new DataNotFoundException("Product", productId));
    }
    
    // Delete
    
    @DeleteMapping("/products/{productId}")
    public String deleteProduct(@PathVariable Integer productId) {
        if ( this.productRepository.existsById(productId) ) {
            this.productRepository.deleteById(productId);
            return "Deleted a product with id " + Integer.toString(productId);
        }
        throw new DataNotFoundException("Product", productId);
    }
    
    @DeleteMapping("/products")
    public String deleteAllProducts() {
        this.productRepository.deleteAll();
        return "All products are deleted successfully";
    }
    
}
