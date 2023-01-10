package com.zhy.yxkj.mapper;

import com.zhy.yxkj.domain.RecordJob;
import com.zhy.yxkj.domain.RecordJobMini;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordJobMapper {

    @Select("select jobId,jcjly,jobDate,jobUuid,jobContent from recordjob where sendOpenId = #{sendOpenId} order by jobId desc limit #{start}, #{size}")
    List<RecordJobMini> selectRecordJobByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("sendOpenId") String sendOpenId);


    @Select("select count(*) from recordjob")
    Integer selectRecordJobCount();

    @Select("select * from recordjob where jobUuid = #{jobUuid}")
    List<RecordJob> getInfoByRecordJobUuid(@Param("jobUuid") String uuid);

    @Insert("insert into recordjob(jobUuid,jobContent,jobDate," +
            "jobPosition,riskFactorsValue,safetyDisclosureValue,inspectionEquipmentValue," +
            "safetyProtectionValue,emerRescueValue,otherInfo,ventilationStartsTime,ventilationEndTime," +
            "jobStartTime,jobEndTime,isInterrupt,interruptStartTime,interruptEndTime,confinedSpaceType," +
            "positionList,interruptList,gasDetectionBase64Arr,signBoardBase64Arr,exhaustAirBase64Arr,xcfzr," +
            "jcjly,xcfzrBase64,jcjlyBase64,sendOpenId,unionWorkUuid,unionMissionCId)" +
            " values(#{jobUuid},#{jobContent},#{jobDate},#{jobPosition},#{riskFactorsValue},#{safetyDisclosureValue}," +
            "#{inspectionEquipmentValue},#{safetyProtectionValue},#{emerRescueValue},#{otherInfo},#{ventilationStartsTime}," +
            "#{ventilationEndTime},#{jobStartTime},#{jobEndTime},#{isInterrupt},#{interruptStartTime},#{interruptEndTime}," +
            "#{confinedSpaceType},#{positionList},#{interruptList},#{gasDetectionBase64Arr},#{signBoardBase64Arr},#{exhaustAirBase64Arr}," +
            "#{xcfzr},#{jcjly},#{xcfzrBase64},#{jcjlyBase64},#{sendOpenId},#{unionWorkUuid},#{unionMissionCId})")
    Integer insertRecordJob(
            @Param("jobUuid") String uuid,
            @Param("jobContent") String jobContent,
            @Param("jobDate") String jobDate,
            @Param("jobPosition") String jobPosition,
            @Param("riskFactorsValue") Boolean riskFactorsValue,
            @Param("safetyDisclosureValue") Boolean safetyDisclosureValue,
            @Param("inspectionEquipmentValue") Boolean inspectionEquipmentValue,
            @Param("safetyProtectionValue") Boolean safetyProtectionValue,
            @Param("emerRescueValue") Boolean emerRescueValue,
            @Param("otherInfo") String otherInfo,
            @Param("ventilationStartsTime") String ventilationStartsTime,
            @Param("ventilationEndTime") String ventilationEndTime,
            @Param("jobStartTime") String jobStartTime,
            @Param("jobEndTime") String jobEndTime,
            @Param("isInterrupt") Boolean isInterrupt,
            @Param("interruptStartTime") String interruptStartTime,
            @Param("interruptEndTime") String interruptEndTime,
            @Param("confinedSpaceType") Boolean confinedSpaceType,
            @Param("positionList") String positionList,
            @Param("interruptList") String interruptList,
            @Param("gasDetectionBase64Arr") String gasDetectionBase64Arr,
            @Param("signBoardBase64Arr") String signBoardBase64Arr,
            @Param("exhaustAirBase64Arr") String exhaustAirBase64Arr,

            @Param("xcfzr") String xcfzr,
            @Param("jcjly") String jcjly,

            @Param("xcfzrBase64") String xcfzrBase64,
            @Param("jcjlyBase64") String jcjlyBase64,
            @Param("sendOpenId") String sendOpenId,
            @Param("unionWorkUuid") String unionWorkUuid,
            @Param("unionMissionCId") Integer unionMissionCId
    );

    @Select("select jobId,jcjly,jobDate,jobUuid,jobContent from recordjob order by jobId desc")
    List<RecordJobMini> adminSelectRecordJobByPage(@Param("start") Integer start, @Param("size") Integer size);
}
