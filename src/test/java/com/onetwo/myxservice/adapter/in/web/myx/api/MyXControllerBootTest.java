package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.myxservice.adapter.in.web.config.TestHeader;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.common.GlobalStatus;
import com.onetwo.myxservice.common.GlobalUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestHeader.class)
class MyXControllerBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestHeader testHeader;

    private static final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");


    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] MyX 등록 - 성공 테스트")
    void registerMyXSuccessTest() throws Exception {
        //given
        RegisterMyXRequest registerMyXRequest = new RegisterMyXRequest(myXName, myXBirth);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMyXRequest))
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("register-my-x",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                requestFields(
                                        fieldWithPath("xsName").type(JsonFieldType.STRING).description("등록할 X의 이름"),
                                        fieldWithPath("xsBirth").type(JsonFieldType.STRING).description("등록할 X의 생년월일")
                                ),
                                responseFields(
                                        fieldWithPath("registerMyXSuccess").type(JsonFieldType.BOOLEAN).description("등록 완료 여부")
                                )
                        )
                );
    }
}