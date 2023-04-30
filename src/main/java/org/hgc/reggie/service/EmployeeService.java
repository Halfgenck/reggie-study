package org.hgc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hgc.reggie.common.R;
import org.hgc.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 0:11
 */
public interface EmployeeService extends IService<Employee> {
    R<Employee> getEmployeeById(Long id);

    R<String> updateEmployeeById(HttpServletRequest request, Employee employee);

    R<Page> pageByEmployee(int page, int pageSize, String name);

    R<String> saveEmployee(HttpServletRequest request, Employee employee);

    R<String> logout(HttpServletRequest request);

    R<Employee> login(HttpServletRequest request, Employee employee);

    R<String> deleteEmployee(HttpServletRequest request, Long id);
}
