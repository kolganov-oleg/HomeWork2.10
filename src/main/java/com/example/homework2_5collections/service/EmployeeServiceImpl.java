package com.example.homework2_5collections.service;

import com.example.homework2_5collections.exception.EmployeeNotFoundException;
import com.example.homework2_5collections.model.Employee;
import com.example.homework_stream.exeption.IncorrectInputException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isAlpha;

@Service
public abstract class EmployeeServiceImpl implements EmployeeService {

    private final List<Employee> employees;

    public EmployeeServiceImpl(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public Employee add(String firstName, String lastName, int salary, int department) {
        checkUserName(firstName, lastName);
        Employee employee = new Employee(firstName, lastName, salary, department);
        if (employees.contains(employee)) {
            throw new EmployeeNotFoundException();
        }
        employees.add(employee);
        return employee;
    }

    @Override
    public Employee remove(String firstName, String lastName) {
        checkUserName(firstName, lastName);
        Employee employee = find(firstName, lastName);
        employees.remove(employee);

        throw new EmployeeNotFoundException();
    }

    @Override
    public Employee find(String firstName, String lastName) {
        checkUserName(firstName, lastName);
        Optional<Employee> employee = employees.stream()
                .filter(e -> e.getFirstName().equals(firstName) && e.getLastName().equals(lastName))
                .findAny();

        return employee.orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    public Employee getMaxSalary(int department) {
        return employees.stream()
                .filter(e -> e.getDepartment() == department)
                .max(Comparator.comparingInt(employee -> employee.getSalary()))
                .orElseThrow(EmployeeNotFoundException::new);

    }

    @Override
    public Employee getMinSalary(int department) {
        return employees.stream()
                .filter(e -> e.getDepartment() == department)
                .min(Comparator.comparingInt(Employee::getSalary))
                .orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    public List<Employee> getAllEmployeeInDepartment(int department) {
        return  employees.stream()
                .filter(e -> e.getDepartment() == department)
                .collect(Collectors.toList());

    }

    @Override
    public List<Employee> getAllEmployeeMultipleDepartment() {
        return employees.stream()
                .sorted(Comparator.comparingInt(Employee::getDepartment))
                .collect(Collectors.toList());
    }


    @Override
    public Collection<Employee> findAll() {
        return Collections.unmodifiableCollection(employees);
    }


    private void checkUserName(String firstName, String lastName) {
        if (!isAlpha(firstName) && !isAlpha(lastName)) {
            throw new IncorrectInputException();
        }
    }
    public List<Employee> getEmployees() {
        employees.add(new Employee("????????", "??????????????", 30000, 2));
        employees.add(new Employee("????????", "??????????????", 25000, 1));
        employees.add(new Employee("??????????", "????????????", 24000, 3));
        employees.add(new Employee("????????", "????????????????", 20000, 2));
        employees.add(new Employee("????????", "??????????", 15000, 3));
        employees.add(new Employee("??????????", "????????????????", 40000, 1));
        return employees;
    }
}