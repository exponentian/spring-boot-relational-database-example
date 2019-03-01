package me.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.practice.entity.Product;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerTest {

    
    @Autowired
    private MockMvc mockMvc;
    
    private final String API = "/api/products";
    
    @Test
    public void test01GetProduct() throws Exception {
        int id = 2;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.name", is("iPad")) )
                .andExpect( jsonPath("$.productNumber", is("p0002")) )
                .andExpect( jsonPath("$.madeIn", is("USA")) )
                .andReturn();
    }
    
    @Test
    public void test02GetProductNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Product with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test03GetAllProducts() throws Exception {
        this.mockMvc.perform( get(this.API) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(5)) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].name", is("iPhone")) )
                .andExpect( jsonPath("$[0].productNumber", is("p0001")) )
                .andExpect( jsonPath("$[0].madeIn", is("USA")) )
                .andReturn();
    }
    
    @Test
    public void test04GetAllProductsByCompany() throws Exception {
        this.mockMvc.perform( get("/api/companies/1/products") )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(3)) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].name", is("iPhone")) )
                .andExpect( jsonPath("$[0].productNumber", is("p0001")) )
                .andExpect( jsonPath("$[0].madeIn", is("USA")) )
                .andExpect( jsonPath("$[1].id", is(2)) )
                .andExpect( jsonPath("$[1].name", is("iPad")) )
                .andExpect( jsonPath("$[1].productNumber", is("p0002")) )
                .andExpect( jsonPath("$[1].madeIn", is("USA")) )
                .andExpect( jsonPath("$[2].id", is(3)) )
                .andExpect( jsonPath("$[2].name", is("MacBook")) )
                .andExpect( jsonPath("$[2].productNumber", is("p0003")) )
                .andExpect( jsonPath("$[2].madeIn", is("USA")) )
                .andReturn();
    }
    
    @Test
    public void test05GetAllProductsByCompanyNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( get("/api/companies/" + id + "/products") )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Company with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test06AddProduct() throws Exception {
        this.mockMvc.perform( post("/api/companies/3/products")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(asJsonString( this.newProduct() )) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(6)) )
                .andExpect( jsonPath("$.name", is("Notebook")) )
                .andExpect( jsonPath("$.productNumber", is("p1111")) )
                .andExpect( jsonPath("$.madeIn", is("Japan")) )
                .andReturn();
    }

    @Test
    public void test07AddProductNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( post("/api/companies/" + id + "/products")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(asJsonString( this.newProduct() )) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Company with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test08UpdateProduct() throws Exception {
        int id = 5;
        
        Product galaxyS = getProduct( apiGet("/" + id).getResponse() );
        galaxyS.setMadeIn("Republic of Korea");
        
        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString( galaxyS )) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.name", is("Galaxy S")) )
                .andExpect( jsonPath("$.productNumber", is("p0005")) )
                .andExpect( jsonPath("$.madeIn", is("Republic of Korea")) )
                .andReturn();
    }
    
    @Test
    public void test09UpdateProductNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content( asJsonString( new Product("test", "test", "test") )) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Product with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test10DeleteProduct() throws Exception {
        String id = "1";
        this.mockMvc.perform( delete(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("Deleted a product with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test11DeleteProductNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( delete(this.API + "/" + id) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Product with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test12DeleteAllProducts() throws Exception {
        this.mockMvc.perform(delete(this.API))
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("All products are deleted successfully")) )
                .andReturn();
    }   
    
    
    // Helper function
    
    public Product newProduct() {
        return new Product("Notebook", "p1111", "Japan");
    }
    
    public MvcResult apiGet(String uri) throws Exception {
        return this.mockMvc.perform(get(this.API + uri))
            .andExpect(status().isOk())
            .andReturn();
    }
    
    public Product getProduct(MockHttpServletResponse res) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(res.getContentAsString(), Product.class);
    }
    
    public String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
