package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.ConnectMyXCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.ConnectMyXResponseDto;
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
class ConnectMyXUseCaseBootTest {

    @Autowired
    private ConnectMyXUseCase connectMyXUseCase;

    @Autowired
    private RegisterMyXPort registerMyXPort;

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");
    private final String xsUserId = "testMyXUserId";

    @Test
    @DisplayName("[통합][Use Case] MyX 연결 - 성공 테스트")
    void connectMyXUseCaseSuccessTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, savedMyX.getId(), xsUserId);

        //when
        ConnectMyXResponseDto result = connectMyXUseCase.connectMyX(connectMyXCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isConnected());
        Assertions.assertTrue(result.isReadyToConnect());
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 상호 연결 - 성공 테스트")
    void twoConnectMyXUseCaseSuccessTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, savedMyX.getId(), xsUserId);

        RegisterMyXCommand secondRegisterMyXCommand = new RegisterMyXCommand(xsUserId, myXName, myXBirth);
        MyX secondMyX = MyX.createNewMyXByCommand(secondRegisterMyXCommand);

        MyX secondSavedMyX = registerMyXPort.registerNewMyX(secondMyX);

        ConnectMyXCommand secondConnectMyXCommand = new ConnectMyXCommand(xsUserId, secondSavedMyX.getId(), userId);

        //when
        ConnectMyXResponseDto result = connectMyXUseCase.connectMyX(connectMyXCommand);
        ConnectMyXResponseDto secondResult = connectMyXUseCase.connectMyX(secondConnectMyXCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isConnected());
        Assertions.assertTrue(result.isReadyToConnect());

        Assertions.assertNotNull(secondResult);
        Assertions.assertTrue(secondResult.isConnected());
        Assertions.assertTrue(secondResult.isReadyToConnect());
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 my x does not exist - 실패 테스트")
    void connectMyXUseCaseMyXDoesNotExistFailTest() {
        //given
        Long testMyXId = 1L;

        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, testMyXId, xsUserId);

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 my x user does not matched - 실패 테스트")
    void connectMyXUseCaseMyXUserDoesNotMatchedFailTest() {
        //given
        String requestUser = "testRequestUser";

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(requestUser, savedMyX.getId(), xsUserId);

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 my x already deleted - 실패 테스트")
    void connectMyXUseCaseMyXAlreadyDeletedFailTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        myX.deleteMyX();

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, savedMyX.getId(), xsUserId);

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] MyX 수정 my x already connected - 실패 테스트")
    void connectMyXUseCaseMyXAlreadyConnectedFailTest() {
        //given
        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        RegisterMyXCommand connectedMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX connectedMyX = MyX.createNewMyXByCommand(connectedMyXCommand);

        myX.connectMyX(connectedMyX);

        MyX savedMyX = registerMyXPort.registerNewMyX(myX);

        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, savedMyX.getId(), xsUserId);

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }
}