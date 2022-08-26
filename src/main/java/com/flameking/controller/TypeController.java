package com.flameking.controller;

import com.flameking.entity.ResultBean;
import com.flameking.entity.Type;
import com.flameking.service.TypeService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/type")
@RestController
public class TypeController {
    @Autowired
    TypeService typeService;

    @RequiresAuthentication
    @ApiOperation(value = "文章类型列表",notes = "返回文章类型列表")
    @GetMapping("/postTypeList")
    public ResultBean findTypeList(){
        List<Type> typeList = typeService.findTypeList();
        return ResultBean.success().add("typeList", typeList);
    }

    @RequiresAuthentication
    @ApiOperation(value = "文章类型列表",notes = "返回文章类型列表")
    @GetMapping("/postTypeNum")
    public ResultBean findTypeNum(){
        return ResultBean.success().add("typeList",typeService.findTypeListNums());
    }

    @RequiresAuthentication
    @GetMapping("/findTypeIdByName/{name}")
    public ResultBean findTypeIdByName(@PathVariable String name){
        return ResultBean.success().add("type_id",typeService.findTypeIdByName(name));
    }
}
