package com.linke.employeeservice;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.linke.employeeservice.employee.Employee;
import com.linke.employeeservice.employee.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner dataLoader(EmployeeRepository repository) {

		return (args) -> {
			List<Employee> employeeList = Arrays
					.asList(
							new Employee("David", "Beckham", "Developer", 400000L),
							new Employee("Michael", "Ballack", "Scrum Master", 450000L),
							new Employee("Lionel", "Messi", "Software Architect", 500000L));

			 employeeList.forEach(repository::save);
		};
	}

}

