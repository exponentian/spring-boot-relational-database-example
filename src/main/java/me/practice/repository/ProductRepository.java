package me.practice.repository;

import java.util.List;
import me.practice.entity.Product;
import org.springframework.data.repository.CrudRepository;


public interface ProductRepository extends CrudRepository<Product, Integer> {
    List<Product> findProductsByCompanyId(Integer companyId);
}
