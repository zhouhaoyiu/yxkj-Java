package com.zhy.yxkj.domain;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class WorkJob {
    public Integer workId;
    public String workUuid;
    public String workDate;
    public String workList;
    public String zyfzr;
    public String jhry;
    public String zyry;
    public String protectiveMeasureGroups;
    public String sendOpenId;
    public String spfzr;
    public String spfzrInfo;
    public String spfzrBase64;
    public Integer status;
    public String verifyDate;
}
