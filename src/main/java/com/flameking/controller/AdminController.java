package com.flameking.controller;

import com.flameking.entity.ResultBean;
import com.github.pagehelper.PageInfo;
import com.flameking.entity.PostDetai;
import com.flameking.entity.ReportDeatil;
import com.flameking.entity.User;
import com.flameking.service.AdminService;
import com.flameking.service.PostService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台接口
 */
@RequestMapping("/admin")
@RestController
public class AdminController {
//    @RequiresRoles("admin")
//    @GetMapping("/admin/test")
//    public ResultBean test(){
//        return ResultBean.success("admin");
//    }

    @Autowired
    AdminService adminService;
    @Autowired
    PostService postService;

    /**
     * 查找出所有用户
     * @param page
     * @param size
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/getUser")
    public ResultBean getUser(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                              @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        PageInfo all = adminService.findAll(page, size);
        return ResultBean.success().add("userlist",all);
    }

    /**
     * 通过用户ID 搜索用户
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/getOne/{id}")
    public ResultBean findOne(@PathVariable Integer id){
        User user= adminService.findById(id);
        List<User> userList=new ArrayList<>();
        if(user!=null)
            userList.add(user);
        return ResultBean.success().add("userlist",userList);
    }

    /**
     * 修改用户状态
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/changeStatus/{id}")
    public ResultBean changeStatus(@PathVariable Integer id){
        boolean b = adminService.changeStatus(id);
        return b? ResultBean.success("修改成功"): ResultBean.fail("修改失败");
    }

    /**
     * 找出所有文章
     * @param page
     * @param size
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/getPost")
    public ResultBean getPost(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                              @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        return ResultBean.success("").add("postlist",postService.findAll(page, size));
    }

    /**
     * 通过文章ID进行查找
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/findOne/{id}")
    public ResultBean getOne(@PathVariable Integer id){
        PostDetai post = postService.findPostById(id);
        List<PostDetai> postList=new ArrayList<>();
        if(post!=null)
            postList.add(post);
        return ResultBean.success().add("postlist",postList);
    }

    /**
     * 修改文章的状态
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/changePostStatus/{id}")
    public ResultBean changePostStatus(@PathVariable Integer id){
        Boolean b=postService.changeStatus(id);
        return ResultBean.success();
    }

    /**
     * 找出所有未处理的举报
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/findAllReport")
    public ResultBean findAllReport(){
        List<ReportDeatil> allReport = adminService.findAllReport();
        return ResultBean.success().add("reportList",allReport);
    }

    /**
     * 把举报标记为已处理
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @PutMapping("/solve/{id}")
    public ResultBean solve(@PathVariable Integer id){
        adminService.solve(id);
        return ResultBean.success("success");
    }


    @RequiresRoles("admin")
    @PutMapping("/deleteSolve/{id}")
    public ResultBean deleteSolve(@PathVariable Integer id){
        adminService.deleteSolve(id);
        return ResultBean.success("success");
    }


}
