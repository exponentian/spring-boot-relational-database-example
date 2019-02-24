package me.practice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class Product {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String productNumber;
    private String madeIn;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="company_id")
    @JsonIgnore
    private Company company;
    
    public Product() {}
    
    public Product(String name, String productNumber, String madeIn) {
        this.name = name;
        this.productNumber = productNumber;
        this.madeIn = madeIn;
    }
    
    public Product(String name, String productNumber, String madeIn, Company company) {
        this.name = name;
        this.productNumber = productNumber;
        this.madeIn = madeIn;
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
