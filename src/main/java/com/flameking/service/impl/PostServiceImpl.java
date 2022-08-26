package com.flameking.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.flameking.entity.Post;
import com.flameking.entity.PostDetai;
import com.flameking.mapper.EditPostMapper;
import com.flameking.mapper.PostMapper;
import com.flameking.service.MentionService;
import com.flameking.service.PostService;
import com.flameking.service.UserService;
import com.flameking.utils.JWTUtil;
import com.flameking.utils.PatternUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserService userService;
    @Autowired
    MentionService mentionService;
    @Autowired
    EditPostMapper editPostMapper;
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 发布文章
     * @param post
     * @return
     */
    @Override
    public int addPost(Post post) {
        post.setCreate_time(new Date());
        post.setUid(jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
        Set<String> name = PatternUtil.findName(post.getContent());
        Set<String> newSet=new HashSet<>();
        for(String username:name){
            if(userService.exists(username)==1)
            {
                newSet.add(username);
            }
        }
        String content="";
        content=post.getContent();
        String replace = PatternUtil.replace(content, newSet);
        //替换变成超链接
        post.setContent(replace);
        postMapper.addPost(post);
        if(newSet.size()!=0) {//如果有@ 并且@的用户名存在
            //添加mention信息
            mentionService.addMention(post,newSet);
            editPostMapper.insert(post.getId(),content);
        }

        return post.getId();
    }

    /**
     * 找出所有文章
     * @param page 分页 页数
     * @param size 一页多少条
     * @return
     */
    @Override
    public PageInfo findAll(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<PostDetai> postDetais=postMapper.findAll();
        for(PostDetai p:postDetais){
            p=getDetai(p);
        }
        PageInfo pageInfo=new PageInfo(postDetais);
        return pageInfo;
    }

    /**
     * 通过文章id找出文章
     * @param id
     * @return
     */
    @Override
    public PostDetai findPostById(Integer id) {
        PostDetai p=postMapper.findPostById(id);
        if(p==null)
            return null;
        p=getDetai(p);
        return p;
    }

    /**
     * 找出某一类型的文章
     *
     * @param id
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo findTypeById(Integer id, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<PostDetai> postDetais=postMapper.findTypeById(id);

        for(PostDetai p:postDetais){
           p=getDetai(p);
        }
        PageInfo pageInfo=new PageInfo(postDetais);
        return pageInfo;
    }

    /**
     * 修改文章
     * @param post
     */
    @Override
    public void update(Post post) {
        int userId= jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        //只有作者才能修改
        if(userId!=post.getUid())
            throw new ShiroException();
        Set<String> name = PatternUtil.findName(post.getContent());
        Set<String> newSet=new HashSet<>();
        for(String username:name){
            if(userService.exists(username)==1)
            {
                newSet.add(username);
            }
        }
        String content="";
        if(newSet.size()!=0) {
            //添加mention信息
            mentionService.addMention(post,newSet);
            content=post.getContent();
            String replace = PatternUtil.replace(content, newSet);
            //替换变成超链接
            post.setContent(replace);
        }
        if(!content.equals("")){
            String oldContent = editPostMapper.findContentById(post.getId());
            if(oldContent!=null&&!oldContent.equals(""))
                editPostMapper.update(post.getId(),content);
            else
                editPostMapper.insert(post.getId(),content);
        }
        postMapper.update(post);
    }

    /**
     * 找出某用户的发布的所有文章
     * @param id
     * @return
     */
    @Override
    public List<PostDetai> findPostByUserId(Integer id) {
        List<PostDetai> postDetais=postMapper.findPostByUserId(id);
        if(postDetais.size()==0||postDetais==null)
            return null;
        for(PostDetai p:postDetais){
            p=getDetai(p);
        }
        return postDetais;
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        Post p=findPostById(id);
        //只有作者本人才能删除
        if(p.getUid().equals(jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()))){
            p.setStatus(0);
            postMapper.update(p);
            return true;
        }
        return false;
    }

    /**
     * 修改文章，回显
     * @param id
     * @return
     */
    @Override
    public PostDetai findPostByIds(Integer id) {
        PostDetai p=postMapper.findPostById(id);
        String content = editPostMapper.findContentById(id);
        if(content!=null&&!content.equals(""))
            p.setContent(content);
        return p;
    }

    /**
     * 找出关注的人发布的文章
     * @param uidList 关注的用户id集合
     * @return
     */
    @Override
    public List<PostDetai> findFollowingPost(List<Integer> uidList) {
        List<PostDetai> followingPost = postMapper.findFollowingPost(uidList);
        for(PostDetai p:followingPost){
            getDetai(p);
        }
        return followingPost;
    }

    /**
     * 查找文章
     * @param content 关键字
     * @return
     */
    @Override
    public List<PostDetai> serachPost(String content) {
        List<PostDetai> posts = postMapper.serachPost("%" + content + "%");
        for(PostDetai p:posts){
            getDetai(p);
        }
        return posts;
    }

    /**
     * 转发
     * @param id 要转发的这篇文章的ID
     * @return
     */
    @Override
    public int rePost(Integer id) {
        int userId= jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        Post postById = postMapper.findPostById(id);
        postById.setAuthor(0);
        postById.setUid(userId);
        postById.setCreate_time(new Date());
        postById.setId(null);
        postMapper.rePost(postById);
        //System.out.println(postById);
        return postById.getId();
    }

    @Override
    public Boolean changeStatus(Integer id) {
        PostDetai postById = postMapper.findPostById(id);
        if(postById==null)
            return false;
        if(postById.getStatus()==1)
            postMapper.changeStatus(postById.getId(),0);
        else
            postMapper.changeStatus(postById.getId(),1);
        return true;
    }

    /**
     * 获取文章的详细信息 收藏、点赞这些
     * @param p
     * @return
     */
    public PostDetai getDetai(PostDetai p){
        int userId= jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        p.setStarts(redisTemplate.opsForSet().size("post:" + p.getId() + ":start").intValue());
        p.setStart(redisTemplate.opsForSet().isMember("post:" + p.getId() + ":start",userId).booleanValue());
        p.setCollects(redisTemplate.opsForSet().size("post:" + p.getId() + ":collect").intValue());
        p.setCollect(redisTemplate.opsForSet().isMember("post:" + p.getId() + ":collect",userId).booleanValue());
        return p;
    }


}
