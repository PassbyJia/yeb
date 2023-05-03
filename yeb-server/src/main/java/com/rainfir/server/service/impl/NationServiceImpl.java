package com.rainfir.server.service.impl;

import com.rainfir.server.pojo.Nation;
import com.rainfir.server.mapper.NationMapper;
import com.rainfir.server.service.INationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rainfir
 * @since 2023-04-29
 */
@Service
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
