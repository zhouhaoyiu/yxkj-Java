package com.zhy.yxkj;

import com.zhy.yxkj.security.*;
import com.zhy.yxkj.controller.WorkJobController;
import com.zhy.yxkj.domain.VerifyWorkJob;
import com.zhy.yxkj.domain.WorkJob;
import com.zhy.yxkj.mapper.WorkJobMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityTests {
    @Test void sessionsRejectMissingForgedAndLoggedOutTokens() {
        Sessions sessions = new Sessions();
        assertNull(sessions.find(null)); assertNull(sessions.find("Bearer invalid"));
        var user = new Accounts.User("worker", "staff");
        String token = sessions.create(user);
        assertEquals(user, sessions.find("Bearer " + token));
        sessions.remove("Bearer " + token); assertNull(sessions.find("Bearer " + token));
    }
    @Test void staffCannotReadOtherOwners() {
        var request = new MockHttpServletRequest();
        request.setAttribute("user", new Accounts.User("alice", "staff"));
        Sessions.owner(request, "alice");
        assertThrows(ResponseStatusException.class, () -> Sessions.owner(request, "bob"));
    }
    @Test void unsignedReviewIsRejectedBeforeDatabaseWrite() {
        var controller = new WorkJobController();
        var mapper = mock(WorkJobMapper.class);
        ReflectionTestUtils.setField(controller, "workJobMapper", mapper);
        var request = new MockHttpServletRequest();
        request.setAttribute("user", new Accounts.User("reviewer", "reviewer"));
        assertThrows(ResponseStatusException.class, () -> controller.verifyWorkJob(new VerifyWorkJob(), request));
        verifyNoInteractions(mapper);
    }
    @Test void reviewerCannotApproveOwnWork() {
        var mapper = mock(WorkJobMapper.class); var controller = new WorkJobController();
        ReflectionTestUtils.setField(controller, "workJobMapper", mapper);
        var work = new WorkJob(); work.setSendOpenId("alice");
        when(mapper.getInfoByWorkJobUuid("work")).thenReturn(List.of(work));
        var body = new VerifyWorkJob(); body.setWorkUuid("work"); body.setStatus(1); body.setSpfzr("Alice"); body.setSpfzrBase64("signature");
        var request = new MockHttpServletRequest(); request.setAttribute("user", new Accounts.User("alice", "reviewer"));
        assertThrows(ResponseStatusException.class, () -> controller.verifyWorkJob(body, request));
    }
    @Test void approvalUsesServerIdentityAndRejectsAlreadyReviewed() {
        var mapper = mock(WorkJobMapper.class); var controller = new WorkJobController();
        ReflectionTestUtils.setField(controller, "workJobMapper", mapper);
        var work = new WorkJob(); work.setSendOpenId("owner");
        when(mapper.getInfoByWorkJobUuid("work")).thenReturn(List.of(work));
        var body = new VerifyWorkJob(); body.setWorkUuid("work"); body.setStatus(1); body.setSpfzr("Reviewer"); body.setSpfzrBase64("signature"); body.setVerifyOpenId("forged");
        var request = new MockHttpServletRequest(); request.setAttribute("user", new Accounts.User("reviewer", "reviewer"));
        assertThrows(ResponseStatusException.class, () -> controller.verifyWorkJob(body, request));
        verify(mapper).verifyWorkJob(eq("Reviewer"), eq(1), isNull(), eq("signature"), eq("reviewer"), anyString(), eq("work"));
    }
}
