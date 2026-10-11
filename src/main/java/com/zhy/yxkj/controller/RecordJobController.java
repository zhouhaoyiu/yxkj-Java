package com.zhy.yxkj.controller;

import com.zhy.yxkj.domain.RecordJob;
import com.zhy.yxkj.mapper.RecordJobMapper;
import com.zhy.yxkj.mapper.WorkJobMapper;
import com.zhy.yxkj.security.Sessions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author ZHY
 */
@RequestMapping("recordJob")
@RestController
public class RecordJobController {

    @Resource
    private RecordJobMapper recordJobMapper;
    @Resource private WorkJobMapper workJobMapper;

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
        String sendOpenId = Sessions.user(request).id();
//        System.out.println(sendOpenId);
        int size = 5;
        int start = (page - 1) * size;
        return recordJobMapper.selectRecordJobByPage(start, size, sendOpenId);
    }


    @GetMapping("getInfoByRecordJobUuid")
    public Object getInfoByJobUuid(@NotNull HttpServletRequest request) {
        String jobUuid = request.getParameter("jobUuid");
        var rows = recordJobMapper.getInfoByRecordJobUuid(jobUuid);
        if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Sessions.owner(request, rows.getFirst().getSendOpenId());
        return rows;
    }


    @PostMapping("addRecordJob")
    @Transactional
    public @NotNull Object addJob(@RequestBody @NotNull RecordJob recordJob, HttpServletRequest request) {
        if (recordJob.getJobUuid() == null || !recordJob.getJobUuid().matches("[A-Za-z0-9_-]{16,64}")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID required");
        if (recordJob.getXcfzrBase64() == null || recordJob.getXcfzrBase64().isBlank() || recordJob.getJcjlyBase64() == null || recordJob.getJcjlyBase64().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Signatures required");
        if (!Boolean.TRUE.equals(recordJob.getRiskFactorsValue()) || !Boolean.TRUE.equals(recordJob.getSafetyDisclosureValue()) || !Boolean.TRUE.equals(recordJob.getInspectionEquipmentValue()) || !Boolean.TRUE.equals(recordJob.getSafetyProtectionValue()) || !Boolean.TRUE.equals(recordJob.getEmerRescueValue())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Safety checks incomplete");
        var work = workJobMapper.lockWork(recordJob.getUnionWorkUuid());
        if (work.isEmpty() || !Integer.valueOf(1).equals(work.getFirst().getStatus())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Approved work required");
        Sessions.owner(request, work.getFirst().getSendOpenId());
        String uuid = recordJob.getJobUuid();
        recordJob.setSendOpenId(Sessions.user(request).id());
        var existing = recordJobMapper.getInfoByRecordJobUuid(uuid);
        if (!existing.isEmpty()) {
            var previous = existing.getFirst();
            previous.setJobId(null); recordJob.setJobId(null);
            if (!previous.equals(recordJob)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Request ID already used");
            return 1;
        }

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

                Sessions.user(request).id(),
                recordJob.getUnionWorkUuid(),
                recordJob.getUnionMissionCId()
        );
    }
}
