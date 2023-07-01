package com.rainfir.server.service;

import com.rainfir.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainfir.server.pojo.Menu;
import com.rainfir.server.pojo.RespBean;
import com.rainfir.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-06
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);
    /**
     * 根据用户名获取用户
     * @param username
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);


    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(String keywords);

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminRole(Integer adminId, Integer[] rids);

    /**
     * 更新用户密码
     * com.rainfir.server.pojo.RespBean
     * @return
 * @param oldPass
 * @param pass
 * @param adminId
     * @author rainfir
     * @create 2023/5/5
     **/
    RespBean updateAdminPassword(String oldPass, String pass, Integer adminId);
}
