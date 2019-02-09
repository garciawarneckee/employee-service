package com.linke.employeeservice.employee.repository;

import com.linke.employeeservice.employee.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class provides Criteria filter methods to be apply over Employee entity.
 */

public class EmployeeSpecifications {

    public static Specification<Employee> hasFirstName(String firstName) {
        return (employee, query, builder) -> builder.equal(employee.get("firstName"), firstName);
    }

    public static Specification<Employee> hasLastName(String lastName) {
        return (employee, query, builder) -> builder.equal(employee.get("lastName"), lastName);
    }

    public static Specification<Employee> hasCharge(String charge) {
        return (employee, query, builder) -> builder.equal(employee.get("charge"), charge);
    }

    public static Specification<Employee> hasSalary(Long salary) {
        return (employee, query, builder) -> builder.equal(employee.get("salary"), salary);
    }


    /**
     * Combines all the previous criteria definitions to build a final query block to be applied in the find all
     * query
     * @param firstName employee first name
     * @param lastName employee last name
     * @param charge employee charge
     * @param salary employee salary
     * @return a list of predicates to be applied in future query
     */
    public static Specification<Employee> findByCriteria(String firstName, String lastName,
                                                         String charge, Long salary) {
        return (root, query, builder) -> {
            List<Specification> specifications = new ArrayList<>();
            if(firstName != null) { specifications.add(EmployeeSpecifications.hasFirstName(firstName)); }
            if(lastName != null) { specifications.add(EmployeeSpecifications.hasLastName(lastName)); }
            if(charge != null) { specifications.add(EmployeeSpecifications.hasCharge(charge)); }
            if(salary != null) { specifications.add(EmployeeSpecifications.hasSalary(salary)); }

            List<Predicate> predicates = specifications
                                .stream()
                                .map(s -> s.toPredicate(root, query, builder))
                                .collect(Collectors.toList());

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
