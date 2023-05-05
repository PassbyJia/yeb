package com.rainfir.server.controller;

import com.rainfir.server.pojo.Admin;
import com.rainfir.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 在线聊天
 *
 * @param null
 * @author rainfir
 * @return
 * @create 2023/5/5
 **/
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
     private IAdminService adminService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/admin")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }
}
