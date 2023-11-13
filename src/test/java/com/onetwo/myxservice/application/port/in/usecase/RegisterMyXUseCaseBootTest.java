package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.common.GlobalStatus;
import com.onetwo.myxservice.common.exceptions.ResourceAlreadyExistsException;
import com.onetwo.myxservice.common.exceptions.ResourceAlreadyFullException;
import com.onetwo.myxservice.domain.MyX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@SpringBootTest
@Transactional
class RegisterMyXUseCaseBootTest {

    @Autowired
    private RegisterMyXUseCase registerMyXUseCase;

    @Autowired
    private RegisterMyXPort registerMyXPort;

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[단위][Use Case] MyX 등록 - 성공 테스트")
    void registerMyXUseCaseSuccessTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);

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

        for (int i = 0; i < GlobalStatus.MAX_MY_X_NUMBER; i++)
            registerMyXPort.registerNewMyX(MyX.createNewMyXByCommand(registerMyXCommand));

        //when then
        Assertions.assertThrows(ResourceAlreadyFullException.class, () -> registerMyXUseCase.registerMyX(registerMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 등록 중복 등록 - 실패 테스트")
    void registerMyXUseCaseMyXIsAlreadyExistFailTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);

        registerMyXPort.registerNewMyX(MyX.createNewMyXByCommand(registerMyXCommand));

        //when then
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> registerMyXUseCase.registerMyX(registerMyXCommand));
    }
}