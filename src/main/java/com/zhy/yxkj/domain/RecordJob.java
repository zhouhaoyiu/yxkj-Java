package com.zhy.yxkj.domain;


import lombok.Data;

/**
 * @author ZHY
 */
@Data
public class RecordJob {
    public Integer jobId;
    public String jobUuid;
    public String jobContent;
    public String jobDate;
    public String jobPosition;
    public Boolean riskFactorsValue;
    public Boolean safetyDisclosureValue;
    public Boolean inspectionEquipmentValue;
    public Boolean safetyProtectionValue;
    public Boolean emerRescueValue;
    public String otherInfo;
    public String ventilationStartsTime;
    public String ventilationEndTime;
    public String jobStartTime;
    public String jobEndTime;
    public Boolean isInterrupt;
    public String interruptStartTime;
    public String interruptEndTime;
    public Boolean confinedSpaceType;
    public String positionList;
    public String interruptList;
    public String gasDetectionBase64Arr;
    public String signBoardBase64Arr;
    public String exhaustAirBase64Arr;
    public String xcfzr;
    public String jcjly;
    public String xcfzrBase64;
    public String jcjlyBase64;
    public String sendOpenId;

}
