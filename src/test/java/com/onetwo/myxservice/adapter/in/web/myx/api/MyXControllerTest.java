package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.myxservice.adapter.in.web.config.TestConfig;
import com.onetwo.myxservice.adapter.in.web.myx.mapper.MyXDtoMapper;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.RegisterMyXResponse;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.in.usecase.RegisterMyXUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
    private MyXDtoMapper myXDtoMapper;

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

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
}