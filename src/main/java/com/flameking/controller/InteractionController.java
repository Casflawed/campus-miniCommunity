package com.flameking.controller;

import com.flameking.entity.ResultBean;
import com.flameking.service.InteractionService;
import com.flameking.sysLog.Logweb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 点赞收藏相关的接口
 */
@RestController
public class InteractionController {
    @Autowired
    InteractionService interactionService;

    @Logweb("点赞")
    @GetMapping("/onStart/{postId}")
    public ResultBean onStart(@PathVariable Integer postId){
        if(interactionService.existsStar(postId)){
            return ResultBean.fail("点赞失败");
        }
        interactionService.onStart(postId);
        return ResultBean.success("点赞成功");
    }
    @Logweb("取消点赞")
    @GetMapping("/unStart/{postId}")
    public ResultBean unStart(@PathVariable Integer postId){
        if(!interactionService.existsStar(postId)){
            return ResultBean.fail("取消点赞失败");
        }
        interactionService.unStart(postId);
        return ResultBean.success("取消点赞成功");
    }
    @Logweb("收藏")
    @GetMapping("/onCollect/{postId}")
    public ResultBean onCollect(@PathVariable Integer postId){
        if(interactionService.existsCollect(postId)){
            return ResultBean.fail("收藏失败");
        }
        else {
            interactionService.onCollect(postId);
        }
        return ResultBean.success("收藏成功");
    }
    @Logweb("取消收藏")
    @GetMapping("/unCollect/{postId}")
    public ResultBean unCollect(@PathVariable Integer postId){
        if(!interactionService.existsCollect(postId)){
            return ResultBean.fail("取消收藏失败");
        }
        else {
            interactionService.unCollect(postId);
        }
        return ResultBean.success("取消收藏成功");
    }



}
