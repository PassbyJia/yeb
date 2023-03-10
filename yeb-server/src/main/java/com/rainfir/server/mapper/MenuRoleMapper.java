package com.rainfir.server.mapper;

import com.rainfir.server.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-06
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 批量更新角色菜单
     *
     * @param rid
     * @param mids
     */
    int insertRecord(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
