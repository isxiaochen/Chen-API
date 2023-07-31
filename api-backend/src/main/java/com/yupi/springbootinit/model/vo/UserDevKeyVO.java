package com.yupi.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author niuma
 * @create 2023-05-11 14:13
 */
@Data
public class UserDevKeyVO implements Serializable {
    private static final long serialVersionUID = 6703326011663561616L;

   private String accessKey;
   private String secretKey;

}
