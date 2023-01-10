package com.zhy.yxkj.mapper;

import com.zhy.yxkj.domain.WorkJob;
import com.zhy.yxkj.domain.WorkJobMini;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface WorkJobMapper {

    @Select("select workId, workDate, workUuid, status from workjob where sendOpenId = #{sendOpenId} order by workId desc limit #{start}, #{size}")
    List<WorkJobMini> selectWorkJobByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("sendOpenId") String sendOpenId);

    @Select("select * from workjob where verifyOpenId = #{verifyOpenId} order by workId desc limit #{start}, #{size} ")
    List<WorkJobMini> selectVerifyWorkJobByPage(@Param("verifyOpenId") String verifyOpenId, @Param("start") int start, @Param("size") int size);

    @Update("update workjob set spfzr=#{spfzr}, status = #{workStatus}, spfzrInfo = #{spfzrInfo}, spfzrBase64 = #{spfzrBase64}, verifyOpenId = #{verifyOpenId},verifyDate=#{verifyDate} where workUuid=#{workUuid}")
    Integer verifyWorkJob(@Param("spfzr") String spfzr,
                          @Param("workStatus") Integer workStatus,
                          @Param("spfzrInfo") String spfzrInfo,
                          @Param("spfzrBase64") String spfzrBase64,
                          @Param("verifyOpenId") String verifyOpenId,
                          @Param("verifyDate") String verifyDate,
                          @Param("workUuid") String workUuid);

    @Select("select * from workjob where workUuid = #{workUuid}")
    List<WorkJob> getInfoByWorkJobUuid(@Param("workUuid") String uuid);

    @Select("select * from workjob order by workId desc")
    List<WorkJob> adminSelectWorkJobByPage(Integer start, int size);

    @Insert("insert into workjob (workUuid, workDate, workList, zyfzr, jhry, zyry, protectiveMeasureGroups, sendOpenId, status) values (#{workUuid}, #{workDate}, #{workList}, #{zyfzr}, #{jhry}, #{zyry}, #{protectiveMeasureGroups}, #{sendOpenId}, 0)")
    Integer insertWorkJob(
            @Param("workUuid") String uuid,
            @Param("workDate") String workDate,
            @Param("workList") String workList,
            @Param("zyfzr") String zyfzr,
            @Param("jhry") String jhry,
            @Param("zyry") String zyry,
            @Param("protectiveMeasureGroups") String protectiveMeasureGroups,
            @Param("sendOpenId") String sendOpenId
    );

    @Select("select workDate, workUuid,workList from workjob where sendOpenId = #{sendOpenId} and status = 1 order by workId desc limit 0, #{size}")
    List<WorkJobMini> getUnionWorkInfo(@Param("sendOpenId") String sendOpenId, @Param("size") int size);

    @Select("select workList from workjob where workUuid = #{workUuid}")
    String getUnionMissionByWorkUuid(@Param("workUuid") String workUuid);
}
