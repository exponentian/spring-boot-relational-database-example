package me.practice.repository;

import me.practice.entity.Company;
import org.springframework.data.repository.CrudRepository;


public interface CompanyRepository extends CrudRepository<Company, Integer> {

}
