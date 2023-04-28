package com.rainfir.server.mapper;

import com.rainfir.server.pojo.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainfir.server.pojo.RespBean;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-06
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer addAdminRole(@Param("adminId") Integer adminId,@Param("rids") Integer[] rids);
}
