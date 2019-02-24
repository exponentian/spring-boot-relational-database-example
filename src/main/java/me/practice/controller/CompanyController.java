package me.practice.controller;

import me.practice.exception.DataNotFoundException;

import java.util.List;
import java.util.ArrayList;
import me.practice.entity.Company;
import me.practice.repository.CompanyRepository;
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
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    
    // Get
    
    @GetMapping("/companies/{companyId}")
    public Company getCompany(@PathVariable Integer companyId) {
        return this.companyRepository.findById(companyId)
                .orElseThrow(() -> new DataNotFoundException("Company", companyId));
    }
    
    @GetMapping("/companies")
    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        this.companyRepository.findAll().forEach(company -> list.add(company));        
        return list;
    }
    
    // Post
    
    @PostMapping("/companies")
    public Company addCompany(@RequestBody Company company) {
        return this.companyRepository.save(company);
    }
    
    // Put
    
    @PutMapping("/companies/{companyId}")
    public Company updateCompany(@PathVariable Integer companyId, @RequestBody Company newCompany) {
        return this.companyRepository.findById(companyId)
                .map(company -> {
                    company.setName(newCompany.getName());
                    company.setCity(newCompany.getCity());
                    company.setPhone(newCompany.getPhone());
                    //company.setProducts(newCompany.getProducts());
                    return this.companyRepository.save(company);
                })
                .orElseThrow(() -> new DataNotFoundException("Company", companyId));
    }
    
    // Delete
    
    @DeleteMapping("/companies/{companyId}")
    public String deleteCompany(@PathVariable Integer companyId) {
        if (this.companyRepository.existsById(companyId)) {
            this.companyRepository.deleteById(companyId);
            return "Deleted a company with id " + Integer.toString(companyId);
        }
        throw new DataNotFoundException("Company", companyId);
    }
    
    @DeleteMapping("/companies")
    public String deleteAllCompanies() {
        this.companyRepository.deleteAll();
        return "All companies are deleted successfully";
    }
}
