package com.flameking.service.impl;

import com.flameking.entity.*;
import com.flameking.mapper.CommentMapper;
import com.flameking.mapper.MessageMapper;
import com.flameking.mapper.PostMapper;
import com.flameking.mapper.UserMapper;
import com.flameking.service.MessageService;

import com.flameking.utils.JWTUtil;
import com.flameking.utils.ReplaceUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 消息推送
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RabbitTemplate template;
    @Autowired
    private JWTUtil jwtUtil;


    /**
     * 评论消息提醒 异步任务
     * @param id 评论者的ID
     * @param message 内容
     * @param type_id 消息类型
     */
    @Async("mySimpleAsync")
    public void addMessage(Integer id,String message,Integer type_id) {
        Message msg=new Message();
        msg.setTo_id(id);
        msg.setMessage(message);
        msg.setType_id(type_id);
        msg.setTime(new Date());
        template.convertAndSend("fanout_msg_exchange","",String.valueOf(id));
        messageMapper.addMessage(msg);
    }

    /**
     *
     * @param type_id 消息类型
     * @param commentId 评论id
     * @param username 哪个用户
     */
    @Async("mySimpleAsync")
    public void addMessage(Integer type_id,Integer commentId,String username){
        Comment comment = commentMapper.findByCommentId(commentId);
        Post post = postMapper.findPostById(comment.getPid());
        Message msg=new Message();
        msg.setType_id(type_id);
        msg.setTo_id(comment.getUid());
        msg.setTime(new Date());
        String message=ReplaceUtil.userReplace(username)+"在"+ReplaceUtil.PostReplace(post.getId(),post.getTitle())+"回复了你";
        msg.setMessage(message);
        template.convertAndSend("fanout_msg_exchange","",String.valueOf(msg.getTo_id()));
        messageMapper.addMessage(msg);
    }

    /**
     * 点赞消息提醒
     * @param uid 用户id
     * @param pid 文章id
     * @param typeId 消息类型
     */
    @Async("mySimpleAsync")
    public void addStartMessage(Integer uid,Integer pid,Integer typeId){
        User user=userMapper.findUserById(uid);
        Post post=postMapper.findPostById(pid);
        String msg=ReplaceUtil.userReplace(user.getUsername())+"给你的文章"+ReplaceUtil.PostReplace(post.getId(),post.getTitle())+
                "点赞了";
        Message message=new Message();
        message.setTime(new Date());
        message.setType_id(typeId);
        message.setTo_id(post.getUid());
        message.setMessage(msg);
        template.convertAndSend("fanout_msg_exchange","",String.valueOf(message.getTo_id()));
        messageMapper.addMessage(message);
    }

    /**
     * 关注消息的推送
     * from_id 关注了 to_id
     * @param from_id
     * @param to_id
     * @param typeId 消息类型
     */
    @Override
    public void addFollow(Integer from_id, Integer to_id,Integer typeId) {
        Message msg=new Message();
        msg.setTo_id(to_id);
        msg.setType_id(typeId);
        msg.setTime(new Date());
        String username=userMapper.findUserById(from_id).getUsername();
        String message=ReplaceUtil.userReplace(username)+"关注了你";
        msg.setMessage(message);
        template.convertAndSend("fanout_msg_exchange","",String.valueOf(to_id));
        messageMapper.addMessage(msg);
    }

    /**
     * 当前登录用户
     * 是否有消息未读的
     * @return
     */
    @Override
    public boolean exitMessage() {
        int uid=jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        Integer messageCount = messageMapper.messageCount(uid);
        return messageCount>0;
    }

    /**
     * 返回所有的消息类型
     * @return
     */
    @Override
    public List<MessageType> findMessageTypeNum() {
        int uid=jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        List<MessageType> messageTypeNum = messageMapper.findMessageTypeNum(uid);
        return messageTypeNum;
    }

    /**
     * 反回某一类型的所有消息
     * @param id 类型ID
     * @return
     */
    @Override
    public List<Message> findMessageByTypeId(Integer id) {
        int uid=jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        List<Message> messageList = messageMapper.findMessageByTypeId(id,uid);
        messageList.sort((o1, o2) -> {
            if(o1.getStatus()>o2.getStatus())//未读优先
                return 1;
            return (int) (o2.getTime().getTime()-o1.getTime().getTime());
        });
        return messageList;
    }

    /**
     * 修改消息的状态
     * @param id  消息的id
     * @param status 状态
     * @return
     */
    @Override
    public boolean update(Integer id,Integer status) {
        Integer uid=jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        Message message = messageMapper.selectOneByIdAndUid(id, uid);
        if(message!=null){
            messageMapper.updateStatus(id,uid,status);
            return true;
        }
        return false;
    }
}
