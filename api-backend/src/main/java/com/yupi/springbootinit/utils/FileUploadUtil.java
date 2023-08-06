package com.yupi.springbootinit.utils;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author niuma
 * @create 2023-06-04 15:39
 */
public class FileUploadUtil {
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");
    private static final int THREE_M = 3 * 1024 * 1024;

    public static String uploadFileAvatar(MultipartFile file) {

        if(!validate(file)){
            return null;
        }

        //工具类获取值
        String endpoint = OOSConstantPropertiesUtils.END_POIND;
        String accessKeyId = OOSConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = OOSConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = OOSConstantPropertiesUtils.BUCKET_NAME;


        InputStream inputStream = null;


        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 获取上传文件的输入流
            inputStream = file.getInputStream();


            //获取文件名称
            String fileName = file.getOriginalFilename();
            //添加随机值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;


            //把文件按照日期分类
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接日期
            fileName = datePath + "/" + fileName;


            //调用oss实例中的方法实现上传
            //参数1： Bucket名称
            //参数2： 上传到oss文件路径和文件名称 /aa/bb/1.jpg
            //参数3： 上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //https://achang-edu.oss-cn-hangzhou.aliyuncs.com/default.gif
            String url = "http://" + bucketName + "." + endpoint + "/" + fileName;

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validate(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        // 文件大小小于3m
        if (file.getSize() > THREE_M) {
            return false;
        }
        // 文件必须是图片
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            return false;
        }

        return true;
    }
}
