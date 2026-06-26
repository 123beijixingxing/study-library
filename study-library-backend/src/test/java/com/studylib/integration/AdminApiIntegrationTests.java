package com.studylib.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminApiIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void loginRefreshMeAndLogoutLifecycleWorks() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(get("/api/admin/v1/auth/me")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.username").value("admin"));

    MvcResult refreshResult = mockMvc.perform(post("/api/admin/v1/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"refreshToken":"%s"}
                """.formatted(tokens.refreshToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.token").isNotEmpty())
        .andReturn();

    String refreshedAccessToken = json(refreshResult).path("data").path("token").asText();

    mockMvc.perform(post("/api/admin/v1/auth/logout")
            .header("Authorization", bearer(refreshedAccessToken)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    mockMvc.perform(get("/api/admin/v1/auth/me")
            .header("Authorization", bearer(refreshedAccessToken)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void editorCannotAccessUserManagementButCanReadMessages() throws Exception {
    AuthTokens tokens = login("editor", "123456");

    mockMvc.perform(get("/api/admin/v1/users")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isForbidden());

    mockMvc.perform(get("/api/admin/v1/messages")
            .param("pageNum", "1")
            .param("pageSize", "2")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.pageNum").value(1))
        .andExpect(jsonPath("$.data.pageSize").value(2));
  }

  @Test
  void editorCannotAccessAuditLogs() throws Exception {
    AuthTokens tokens = login("editor", "123456");

    mockMvc.perform(get("/api/admin/v1/logs/operations")
            .param("pageNum", "1")
            .param("pageSize", "2")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isForbidden());
  }

  @Test
  void operationLogsReturnPagedMetadata() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(get("/api/admin/v1/logs/operations")
            .param("pageNum", "1")
            .param("pageSize", "2")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.pageNum").value(1))
        .andExpect(jsonPath("$.data.pageSize").value(2))
        .andExpect(jsonPath("$.data.total").isNumber())
        .andExpect(jsonPath("$.data.list").isArray());
  }

  @Test
  void loginLogsReturnPagedMetadata() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(get("/api/admin/v1/logs/logins")
            .param("pageNum", "1")
            .param("pageSize", "2")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.pageNum").value(1))
        .andExpect(jsonPath("$.data.pageSize").value(2))
        .andExpect(jsonPath("$.data.total").isNumber())
        .andExpect(jsonPath("$.data.list").isArray());
  }

  @Test
  void deletedCourseEvaluationCanBeQueriedByDeletedStatus() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(patch("/api/admin/v1/course-evaluations/19001/status")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", bearer(tokens.accessToken()))
            .content("""
                {"status":"deleted"}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status").value("deleted"));

    MvcResult deletedList = mockMvc.perform(get("/api/admin/v1/course-evaluations")
            .param("status", "deleted")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode deletedData = json(deletedList).path("data");
    assertThat(deletedData.path("total").asLong()).isGreaterThanOrEqualTo(1L);
    assertThat(deletedData.path("list").toString()).contains("19001");
  }

  @Test
  void deleteCourseHidesItFromListAndDetail() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/courses/2001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    mockMvc.perform(get("/api/admin/v1/courses/2001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isNotFound());

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/courses")
            .param("keyword", "Java Backend Bootcamp")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("total").asLong()).isZero();
    assertThat(data.path("list")).isEmpty();
  }

  @Test
  void deleteQuestionBankHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/question-banks/10001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/question-banks")
            .param("keyword", "题库 1")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("total").asLong()).isZero();
    assertThat(data.path("list")).isEmpty();
  }

  @Test
  void deletePracticePaperHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/practice-papers/11001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/practice-papers")
            .param("keyword", "练习试卷 1")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("total").asLong()).isZero();
    assertThat(data.path("list")).isEmpty();
  }

  @Test
  void deleteMessageHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/messages/3001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/messages")
            .param("keyword", "System Upgrade Notice")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("total").asLong()).isZero();
    assertThat(data.path("list")).isEmpty();
  }

  @Test
  void deleteServiceTicketHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/service-tickets/6001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/service-tickets")
            .param("keyword", "咨询用户1")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("total").asLong()).isZero();
    assertThat(data.path("list")).isEmpty();
  }

  @Test
  void deleteSearchKeywordHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/search/hot-keywords/12001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/search/hot-keywords")
            .param("keyword", "Python 入门")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("total").asLong()).isZero();
    assertThat(data.path("list")).isEmpty();
  }

  @Test
  void deleteRecommendSlotHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/recommend-slots/13001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/recommend-slots")
            .param("pageCode", "home")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("list").toString()).doesNotContain("首页主推荐位");
  }

  @Test
  void deleteMessageTemplateHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/message-templates/14001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/message-templates")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("list").toString()).doesNotContain("系统公告模板");
  }

  @Test
  void deleteSensitiveWordHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/sensitive-words/15001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/sensitive-words")
            .param("keyword", "违规词A")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("total").asLong()).isZero();
    assertThat(data.path("list")).isEmpty();
  }

  @Test
  void deleteMemberPackageHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/member-packages/16001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/member-packages")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("list").toString()).doesNotContain("月卡基础包");
  }

  @Test
  void deleteHomeSectionHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/home/courses/7001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/home/courses")
            .param("sectionType", "recommend")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("list").toString()).doesNotContain("AI 编程入门课");
  }

  @Test
  void deleteGardenContentHidesItFromPagedList() throws Exception {
    AuthTokens tokens = loginAsAdmin();

    mockMvc.perform(delete("/api/admin/v1/contents/8001")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.success").value(true));

    MvcResult listResult = mockMvc.perform(get("/api/admin/v1/contents")
            .param("contentType", "hot")
            .param("pageNum", "1")
            .param("pageSize", "10")
            .header("Authorization", bearer(tokens.accessToken())))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode data = json(listResult).path("data");
    assertThat(data.path("list").toString()).doesNotContain("学习社区");
  }

  private AuthTokens loginAsAdmin() throws Exception {
    return login("admin", "123456");
  }

  private AuthTokens login(String username, String password) throws Exception {
    MvcResult result = mockMvc.perform(post("/api/admin/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"username":"%s","password":"%s"}
                """.formatted(username, password)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.token").isNotEmpty())
        .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
        .andReturn();

    JsonNode data = json(result).path("data");
    return new AuthTokens(data.path("token").asText(), data.path("refreshToken").asText());
  }

  private JsonNode json(MvcResult result) throws Exception {
    return objectMapper.readTree(result.getResponse().getContentAsString());
  }

  private String bearer(String token) {
    return "Bearer " + token;
  }

  private record AuthTokens(String accessToken, String refreshToken) {
  }
}
