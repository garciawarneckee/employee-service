package com.linke.employeeservice.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
}
