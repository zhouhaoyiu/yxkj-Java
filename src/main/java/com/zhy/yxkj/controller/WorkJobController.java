package com.zhy.yxkj.controller;

import com.zhy.yxkj.domain.VerifyWorkJob;
import com.zhy.yxkj.domain.WorkJob;
import com.zhy.yxkj.domain.WorkJobMini;
import com.zhy.yxkj.mapper.WorkJobMapper;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    public @NotNull Object addWorkJob(@RequestBody @NotNull WorkJob workJob) {
        String uuid = String.valueOf(UUID.randomUUID());
        int res = workJobMapper.insertWorkJob(
                uuid,
                workJob.getWorkDate(),
                workJob.getWorkList(),
                workJob.getZyfzr(),
                workJob.getJhry(),
                workJob.getZyry(),
                workJob.getProtectiveMeasureGroups(),
                workJob.getSendOpenId()
        );
        JSONObject jsonRes = new JSONObject();
        jsonRes.put("code", 1);
        jsonRes.put("data", res);
        return jsonRes;
    }

    @GetMapping("getVerifyWorkJobByPage")
    public Object getVerifyAllInfo(@NotNull HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        String verifyOpenId = request.getParameter("verifyOpenId");
//        System.out.println("verifyOpenId:  " + verifyOpenId);
        int size = 5;
        int start = (page - 1) * size;
        return workJobMapper.selectVerifyWorkJobByPage(verifyOpenId, start, size);
    }

    @GetMapping("getSendWorkJobByPage")
    public Object getSendAllInfo(@NotNull HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        String sendOpenId = request.getParameter("sendOpenId");
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
        return workJobMapper.getInfoByWorkJobUuid(workUuid);
    }

    @GetMapping("getUnionWork")
    public Object getUnionWork(@NotNull HttpServletRequest request) {
        String sendOpenId = request.getParameter("sendOpenId");
        int size = Integer.parseInt(request.getParameter("size"));
        return workJobMapper.getUnionWorkInfo(sendOpenId, size);
    }

    @GetMapping("getUnionMissionByWorkUuid")
    public Object getUnionMissionByWorkUuid(@NotNull HttpServletRequest request) {
        String workUuid = request.getParameter("workUuid");
        System.out.println(workUuid);
        return workJobMapper.getUnionMissionByWorkUuid(workUuid);
    }


    @PostMapping("verifyWorkJob")
    public Object verifyWorkJob(@RequestBody @NotNull VerifyWorkJob verifyWorkJob) {
        System.out.println(verifyWorkJob);
        int workStatus = verifyWorkJob.getStatus();
        String spfzr = verifyWorkJob.getSpfzr();
        String spfzrInfo = verifyWorkJob.getSpfzrInfo();
        String spfzrBase64 = verifyWorkJob.getSpfzrBase64();
        String verifyOpenId = verifyWorkJob.getVerifyOpenId();
        String verifyDate = verifyWorkJob.getVerifyDate();
        String workUuid = verifyWorkJob.getWorkUuid();

        return workJobMapper.verifyWorkJob(spfzr, workStatus, spfzrInfo, spfzrBase64, verifyOpenId, verifyDate, workUuid);
    }

}
