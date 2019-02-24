package me.practice.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="company")
public class Company {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private String city;
    private String phone;
    
    @OneToMany(mappedBy="company", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Product> products;
    
    public Company() {
        this.products = new ArrayList<>();
    }

    public Company(String name, String city, String phone) {
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.products = new ArrayList<>();
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
