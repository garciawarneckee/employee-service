package com.linke.employeeservice.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Boolean save(Employee employee) {
        try {
            employeeRepository.save(employee);
            return Boolean.TRUE;
        } catch(RuntimeException rex) {
            throw new RuntimeException("Error while saving");
        }
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
