package com.flameking.upload;

import com.google.gson.Gson;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QiniuProperties.class)
public class QiniuFileConfig {

    @Autowired
    private QiniuProperties qiniuProperties;

    /**
     * 七牛云上传配置
     * @return
     */
    @Bean
    public com.qiniu.storage.Configuration getQiniuCfg() {
        //配置存储空间对应的区域，比如当前我的存储空间的区域是华南-广东
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.huanan());
        //设置请求协议为http
        cfg.useHttpsDomains = false;
        return cfg;
//        return new com.qiniu.storage.Configuration(Zone.zone2());
    }

    /**
     * 构建一个七牛上传工具实例
     * @param cfg
     * @return
     */
    @Bean
    public UploadManager getUploadManager(com.qiniu.storage.Configuration cfg) {
        return new UploadManager(cfg);
    }

    /**
     * 认证信息实例，用于生成上传凭证uploadToken
     * @return
     */
    @Bean
    public Auth auth() {
        return Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager(com.qiniu.storage.Configuration cfg) {
        return new BucketManager(auth(), cfg);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}