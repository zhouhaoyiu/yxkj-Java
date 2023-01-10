package com.zhy.yxkj.domain;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class VerifyWorkJob {
    String workUuid;
    Integer status;
    String spfzr;
    String spfzrInfo;
    String spfzrBase64;
    String verifyOpenId;
    String verifyDate;
}
