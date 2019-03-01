package me.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.practice.entity.Company;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
public class CompanyControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    private final String API = "/api/companies";
    
    @Test
    public void test01GetCompany() throws Exception {
        int id = 2;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.name", is("Samsung")) )
                .andExpect( jsonPath("$.city", is("Seoul, South Korea")) )
                .andExpect( jsonPath("$.phone", is("123-456-1111")) )
                .andExpect( jsonPath("$.products", hasSize(2)) )
                .andReturn();
    }
    
    @Test
    public void test02GetCompanyNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Company with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test03GetAllCompanies() throws Exception {
        this.mockMvc.perform( get(this.API) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(3)) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].name", is("Apple")) )
                .andExpect( jsonPath("$[0].city", is("San Francisco, USA")) )
                .andExpect( jsonPath("$[0].phone", is("123-456-7890")) )
                .andExpect( jsonPath("$[0].products", hasSize(3)) )
                .andReturn();
    }
    
    @Test
    public void test04AddCompany() throws Exception {
        this.mockMvc.perform( post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(asJsonString( this.newCompany() )) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(4)) )
                .andExpect( jsonPath("$.name", is("Mercedes-Benz")) )
                .andExpect( jsonPath("$.city", is("Stuttgart, Germany")) )
                .andExpect( jsonPath("$.phone", is("111-222-3333")) )
                .andExpect( jsonPath("$.products", hasSize(0)) )
                .andReturn();
    }
    
    @Test
    public void test05UpdateCompany() throws Exception {
        int id = 2;
        
        Company samsung = getCompany( apiGet("/" + id).getResponse() );
        samsung.setCity("Seoul, Republic of Korea");
        
        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString( samsung )) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.name", is("Samsung")) )
                .andExpect( jsonPath("$.city", is("Seoul, Republic of Korea")) )
                .andExpect( jsonPath("$.phone", is("123-456-1111")) )
                .andExpect( jsonPath("$.products", hasSize(2)) )
                .andReturn();
        
    }
    
    @Test
    public void test06UpdateCompanyNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content( asJsonString( new Company("test", "test", "test") )) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Company with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test07DeleteCompany() throws Exception {
        String id = "1";
        this.mockMvc.perform( delete(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("Deleted a company with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test08DeleteCompanyNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( delete(this.API + "/" + id) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$", is("Could not found Company with id " + id)) )
                .andReturn();
    }
    
    @Test
    public void test09DeleteAllCompanies() throws Exception {
        this.mockMvc.perform(delete(this.API))
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("All companies are deleted successfully")) )
                .andReturn();
    }
    
    // Helper functions
    public Company newCompany() {
        return new Company("Mercedes-Benz", "Stuttgart, Germany", "111-222-3333");
    }
    
    public String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public MvcResult apiGet(String uri) throws Exception {
        return this.mockMvc.perform(get(this.API + uri))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }
    
    public Company getCompany(MockHttpServletResponse res) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(res.getContentAsString(), Company.class);
    }
    
}
