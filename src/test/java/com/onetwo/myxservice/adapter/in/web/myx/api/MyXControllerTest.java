package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.myxservice.adapter.in.web.config.TestConfig;
import com.onetwo.myxservice.adapter.in.web.myx.mapper.MyXDtoMapper;
import com.onetwo.myxservice.adapter.in.web.myx.request.ConnectMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.UpdateMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.*;
import com.onetwo.myxservice.application.port.in.command.*;
import com.onetwo.myxservice.application.port.in.response.*;
import com.onetwo.myxservice.application.port.in.usecase.*;
import com.onetwo.myxservice.common.GlobalUrl;
import com.onetwo.myxservice.common.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
class MyXControllerTest {

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
    private final String xsUserId = "testMyXUserId";

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 등록 - 성공 테스트")
    void registerMyXSuccessTest() throws Exception {
        //given
        RegisterMyXRequest registerMyXRequest = new RegisterMyXRequest(myXName, myXBirth);
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        RegisterMyXResponseDto registerMyXResponseDto = new RegisterMyXResponseDto(true);
        RegisterMyXResponse registerMyXResponse = new RegisterMyXResponse(registerMyXResponseDto.registerMyXSuccess());

        when(myXDtoMapper.registerRequestToCommand(anyString(), any(RegisterMyXRequest.class))).thenReturn(registerMyXCommand);
        when(registerMyXUseCase.registerMyX(any(RegisterMyXCommand.class))).thenReturn(registerMyXResponseDto);
        when(myXDtoMapper.dtoToRegisterResponse(any(RegisterMyXResponseDto.class))).thenReturn(registerMyXResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 삭제 - 성공 테스트")
    void deleteMyXSuccessTest() throws Exception {
        //given
        DeleteMyXRequest deleteMyXRequest = new DeleteMyXRequest(myXId);
        DeleteMyXCommand deleteMyXCommand = new DeleteMyXCommand(userId, myXId);
        DeleteMyXResponseDto deleteMyXResponseDto = new DeleteMyXResponseDto(true);
        DeleteMyXResponse deleteMyXResponse = new DeleteMyXResponse(deleteMyXResponseDto.isDeleteSuccess());

        when(myXDtoMapper.deleteRequestToCommand(anyString(), any(DeleteMyXRequest.class))).thenReturn(deleteMyXCommand);
        when(deleteMyXUseCase.deleteMyX(any(DeleteMyXCommand.class))).thenReturn(deleteMyXResponseDto);
        when(myXDtoMapper.dtoToDeleteResponse(any(DeleteMyXResponseDto.class))).thenReturn(deleteMyXResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX list 조회 - 성공 테스트")
    void getMyXDetailsSuccessTest() throws Exception {
        //given
        MyXDetailsCommand myXDetailsCommand = new MyXDetailsCommand(userId);

        List<MyXDetailResponseDto> deleteMyXResponseDtoList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            deleteMyXResponseDtoList.add(
                    new MyXDetailResponseDto(
                            i,
                            userId + i,
                            myXName + i,
                            myXBirth,
                            false,
                            null,
                            false
                    )
            );
        }

        MyXDetailsResponse myXDetailsResponse = new MyXDetailsResponse(deleteMyXResponseDtoList);

        when(myXDtoMapper.getMyXDetailsRequestToCommand(anyString())).thenReturn(myXDetailsCommand);
        when(readMyXUseCase.getMyXDetails(any(MyXDetailsCommand.class))).thenReturn(deleteMyXResponseDtoList);
        when(myXDtoMapper.dtoToMyXDetailsResponse(any(List.class))).thenReturn(myXDetailsResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 수정 - 성공 테스트")
    void updateMyXSuccessTest() throws Exception {
        //given
        UpdateMyXRequest updateMyXRequest = new UpdateMyXRequest(myXId, myXName, myXBirth);
        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, myXId, myXName, myXBirth);
        UpdateMyXResponseDto updateMyXResponseDto = new UpdateMyXResponseDto(true);
        UpdateMyXResponse updateMyXResponse = new UpdateMyXResponse(updateMyXResponseDto.updateMyXSuccess());

        when(myXDtoMapper.updateRequestToCommand(anyString(), any(UpdateMyXRequest.class))).thenReturn(updateMyXCommand);
        when(updateMyXUseCase.updateMyX(any(UpdateMyXCommand.class))).thenReturn(updateMyXResponseDto);
        when(myXDtoMapper.dtoToUpdateResponse(any(UpdateMyXResponseDto.class))).thenReturn(updateMyXResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] MyX 연결 - 성공 테스트")
    void connectMyXSuccessTest() throws Exception {
        //given
        ConnectMyXRequest connectMyXRequest = new ConnectMyXRequest(myXId, xsUserId);
        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, myXId, xsUserId);
        ConnectMyXResponseDto connectMyXResponseDto = new ConnectMyXResponseDto(false, true);
        ConnectMyXResponse connectMyXResponse = new ConnectMyXResponse(false, true);

        when(myXDtoMapper.connectRequestToCommand(anyString(), any(ConnectMyXRequest.class))).thenReturn(connectMyXCommand);
        when(connectMyXUseCase.connectMyX(any(ConnectMyXCommand.class))).thenReturn(connectMyXResponseDto);
        when(myXDtoMapper.dtoToConnectResponse(any(ConnectMyXResponseDto.class))).thenReturn(connectMyXResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.MY_X_CONNECT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectMyXRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}