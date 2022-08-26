package com.flameking;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;


@SpringBootTest
class RetwisapiApplicationTests {
//    @Autowired
//    PostMapper postMapper;
//    @Autowired
//    RedisTemplate redisTemplate;
//    @Autowired
//    InteractionService interactionService;
//    @Autowired
//    ProfileService profileService;
//    @Autowired
//    UserMapper userMapper;
//    @Autowired
//    MessageMapper messageMapper;
//    @Autowired
//    EmailService emailService;

//    @Autowired
//    EmailUtil emailUtil;
//    @Autowired
//    SerachService serachService;
//    @Autowired
//    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    void contextLoads() throws IOException, InterruptedException {
//        Message message = new Message();
//        message.setType_id(1);
//        message.setTime(new Date());
//        message.setTo_id(2);
//        message.setMessage("<a href='/profile/admin'>admin</a>在<a href='/detail/72'>111111</a>留下了评论");
//        messageMapper.addMessage(message);
//        List<MessageType> messageTypeNum = messageMapper.findMessageTypeNum(3);
//        for(MessageType mt:messageTypeNum){
//            System.out.println(mt);
//        }
//        emailService.sendRegisterEmail("666","3212017338@qq.com");
//
//        emailUtil.sendEmai("66","222","3212017338@qq.com");
//        System.out.println(6666666);
//        serachService.serach("a");

//        boolean b = redisTemplate.delete("123").booleanValue();
//        System.out.println(b);

    }

}
