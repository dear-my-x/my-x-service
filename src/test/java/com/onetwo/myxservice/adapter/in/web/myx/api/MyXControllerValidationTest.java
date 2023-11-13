package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.myxservice.adapter.in.web.config.TestConfig;
import com.onetwo.myxservice.adapter.in.web.myx.mapper.MyXDtoMapper;
import com.onetwo.myxservice.adapter.in.web.myx.request.ConnectMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.UpdateMyXRequest;
import com.onetwo.myxservice.application.port.in.usecase.*;
import com.onetwo.myxservice.common.GlobalUrl;
import com.onetwo.myxservice.common.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MyXController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
@Import(TestConfig.class)
class MyXControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterMyXUseCase registerMyXUseCase;

    @MockBean
    private DeleteMyXUseCase deleteMyXUseCase;

    @MockBean
    private ReadMyXUseCase readMyXUseCase;

    @MockBean
    private UpdateMyXUseCase updateMyXUseCase;

    @MockBean
    private ConnectMyXUseCase connectMyXUseCase;

    @MockBean
    private MyXDtoMapper myXDtoMapper;

    private final String userId = "testUserId";
    private final Long myXId = 1L;
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 등록 my x name validation fail - 실패 테스트")
    void registerMyXNameValidationFailTest(String testMyXName) throws Exception {
        //given
        RegisterMyXRequest registerMyXRequest = new RegisterMyXRequest(testMyXName, myXBirth);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullSource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 등록 my x brith validation fail - 실패 테스트")
    void registerMyXBirthValidationFailTest(Instant testMyXBirth) throws Exception {
        //given
        RegisterMyXRequest registerMyXRequest = new RegisterMyXRequest(myXName, testMyXBirth);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullSource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 삭제 my x name validation fail - 실패 테스트")
    void deleteMyXNameValidationFailTest(Long testMyXId) throws Exception {
        //given
        DeleteMyXRequest registerMyXRequest = new DeleteMyXRequest(testMyXId);

        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 수정 my x name validation fail - 실패 테스트")
    void updateMyXNameValidationFailTest(String testMyXName) throws Exception {
        //given
        UpdateMyXRequest updateMyXRequest = new UpdateMyXRequest(myXId, testMyXName, myXBirth);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullSource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 수정 my x brith validation fail - 실패 테스트")
    void updateMyXBirthValidationFailTest(Instant testMyXBirth) throws Exception {
        //given
        UpdateMyXRequest updateMyXRequest = new UpdateMyXRequest(myXId, myXName, testMyXBirth);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullSource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 수정 my x name validation fail - 실패 테스트")
    void updateMyXNameValidationFailTest(Long testMyXId) throws Exception {
        //given
        UpdateMyXRequest updateMyXRequest = new UpdateMyXRequest(testMyXId, myXName, myXBirth);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 연결 xs user id validation fail - 실패 테스트")
    void connectXsUserIdValidationFailTest(String testXsUserId) throws Exception {
        //given
        ConnectMyXRequest connectMyXRequest = new ConnectMyXRequest(myXId, testXsUserId);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_CONNECT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullSource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 연결 my x id validation fail - 실패 테스트")
    void connectMyXIdValidationFailTest(Long testMyXId) throws Exception {
        //given
        ConnectMyXRequest connectMyXRequest = new ConnectMyXRequest(testMyXId, userId);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_CONNECT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }
}