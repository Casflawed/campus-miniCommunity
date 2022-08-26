package com.flameking.service.impl;


//
//@Transactional
//@Service
//public class UploadServiceImpl implements UploadService {
//
//    final String path=System.getProperty("user.dir")+ "/src/main/resources/static/image/";
//    final String returnName="http://localhost:8888/retwisApi/image";
////
////    public UploadServiceImpl() throws FileNotFoundException {
////    }
//
//
//    @Override
//    public String uploadFile(MultipartFile file)  {
//        Date date=new Date();
//        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//        String pathName =TimeUtil.stampToDay(date.getTime())+UUID.randomUUID().toString()+"."+suffix;
//        File tmpFile=new File(path+pathName);
//        try {
//            file.transferTo(tmpFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return returnName+pathName;
//    }
//}
