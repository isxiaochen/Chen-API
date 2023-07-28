package com.czq.apiorder.model.dto;


import com.czq.apicommon.common.PageRequest;
import lombok.Data;

import java.io.Serializable;


@Data
public class OrderQueryRequest extends PageRequest implements Serializable {
    private String type;
}
