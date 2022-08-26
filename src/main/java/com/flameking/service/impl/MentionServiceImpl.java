package com.flameking.service.impl;

import com.flameking.entity.Mention;
import com.flameking.entity.Post;
import com.flameking.entity.User;
import com.flameking.mapper.MentionMapper;
import com.flameking.mapper.UserMapper;
import com.flameking.service.MentionService;
import com.flameking.service.MessageService;
import com.flameking.utils.JWTUtil;
import com.flameking.utils.ReplaceUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
@Service
public class MentionServiceImpl implements MentionService {
    @Autowired
    MentionMapper mentionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MessageService messageService;
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 提到的，因为一个用户发布文章，只关心有没有发布成功，添加提醒也是
     * 耗时的，所以用异步任务@Async
     * @param post 文章
     * @param newSet 被@的用户的姓名
     */
    @Async("mySimpleAsync")
    public void addMention(Post post, Set<String> newSet) {
        Mention mention=new Mention();
        mention.setFrom_id(post.getUid());
        mention.setTime(post.getCreate_time());
        mention.setContent(post.getContent());
        mention.setTitle(post.getTitle());
        mention.setDescr(post.getDescr());
        mention.setType_id(post.getType_id());
        mention.setPid(post.getId());
        String username=userMapper.findUserById(post.getUid()).getUsername();
        for(String name :newSet){
            //添加mention
            Integer id = userMapper.findUserByUserName(name).getId();
            mention.setTo_id(id);
            mentionMapper.addMention(mention);
            Integer mentionId = mention.getId();

            //消息提醒
            String message=ReplaceUtil.userReplace(username)+"在" +
                    ReplaceUtil.mentionReplace(mentionId,mention.getTitle())+"提到了你";
            messageService.addMessage(id,message,3);
        }
    }

    /**
     * 找出一个所有被提到的
     * @param id 用户ID
     * @return
     */
    @Override
    public List<Mention> findMentionsByUid(Integer id) {
        List<Mention> mentions = mentionMapper.findMentionsByUid(id);
        for(Mention mention:mentions) {
            User user = userMapper.findUserById(mention.getFrom_id());
            mention.setFrom_name(user.getUsername());
            mention.setFrom_avatar(user.getAvatar());
        }
        return mentions;
    }

    /**
     * 找出被提到的详情
     * @param id
     * @return
     */
    @Override
    public Mention findMentionById(Integer id) {

        Mention mention = mentionMapper.findMentionById(id);
        User user = userMapper.findUserById(mention.getFrom_id());
        mention.setFrom_name(user.getUsername());
        mention.setFrom_avatar(user.getAvatar());
        return mention;
    }

    /**
     * 删除被提到的
     * @param mention
     * @return
     */
    @Override
    public boolean deleteMention(Mention mention) {
        if(mention.getTo_id()==jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal())&&mention.getStatus()!=0){
            mentionMapper.deleteMention(mention.getId());
            return true;
        }
        return false;
    }

}
