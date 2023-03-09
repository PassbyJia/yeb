package com.rainfir.server.service;

import com.rainfir.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainfir.server.pojo.Menu;
import com.rainfir.server.pojo.RespBean;

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


}
