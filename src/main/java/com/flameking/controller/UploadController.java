//package com.zyh.controller;
//
//import com.zyh.entity.ResultBean;
//import com.zyh.service.UploadService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//public class UploadController {
//    @Autowired
//    UploadService uploadService;
//    @PostMapping("/upload")
//    public String uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {
////        return qiniuService.uploadFile(file.getInputStream());
////        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//        return uploadService.uploadFile(file);
//    }
//
//    @PostMapping("/upAvatar")
//    public ResultBean upAvatar(@RequestParam(value = "file") MultipartFile file)throws IOException{
//        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//        System.out.println(suffix);
//        if(suffix.equals("JPG")||suffix.equals("jpg")){
////            String s = qiniuService.upAvatar(file.getInputStream());
////            return ResultBean.success().add("fileName",s);
//            return ResultBean.success();
//        }
//        return ResultBean.fail("只支持jpg格式的文件");
//    }
//}
