package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.ConnectMyXCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.ConnectMyXResponseDto;
import com.onetwo.myxservice.application.port.out.ReadMyXPort;
import com.onetwo.myxservice.application.port.out.UpdateMyXPort;
import com.onetwo.myxservice.application.service.converter.MyXUseCaseConverter;
import com.onetwo.myxservice.application.service.service.MyXService;
import com.onetwo.myxservice.common.exceptions.BadRequestException;
import com.onetwo.myxservice.common.exceptions.NotFoundResourceException;
import com.onetwo.myxservice.domain.MyX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConnectMyXUseCaseTest {

    @InjectMocks
    private MyXService connectMyXUseCase;

    @Mock
    private ReadMyXPort readMyXPort;

    @Mock
    private UpdateMyXPort updateMyXPort;

    @Mock
    private MyXUseCaseConverter myXUseCaseConverter;

    private final String userId = "testUserId";
    private final Long myXId = 1L;
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");
    private final String xsUserId = "testMyXUserId";

    @Test
    @DisplayName("[단위][Use Case] MyX 연결 - 성공 테스트")
    void connectMyXUseCaseSuccessTest() {
        //given
        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, myXId, xsUserId);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        ConnectMyXResponseDto connectMyXResponseDto = new ConnectMyXResponseDto(false, true);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));
        given(readMyXPort.findByUserIdAndXsUserId(anyString(), anyString())).willReturn(Optional.empty());
        given(myXUseCaseConverter.myXToConnectResponseDto(any(MyX.class))).willReturn(connectMyXResponseDto);
        //when
        ConnectMyXResponseDto result = connectMyXUseCase.connectMyX(connectMyXCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isConnected());
        Assertions.assertTrue(result.isReadyToConnect());
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x does not exist - 실패 테스트")
    void connectMyXUseCaseMyXDoesNotExistFailTest() {
        //given
        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, myXId, xsUserId);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x user does not matched - 실패 테스트")
    void connectMyXUseCaseMyXUserDoesNotMatchedFailTest() {
        //given
        String requestUser = "testRequestUser";

        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(requestUser, myXId, xsUserId);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x already deleted - 실패 테스트")
    void connectMyXUseCaseMyXAlreadyDeletedFailTest() {
        //given
        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, myXId, xsUserId);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        myX.deleteMyX();

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x already connected - 실패 테스트")
    void connectMyXUseCaseMyXAlreadyConnectedFailTest() {
        //given
        ConnectMyXCommand connectMyXCommand = new ConnectMyXCommand(userId, myXId, xsUserId);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        RegisterMyXCommand connectedMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX connectedMyX = MyX.createNewMyXByCommand(connectedMyXCommand);

        myX.connectMyX(connectedMyX);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> connectMyXUseCase.connectMyX(connectMyXCommand));
    }
}