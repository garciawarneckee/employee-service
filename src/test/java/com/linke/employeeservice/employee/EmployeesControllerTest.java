package com.linke.employeeservice.employee;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linke.employeeservice.employee.repository.EmployeeSpecifications;
import com.linke.employeeservice.employee.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * In charge to test the REST interface of the API's Employee resource
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @MockBean
    private EmployeeSpecifications specifications;

    @Test
    public void shouldReturnListOfEmployees() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<Specification> spec = new ArrayList<>();

        List<Employee> employeesList = Arrays.asList(
                new Employee( 1L,"David", "Beckham", "Developer", 400000L, null),
                new Employee(2L,"Michael", "Ballack", "Scrum Master", 450000L, null),
                new Employee(3L,"Lionel", "Messi", "Software Architect", 50000L, null));

        when(specifications.buildCriteria(
                null,
                null,
                null,
                null))
                .thenReturn((root, query, builder) -> builder.and(spec.toArray(new Predicate[]{}))) ;

        when(service
                .getAll(specifications.buildCriteria(null ,null, null, null)))
                .thenReturn(employeesList);

        MvcResult result = this.mockMvc.perform(get("/employees"))
                .andReturn();

        List<Employee> actual = mapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<Employee>>() {});

        assertThat(actual).isEqualTo(employeesList);
    }

    @Test
    public void shouldReturnOnlyOneEmployeeByFirstName() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<Specification> spec = Arrays.asList(specifications.hasFirstName("David"));

        List<Employee> employeesList = Arrays.asList(
                new Employee( 1L,"David", "Beckham", "Developer", 400000L, null),
                new Employee(2L,"Michael", "Ballack", "Scrum Master", 450000L, null),
                new Employee(3L,"Lionel", "Messi", "Software Architect", 50000L, null));

        List expectedList = Collections.singletonList(employeesList.toArray()[0]);

        when(specifications.buildCriteria(
                "David",
                null,
                null,
                null))
                .thenReturn((root, query, builder) -> builder.and(spec.toArray(new Predicate[]{}))) ;

        when(service
                .getAll(specifications.buildCriteria("David" ,null, null, null)))
                .thenReturn(expectedList);

        MvcResult result = this.mockMvc.perform(get("/employees?firstName=David"))
                .andReturn();

        List<Employee> actual = mapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<Employee>>() {});

        assertThat(actual).isEqualTo(expectedList);
    }

    @Test
    public void shouldReturnOnlyTwoEmployeesByLastName() throws Exception {

        ObjectMapper mapper = new ObjectMapper();


        List<Employee> employeesList = Arrays.asList(
                new Employee( 1L,"David", "Beckham", "Developer", 400000L, null),
                new Employee(2L,"Michael", "Ballack", "Scrum Master", 450000L, null),
                new Employee(3L,"Lionel", "Messi", "Software Architect", 50000L, null),
                new Employee(4L,"Leonard", "Ballack", "CEO", 1450000L, null));

        List expectedList = Arrays.asList(
                employeesList.toArray()[1], employeesList.toArray()[3]);

        List<Specification> specs = Arrays.asList(specifications.hasLastName("Ballack"));

        when(specifications.buildCriteria(
                null,
                "Ballack",
                null,
                null))
                .thenReturn((root, query, builder) -> builder.and(specs.toArray(new Predicate[]{}))) ;

        when(service
                .getAll(specifications.buildCriteria(null ,"Ballack", null, null)))
                .thenReturn(expectedList);

        MvcResult result = this.mockMvc.perform(get("/employees?lastName=Ballack"))
                .andReturn();

        List<Employee> actual = mapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<Employee>>() {});

        assertThat(actual).isEqualTo(expectedList);
    }

    @Test
    public void shouldReturnOnlyThreeEmployeesByCharge() throws Exception {

        ObjectMapper mapper = new ObjectMapper();


        List<Employee> employeesList = Arrays.asList(
                new Employee( 1L,"David", "Beckham", "Developer", 400000L, null),
                new Employee(2L,"Michael", "Ballack", "Developer", 450000L, null),
                new Employee(3L,"Lionel", "Messi", "Developer", 50000L, null),
                new Employee(4L,"Leonard", "Ballack", "CEO", 1450000L, null));

        List expectedList = Arrays.asList(
                employeesList.toArray()[0],
                employeesList.toArray()[1],
                employeesList.toArray()[2]);

        List<Specification> specs = Arrays.asList(specifications.hasCharge("Developer"));

        when(specifications.buildCriteria(
                null,
                null,
                "Developer",
                null))
                .thenReturn((root, query, builder) -> builder.and(specs.toArray(new Predicate[]{}))) ;

        when(service
                .getAll(specifications.buildCriteria(null ,null, "Developer", null)))
                .thenReturn(expectedList);

        MvcResult result = this.mockMvc.perform(get("/employees?charge=Developer"))
                .andReturn();

        List<Employee> actual = mapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<Employee>>() {});

        assertThat(actual).isEqualTo(expectedList);
    }

    @Test
    public void shouldReturnOnlyOneEmployeesBySalary() throws Exception {

        ObjectMapper mapper = new ObjectMapper();


        List<Employee> employeesList = Arrays.asList(
                new Employee( 1L,"David", "Beckham", "Developer", 40000L, null),
                new Employee(2L,"Michael", "Ballack", "Developer", 45000L, null),
                new Employee(3L,"Lionel", "Messi", "Developer", 50000L, null),
                new Employee(4L,"Leonard", "Ballack", "CEO", 145000L, null));

        List expectedList = Arrays.asList(employeesList.toArray()[0]);

        List<Specification> specs = Collections.singletonList(specifications.hasSalary(40000L));

        when(specifications.buildCriteria(
                null,
                null,
                null,
                40000L))
                .thenReturn((root, query, builder) -> builder.and(specs.toArray(new Predicate[]{}))) ;

        when(service
                .getAll(specifications.buildCriteria(null ,null, null, 40000L)))
                .thenReturn(expectedList);

        MvcResult result = this.mockMvc.perform(get("/employees?salary=40000"))
                .andReturn();

        List<Employee> actual = mapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<Employee>>() {});

        assertThat(actual).isEqualTo(expectedList);
    }

    @Test
    public void shouldSaveSuccessFully() throws Exception {

        Employee employee = new Employee(
                "new",
                "employee",
                "a charge",
                4000L);

        when(service
                .save(employee))
                .thenReturn(Boolean.TRUE);

        this.mockMvc.perform(
                post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"new\", \"lastName\": \"employee\", " +
                                "\"charge\": \"charge\", \"salary\": 4000}"))
                .andExpect(content().string("true"))
                .andExpect(status().is(201));
    }

    @Test
    public void shouldDeleteSuccessfully() throws Exception {

        when(service
                .delete(3L))
                .thenReturn(Boolean.TRUE);

        this.mockMvc.perform(
                delete("/employees/3"))
                .andExpect(content().string("true"))
                .andExpect(status().is(200));
    }

}


