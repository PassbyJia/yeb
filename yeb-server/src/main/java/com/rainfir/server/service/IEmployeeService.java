package com.rainfir.server.service;

import com.rainfir.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainfir.server.pojo.RespBean;
import com.rainfir.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-06
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 获取所有员工（分页）
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     *  获取工号
     * com.rainfir.server.pojo.RespBean
     * @return
     * @author rainfir
     * @create 2023/4/29
     **/
    RespBean maxWorkID();

    /**
     * 添加员工
     * com.rainfir.server.pojo.RespBean
     * @return
     * * @param employee
     * @author rainfir
     * @create 2023/5/3
     **/
    RespBean addEmp(Employee employee);

    /**
     * 查询员工
     * java.util.List<com.rainfir.server.pojo.Employee>
     * @return
 * @param id
     * @author rainfir
     * @create 2023/5/3
     **/
    List<Employee> getEmployee(Integer id);
}
