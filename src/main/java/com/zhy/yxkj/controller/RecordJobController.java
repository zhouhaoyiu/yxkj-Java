package com.zhy.yxkj.controller;

import com.zhy.yxkj.domain.RecordJob;
import com.zhy.yxkj.mapper.RecordJobMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author ZHY
 */
@RequestMapping("recordJob")
@RestController
public class RecordJobController {

    @Resource
    private RecordJobMapper recordJobMapper;

    @GetMapping("getUuid")
    public Object getUuid() {
        return UUID.randomUUID().toString();
    }

    @GetMapping("adminGetSendRecordJobByPage")
    public Object adminGetSendAllInfo(@NotNull HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        int size = 100;
        int start = (page - 1) * size;
        System.out.println(page);
        return recordJobMapper.adminSelectRecordJobByPage(start, size);
    }

    @GetMapping("getSendRecordJobByPage")
    public Object getSendAllInfo(@NotNull HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        String sendOpenId = request.getParameter("sendOpenId");
//        System.out.println(sendOpenId);
        int size = 5;
        int start = (page - 1) * size;
        return recordJobMapper.selectRecordJobByPage(start, size, sendOpenId);
    }


    @GetMapping("getInfoByRecordJobUuid")
    public Object getInfoByJobUuid(@NotNull HttpServletRequest request) {
        String jobUuid = request.getParameter("jobUuid");
        return recordJobMapper.getInfoByRecordJobUuid(jobUuid);
    }


    @PostMapping("addRecordJob")
    public @NotNull Object addJob(@RequestBody @NotNull RecordJob recordJob) {
        String uuid = String.valueOf(UUID.randomUUID());

        return recordJobMapper.insertRecordJob(
                uuid,
                recordJob.getJobContent(),
                recordJob.getJobDate(),
                recordJob.getJobPosition(),
                recordJob.getRiskFactorsValue(),
                recordJob.getSafetyDisclosureValue(),
                recordJob.getInspectionEquipmentValue(),
                recordJob.getSafetyProtectionValue(),
                recordJob.getEmerRescueValue(),
                recordJob.getOtherInfo(),
                recordJob.getVentilationStartsTime(),
                recordJob.getVentilationEndTime(),
                recordJob.getJobStartTime(),
                recordJob.getJobEndTime(),
                recordJob.getIsInterrupt(),
                recordJob.getInterruptStartTime(),
                recordJob.getInterruptEndTime(),
                recordJob.getConfinedSpaceType(),
                recordJob.getPositionList(),
                recordJob.getInterruptList(),
                recordJob.getGasDetectionBase64Arr(),
                recordJob.getSignBoardBase64Arr(),
                recordJob.getExhaustAirBase64Arr(),

                recordJob.getXcfzr(),
                recordJob.getJcjly(),

                recordJob.getXcfzrBase64(),
                recordJob.getJcjlyBase64(),

                recordJob.getSendOpenId(),
                recordJob.getUnionWorkUuid(),
                recordJob.getUnionMissionCId()
        );
    }
}
