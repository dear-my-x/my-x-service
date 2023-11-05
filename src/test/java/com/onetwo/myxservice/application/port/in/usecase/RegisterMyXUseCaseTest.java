package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.out.MyXRegisterEventPublisherPort;
import com.onetwo.myxservice.application.port.out.ReadMyXPort;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.application.service.converter.MyXUseCaseConverter;
import com.onetwo.myxservice.application.service.service.MyXService;
import com.onetwo.myxservice.common.GlobalStatus;
import com.onetwo.myxservice.common.exceptions.ResourceAlreadyExistsException;
import com.onetwo.myxservice.common.exceptions.ResourceAlreadyFullException;
import com.onetwo.myxservice.domain.MyX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RegisterMyXUseCaseTest {

    @InjectMocks
    private MyXService registerMyXUseCase;

    @Mock
    private ReadMyXPort readMyXPort;

    @Mock
    private RegisterMyXPort registerMyXPort;

    @Mock
    private MyXUseCaseConverter myXUseCaseConverter;

    @Mock
    private MyXRegisterEventPublisherPort myXRegisterEventPublisherPort;

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[단위][Use Case] MyX 등록 - 성공 테스트")
    void registerMyXUseCaseSuccessTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);
        RegisterMyXResponseDto registerMyXResponseDto = new RegisterMyXResponseDto(true);

        given(readMyXPort.findByUserId(userId)).willReturn(Collections.emptyList());
        given(registerMyXPort.registerNewMyX(any(MyX.class))).willReturn(myX);
        given(myXUseCaseConverter.myXToRegisterMyXResponseDto(any(MyX.class))).willReturn(registerMyXResponseDto);
        //when
        RegisterMyXResponseDto result = registerMyXUseCase.registerMyX(registerMyXCommand);

        //then
        Assertions.assertTrue(result.registerMyXSuccess());
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 등록 수 제한 초과 - 실패 테스트")
    void registerMyXUseCaseMyXIsFullFailTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);

        List<MyX> myXList = new ArrayList<>();
        for (int i = 0; i < GlobalStatus.MAX_MY_X_NUMBER; i++)
            myXList.add(MyX.createNewMyXByCommand(registerMyXCommand));

        given(readMyXPort.findByUserId(userId)).willReturn(myXList);

        //when then
        Assertions.assertThrows(ResourceAlreadyFullException.class, () -> registerMyXUseCase.registerMyX(registerMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 등록 중복 등록 - 실패 테스트")
    void registerMyXUseCaseMyXIsAlreadyExistFailTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);

        List<MyX> myXList = new ArrayList<>();
        myXList.add(MyX.createNewMyXByCommand(registerMyXCommand));

        given(readMyXPort.findByUserId(userId)).willReturn(myXList);

        //when then
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> registerMyXUseCase.registerMyX(registerMyXCommand));
    }
}