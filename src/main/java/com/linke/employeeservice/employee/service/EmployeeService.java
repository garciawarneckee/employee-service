package com.linke.employeeservice.employee.service;

import com.linke.employeeservice.employee.Employee;
import com.linke.employeeservice.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll(Specification filters) {
        return employeeRepository.findAll(filters);
    }

    public Boolean save(Employee employee) {
        try {
            employeeRepository.save(employee);
            return Boolean.TRUE;
        } catch(RuntimeException rex) {
            throw new RuntimeException("Error while saving");
        }
    }

    @Transactional
    public Boolean bulkSave(List<Employee> employees) {
        employees.forEach(this::save);
        return Boolean.TRUE;
    }

    public Boolean delete(Long id) {
        try {
            employeeRepository.deleteById(id);
            return Boolean.TRUE;
        } catch(RuntimeException rex) {
            throw new RuntimeException("Error while deleting");
        }
    }
}
