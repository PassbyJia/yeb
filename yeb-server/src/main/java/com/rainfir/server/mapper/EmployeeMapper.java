package com.rainfir.server.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainfir.server.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-06
 */
public interface EmployeeMapper extends BaseMapper<Employee> {


    /**
     *  获取所有员工（分页）
     * com.baomidou.mybatisplus.core.metadata.IPage<com.rainfir.server.pojo.Employee>
     * @return
     * @param page
     * @param employee
     * @param beginDateScope
     * @author rainfir
     * @create 2023/4/29
     **/
    IPage<Employee> getEmployeeByPage(Page<Employee> page, @Param("empolyee") Employee employee,
                                      @Param("beginDateScope") LocalDate[] beginDateScope);

    /**
     * 查询员工
     * java.util.List<com.rainfir.server.pojo.Employee>
     * @return
     * @param id
     * @author rainfir
     * @create 2023/5/3
     **/
    List<Employee> getEmployee(Integer id);

    /**
     * 获取所有员工账套
     * com.rainfir.server.pojo.RespPageBean
     *
     * @param page
     * @return
     * @author rainfir
     * @create 2023/5/5
     **/
    IPage<Employee> getEmployeeWithSalary(Page<Employee> page);
}
