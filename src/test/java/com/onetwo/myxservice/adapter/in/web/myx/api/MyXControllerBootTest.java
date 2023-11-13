package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.myxservice.adapter.in.web.config.TestHeader;
import com.onetwo.myxservice.adapter.in.web.myx.request.ConnectMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.UpdateMyXRequest;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.usecase.RegisterMyXUseCase;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.common.GlobalStatus;
import com.onetwo.myxservice.common.GlobalUrl;
import com.onetwo.myxservice.domain.MyX;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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

    @Autowired
    private RegisterMyXUseCase registerMyXUseCase;

    @Autowired
    private RegisterMyXPort registerMyXPort;

    private final String userId = "testUserId";
    private final Long myXId = 1L;
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");
    private final String xsUserId = "testMyXUserId";


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

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] MyX 삭제 - 성공 테스트")
    void deleteMyXSuccessTest() throws Exception {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX newMyX = MyX.createNewMyXByCommand(registerMyXCommand);
        MyX savedMyX = registerMyXPort.registerNewMyX(newMyX);

        DeleteMyXRequest deleteMyXRequest = new DeleteMyXRequest(savedMyX.getId());

        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteMyXRequest))
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-my-x",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                requestFields(
                                        fieldWithPath("myXId").type(JsonFieldType.NUMBER).description("삭제할 My X ID")
                                ),
                                responseFields(
                                        fieldWithPath("isDeleteSuccess").type(JsonFieldType.BOOLEAN).description("삭제 완료 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] MyX list 조회 - 성공 테스트")
    void getMyXDetailsSuccessTest() throws Exception {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        registerMyXUseCase.registerMyX(registerMyXCommand);

        String secondMyXName = "정정이";

        RegisterMyXCommand registerMyXCommandSecond = new RegisterMyXCommand(userId, secondMyXName, myXBirth);
        registerMyXUseCase.registerMyX(registerMyXCommandSecond);

        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-my-x",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                responseFields(
                                        fieldWithPath("myXDetailResponseDtoList[]").type(JsonFieldType.ARRAY).description("My X 리스트"),
                                        fieldWithPath("myXDetailResponseDtoList[].id").type(JsonFieldType.NUMBER).description("My X 고유 id"),
                                        fieldWithPath("myXDetailResponseDtoList[].userId").type(JsonFieldType.STRING).description("My X를 등록한 User id"),
                                        fieldWithPath("myXDetailResponseDtoList[].xsName").type(JsonFieldType.STRING).description("My X의 이름"),
                                        fieldWithPath("myXDetailResponseDtoList[].xsBirth").type(JsonFieldType.STRING).description("My X의 생년월일"),
                                        fieldWithPath("myXDetailResponseDtoList[].isConnected").type(JsonFieldType.BOOLEAN).description("My X 상호 연결 여부"),
                                        fieldWithPath("myXDetailResponseDtoList[].xsUserId").type(JsonFieldType.STRING).description("연결된 My X의 user Id"),
                                        fieldWithPath("myXDetailResponseDtoList[].state").type(JsonFieldType.BOOLEAN).description("My X 삭제 상태")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] MyX 수정 - 성공 테스트")
    void updateMyXSuccessTest() throws Exception {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX newMyX = MyX.createNewMyXByCommand(registerMyXCommand);
        MyX savedMyX = registerMyXPort.registerNewMyX(newMyX);

        UpdateMyXRequest updateMyXRequest = new UpdateMyXRequest(savedMyX.getId(), myXName, myXBirth);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMyXRequest))
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-my-x",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                requestFields(
                                        fieldWithPath("myXId").type(JsonFieldType.NUMBER).description("수정할 My X ID"),
                                        fieldWithPath("xsName").type(JsonFieldType.STRING).description("수정 새로운 My X의 이름"),
                                        fieldWithPath("xsBirth").type(JsonFieldType.STRING).description("수정 새로운 My X의 생년월일")
                                ),
                                responseFields(
                                        fieldWithPath("updateMyXSuccess").type(JsonFieldType.BOOLEAN).description("수정 완료 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] MyX 연결 - 성공 테스트")
    void connectMyXSuccessTest() throws Exception {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX newMyX = MyX.createNewMyXByCommand(registerMyXCommand);
        MyX savedMyX = registerMyXPort.registerNewMyX(newMyX);

        ConnectMyXRequest connectMyXRequest = new ConnectMyXRequest(savedMyX.getId(), xsUserId);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_CONNECT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectMyXRequest))
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("connect-my-x",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                requestFields(
                                        fieldWithPath("myXId").type(JsonFieldType.NUMBER).description("연결할 My X ID"),
                                        fieldWithPath("xsUserId").type(JsonFieldType.STRING).description("연결할 My X의 UserId")
                                ),
                                responseFields(
                                        fieldWithPath("isConnected").type(JsonFieldType.BOOLEAN).description("상호 연결 여부"),
                                        fieldWithPath("isReadyToConnect").type(JsonFieldType.BOOLEAN).description("연결 준비완료 여부")
                                )
                        )
                );
    }
}