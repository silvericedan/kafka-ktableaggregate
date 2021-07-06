package com.example.ktableaggregate.services;


import com.example.ktableaggregate.model.DepartmentAggregate;
import com.example.ktableaggregate.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RecordBuilder {
    public DepartmentAggregate init(){
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(0);
        departmentAggregate.setTotalSalary(0);
        departmentAggregate.setAvgSalary(0D);
        return departmentAggregate;
    }

    //when an employee joins a department, we call the adder method
    public DepartmentAggregate adder(Employee emp, DepartmentAggregate aggValue){
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(aggValue.getEmployeeCount() + 1);
        departmentAggregate.setTotalSalary(aggValue.getTotalSalary() + emp.getSalary());
        departmentAggregate.setAvgSalary((aggValue.getTotalSalary() + emp.getSalary()) / (aggValue.getEmployeeCount() + 1D));
        return departmentAggregate;
    }

    //when an employee leaves a department, we call the substractor method
    public DepartmentAggregate subtractor(Employee emp, DepartmentAggregate aggValue){
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(aggValue.getEmployeeCount() - 1);
        departmentAggregate.setTotalSalary(aggValue.getTotalSalary() - emp.getSalary());
        departmentAggregate.setAvgSalary((aggValue.getTotalSalary() - emp.getSalary()) / (aggValue.getEmployeeCount() - 1D));
        return departmentAggregate;
    }
}
