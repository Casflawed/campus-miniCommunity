package com.flameking.controller;


import com.flameking.entity.Mention;
import com.flameking.entity.ResultBean;
import com.flameking.entity.User;
import com.flameking.service.MentionService;
import com.flameking.service.PostService;
import com.flameking.service.ProfileService;
import com.flameking.service.UserService;
import com.flameking.sysLog.Logweb;
import com.flameking.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 个人主页相关的接口
 */
@RequestMapping("/profile")
@RestController
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    MentionService mentionService;
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 个人主页信息
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/findUserProfile/{username}")
    public ResultBean findUserProfile(@PathVariable String username){
        User user=userService.findUserDetails(username);
        if(user==null)
            return ResultBean.fail("用户名不存在");
        boolean isfollower=profileService.isFollower(user.getId(),jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
        return ResultBean.success().add("user",user).add("isfollower",isfollower);
    }

    /**
     * 关注，当前请求接口的用户，关注这个id的用户
     * @param userId 被关注的人id
     * @return
     */
    @Logweb("关注")
    @GetMapping("/follow/{userId}")
    public ResultBean follow(@PathVariable Integer userId){
        boolean result = profileService.follow(userId);
        if(!result)return ResultBean.fail("关注失败");
        return ResultBean.success("关注成功");
    }

    /**
     * 取消关注，当前请求接口的用户，取消关注这个id的用户
     * @param userId
     * @return
     */
    @Logweb("取消关注")
    @GetMapping("/unFollow/{userId}")
    public ResultBean unfollow(@PathVariable Integer userId){
        boolean result = profileService.unFollow(userId);
        if(!result)return ResultBean.fail("取消关注失败");
        return ResultBean.success("取消关注成功");
    }

    /**
     * 个人主页，查看这个用户的动态（也就是发过的文章）
     * @param id 这个用户id
     * @return
     */
    @GetMapping("/findPostById/{id}")
    public ResultBean findPostByUserId(@PathVariable Integer id){
        return ResultBean.success().add("postList",postService.findPostByUserId(id));
    }

    /**
     * 删除文章，在个人主页删除文章
     * @param map
     * @return
     */
    @Logweb("删除文章")
    @PutMapping("/delete")
    public ResultBean deletePost(@RequestBody Map map){
        boolean result = postService.delete((Integer) map.get("id"));
        if(result){
            return ResultBean.success("删除成功");
        }
        return ResultBean.fail("删除失败");
    }

    /**
     * 个人主页，收藏的所有文章
     * @param id 用户id
     * @return
     */
    @GetMapping("/findCollect/{id}")
    public ResultBean findCollectById(@PathVariable  Integer id){
        return ResultBean.success(profileService.findCollectById(id));
    }

    /**
     * 个人主页，查出这个用户的粉丝
     * @param id
     * @return
     */
    @GetMapping("/findFollowers/{id}")
    public ResultBean findFollowers(@PathVariable Integer id){
        List<User> followers = profileService.findFollowers(id);
        return ResultBean.success().add("followers",followers);
    }

    /**
     * 个人主页，查出这个用户的关注
     * @param id
     * @return
     */
    @GetMapping("/findFollowings/{id}")
    public ResultBean findFollowings(@PathVariable Integer id){
        List<User> followings = profileService.findFollowings(id);
        return ResultBean.success().add("followers",followings);
    }

    /**
     * 个人主页，查出这个用户所有被提到的
     * 也就是被别人@
     * @param id
     * @return
     */
    @GetMapping("/findMentionsByUid/{id}")
    public ResultBean findMentionsByUid(@PathVariable Integer id){
        List<Mention> list=mentionService.findMentionsByUid(id);
        return ResultBean.success().add("list",list);
    }

    /**
     * 查看某一个被提到的具体内容
     * @param id
     * @return
     */
    @GetMapping("/findMentionById/{id}")
    public ResultBean findMentionById(@PathVariable Integer id){
        Mention mention=mentionService.findMentionById(id);
        return ResultBean.success().add("data",mention);
    }

    /**
     * 删除被提到的
     * @param mention
     * @return
     */
    @Logweb("删除提醒")
    @PutMapping("/deleteMention")
    public ResultBean deleteMention(@RequestBody Mention mention){

        if(mentionService.deleteMention(mention))
            return ResultBean.success();
        return ResultBean.fail();
    }
}
