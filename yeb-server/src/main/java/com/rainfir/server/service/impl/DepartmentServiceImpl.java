package com.rainfir.server.service.impl;

import com.rainfir.server.pojo.Department;
import com.rainfir.server.mapper.DepartmentMapper;
import com.rainfir.server.pojo.RespBean;
import com.rainfir.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-06
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    /**
     * 获取所有部门
     * @return
     */
    @Override
    public List<Department> getAllDepartments(Integer parentId) {
        return departmentMapper.getAllDepartments(parentId);
    }

    /**
     * 添加部门
     * @param dep
     * @return
     */
    @Override
    public RespBean addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if (1==dep.getResult()){
            return RespBean.success("添加成功！！",dep);
        }
        return RespBean.error("添加失败！！");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deleteDep(Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDep(department);
        if (department.getResult()==-2){
            return RespBean.error("该部门下还有子部门，删除失败！");
        }
        if(department.getResult()==-1){
            return RespBean.error("该部门下还有员工，删除失败！");
        }
        if(department.getResult()==1){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
