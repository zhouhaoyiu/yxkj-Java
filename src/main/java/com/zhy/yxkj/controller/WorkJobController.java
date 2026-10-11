package com.zhy.yxkj.controller;

import com.zhy.yxkj.domain.VerifyWorkJob;
import com.zhy.yxkj.domain.WorkJob;
import com.zhy.yxkj.domain.WorkJobMini;
import com.zhy.yxkj.mapper.WorkJobMapper;
import com.zhy.yxkj.security.Sessions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 */
@RequestMapping("workJob")
@RestController
public class WorkJobController {
    @Resource
    private WorkJobMapper workJobMapper;

    @GetMapping("adminGetSendWorkJobByPage")
    public Object adminGetSendAllInfo(@NotNull HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        int size = 100;
        int start = (page - 1) * size;
        return workJobMapper.adminSelectWorkJobByPage(start, size);
    }

    @PostMapping("addWorkJob")
    public @NotNull Object addWorkJob(@RequestBody @NotNull WorkJob workJob, HttpServletRequest request) {
        if (workJob.getWorkDate() == null || workJob.getWorkList() == null || workJob.getZyfzr() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        String uuid = String.valueOf(UUID.randomUUID());
        int res = workJobMapper.insertWorkJob(
                uuid,
                workJob.getWorkDate(),
                workJob.getWorkList(),
                workJob.getZyfzr(),
                workJob.getJhry(),
                workJob.getZyry(),
                workJob.getProtectiveMeasureGroups(),
                Sessions.user(request).id()
        );
        Map<String, Object> jsonRes = new HashMap<>();
        jsonRes.put("code", 1);
        jsonRes.put("data", res);
        return jsonRes;
    }

    @GetMapping("getVerifyWorkJobByPage")
    public Object getVerifyAllInfo(@NotNull HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        String verifyOpenId = Sessions.user(request).id();
//        System.out.println("verifyOpenId:  " + verifyOpenId);
        int size = 5;
        int start = (page - 1) * size;
        return workJobMapper.selectVerifyWorkJobByPage(verifyOpenId, start, size);
    }

    @GetMapping("getSendWorkJobByPage")
    public Object getSendAllInfo(@NotNull HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        String sendOpenId = Sessions.user(request).id();
//        System.out.println(sendOpenId);
        int size = 5;
        int start = (page - 1) * size;
//        System.out.println(start);
//        System.out.println(size);
//        System.out.println(sendOpenId);
        //        System.out.println(res.toString());
        return workJobMapper.selectWorkJobByPage(start, size, sendOpenId);
    }

    @GetMapping("getInfoByWorkJobUuid")
    public Object getInfoByJobUuid(@NotNull HttpServletRequest request) {
        String workUuid = request.getParameter("workUuid");
        var rows = workJobMapper.getInfoByWorkJobUuid(workUuid);
        if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Sessions.owner(request, rows.getFirst().getSendOpenId());
        return rows;
    }

    @GetMapping("getUnionWork")
    public Object getUnionWork(@NotNull HttpServletRequest request) {
        String sendOpenId = Sessions.user(request).id();
        int size = Math.max(1, Math.min(100, Integer.parseInt(request.getParameter("size"))));
        return workJobMapper.getUnionWorkInfo(sendOpenId, size);
    }

    @GetMapping("getUnionMissionByWorkUuid")
    public Object getUnionMissionByWorkUuid(@NotNull HttpServletRequest request) {
        String workUuid = request.getParameter("workUuid");
        var rows = workJobMapper.getInfoByWorkJobUuid(workUuid);
        if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Sessions.owner(request, rows.getFirst().getSendOpenId());
        return workJobMapper.getUnionMissionByWorkUuid(workUuid);
    }


    @PostMapping("verifyWorkJob")
    public Object verifyWorkJob(@RequestBody @NotNull VerifyWorkJob verifyWorkJob, HttpServletRequest request) {
        var user = Sessions.user(request);
        if (!user.manages()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (verifyWorkJob.getSpfzr() == null || verifyWorkJob.getSpfzr().isBlank() || verifyWorkJob.getSpfzrBase64() == null || verifyWorkJob.getSpfzrBase64().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Signature required");
        if (verifyWorkJob.getStatus() == null || (verifyWorkJob.getStatus() != 1 && verifyWorkJob.getStatus() != 2)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        var rows = workJobMapper.getInfoByWorkJobUuid(verifyWorkJob.getWorkUuid());
        if (rows.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (user.id().equals(rows.getFirst().getSendOpenId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Separate reviewer required");
        int workStatus = verifyWorkJob.getStatus();
        String spfzr = verifyWorkJob.getSpfzr();
        String spfzrInfo = verifyWorkJob.getSpfzrInfo();
        String spfzrBase64 = verifyWorkJob.getSpfzrBase64();
        String verifyOpenId = user.id();
        String verifyDate = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Shanghai")).toString();
        String workUuid = verifyWorkJob.getWorkUuid();

        int updated = workJobMapper.verifyWorkJob(spfzr, workStatus, spfzrInfo, spfzrBase64, verifyOpenId, verifyDate, workUuid);
        if (updated != 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "Already reviewed");
        return updated;
    }

}
