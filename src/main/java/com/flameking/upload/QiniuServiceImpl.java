package com.flameking.upload;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.flameking.constant.ApplicationConstant;
import com.flameking.entity.User;
import com.flameking.mapper.UserMapper;
import com.flameking.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Service
@EnableConfigurationProperties(QiniuProperties.class)
public class QiniuServiceImpl implements QiniuService, ApplicationConstant, InitializingBean {

    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BucketManager bucketManager;
    @Autowired
    private Auth auth;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private JWTUtil jwtUtil;

    // 定义七牛云上传的相关策略
    private StringMap putPolicy;

    /**
     * 以文件的形式上传
     *
     * @param file
     * @return
     * @throws QiniuException
     */
    public String uploadFile(File file) throws QiniuException {
        Response response = this.uploadManager.put(file, null, getUploadToken());
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(file, null, getUploadToken());
            retry++;
        }
        //解析结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String return_path = PROTOCOL + "://" + qiniuProperties.getPrefix() + "/" + putRet.key;
        log.info("文件名称={}", return_path);
        return return_path;
    }

    /**
     * 以流的形式上传
     *
     * @param inputStream
     * @return
     * @throws QiniuException
     */
    @Override
    public String uploadFile(InputStream inputStream) throws QiniuException {
        Response response = this.uploadManager.put(inputStream, null, getUploadToken(), null, null);
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(inputStream, null, getUploadToken(), null, null);
            retry++;
        }
        //解析结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String return_path = PROTOCOL + "://" + qiniuProperties.getPrefix() + "/" + putRet.key;
        log.info("文件名称={}", return_path);
        return return_path;
    }

    /**
     * 删除七牛云上的相关文件
     *
     * @param key
     * @return
     * @throws QiniuException
     */
    @Override
    public Response delete(String key) throws QiniuException {
        Response response = bucketManager.delete(qiniuProperties.getBucket(), key);
        int retry = 0;
        while (response.needRetry() && retry++ < 3) {
            response = bucketManager.delete(qiniuProperties.getBucket(), key);
        }
        return response;
    }

    /**
     * 数据流上传头像
     * @param inputStream
     * @return
     * @throws QiniuException
     */
    @Override
    public String upAvatar(InputStream inputStream) throws QiniuException {
        Integer uid=jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        //上传之前获取上传凭证
        String uploadToken = getUploadToken();
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Response response = this.uploadManager.put(inputStream, null, uploadToken, null, null);
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(inputStream, null, uploadToken, null, null);
            retry++;
        }
        //解析结果
        MyPutRet putRet = JSON.parseObject(response.bodyString(), MyPutRet.class);
        String url = PROTOCOL + "://" + qiniuProperties.getPrefix() + "/" + putRet.key;
        User user=new User();
        user.setId(uid);
        user.setAvatar(url);
        userMapper.updateUserInfo(user);
        return url;
    }

    /**
     * 上传策略：自定义上传后返回内容；返回自定义参数，需要设置 x:参数名称，
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
    }

    /**
     * 获取上传凭证
     *
     * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(qiniuProperties.getBucket(), null, 3600, putPolicy);
    }
}