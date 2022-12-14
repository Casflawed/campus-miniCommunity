package com.flameking.controller;

import com.flameking.entity.ResultBean;
import com.flameking.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关注相关的接口
 */
@RestController
@RequestMapping("/following")
public class FollowingController {
    @Autowired
    FollowingService followingService;

    /**
     * 找到关注的人发的所有文章
     * @param page 分页的页数，第几页 默认是1
     * @param size 一页的size        默认是5
     * @return
     */
    @GetMapping("/findPost")
    public ResultBean findFollowingPost(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                                        @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        return ResultBean.success().add("postList",followingService.findFollowingPost(page, size));
    }

    /**
     * 找出已经关注的所有用户
     * @return
     */
    @GetMapping("/findFowings")
    public ResultBean findFowings(){
        return ResultBean.success().add("followers",followingService.findFowings());
    }
}
