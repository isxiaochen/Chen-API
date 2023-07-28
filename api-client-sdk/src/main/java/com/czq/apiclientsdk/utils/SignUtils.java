package com.czq.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具
 */
public class SignUtils {


    private static final Digester md5 = new Digester(DigestAlgorithm.SHA1);
    /**
     * 生成签名算法
     * @param body
     * @param secretKey
     * @return
     */
    public static String generateSign(String body,String secretKey){
        return md5.digestHex(body+secretKey);
    }
}
