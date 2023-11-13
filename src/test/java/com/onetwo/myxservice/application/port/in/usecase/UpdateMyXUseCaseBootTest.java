package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.command.UpdateMyXCommand;
import com.onetwo.myxservice.application.port.in.response.UpdateMyXResponseDto;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.common.exceptions.BadRequestException;
import com.onetwo.myxservice.common.exceptions.NotFoundResourceException;
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
class UpdateMyXUseCaseBootTest {

    @Autowired
    private UpdateMyXUseCase updateMyXUseCase;

    @Autowired
    private RegisterMyXPort registerMyXPort;

    private final Long myXId = 1L;
    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 - 성공 테스트")
    void updateMyXUseCaseSuccessTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, savedMyX.getId(), myXName, myXBirth);

        //when then
        Assertions.assertDoesNotThrow(() -> updateMyXUseCase.updateMyX(updateMyXCommand));

        UpdateMyXResponseDto result = updateMyXUseCase.updateMyX(updateMyXCommand);

        Assertions.assertTrue(result.updateMyXSuccess());
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 my x does not exist - 실패 테스트")
    void updateMyXUseCaseMyXDoesNotExistFailTest() {
        //given
        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, myXId, myXName, myXBirth);

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> updateMyXUseCase.updateMyX(updateMyXCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 my x user does not matched - 실패 테스트")
    void updateMyXUseCaseMyXUserDoesNotMatchedFailTest() {
        //given
        String requestUser = "testRequestUser";

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(requestUser, savedMyX.getId(), myXName, myXBirth);

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> updateMyXUseCase.updateMyX(updateMyXCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 my x already connected - 실패 테스트")
    void updateMyXUseCaseMyXAlreadyConnectedFailTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        RegisterMyXCommand connectedMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX connectedMyX = MyX.createNewMyXByCommand(connectedMyXCommand);

        myX.connectMyX(connectedMyX);

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, savedMyX.getId(), myXName, myXBirth);

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> updateMyXUseCase.updateMyX(updateMyXCommand));
    }
}