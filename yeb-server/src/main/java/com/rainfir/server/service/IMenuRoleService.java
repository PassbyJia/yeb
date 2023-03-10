package com.rainfir.server.service;

import com.rainfir.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainfir.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-06
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
