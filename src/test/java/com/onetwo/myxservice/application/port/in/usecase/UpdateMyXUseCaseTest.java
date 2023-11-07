package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.command.UpdateMyXCommand;
import com.onetwo.myxservice.application.port.in.response.UpdateMyXResponseDto;
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

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateMyXUseCaseTest {

    @InjectMocks
    private MyXService updateMyXUseCase;

    @Mock
    private ReadMyXPort readMyXPort;

    @Mock
    private UpdateMyXPort updateMyXPort;

    @Mock
    private MyXUseCaseConverter myXUseCaseConverter;

    private final Long myXId = 1L;
    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 - 성공 테스트")
    void updateMyXUseCaseSuccessTest() {
        //given
        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, myXId, myXName, myXBirth);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        UpdateMyXResponseDto updateMyXResponseDto = new UpdateMyXResponseDto(true);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));
        given(myXUseCaseConverter.myXToUpdateResponseDto(anyBoolean())).willReturn(updateMyXResponseDto);

        //when then
        Assertions.assertDoesNotThrow(() -> updateMyXUseCase.updateMyX(updateMyXCommand));

        UpdateMyXResponseDto result = updateMyXUseCase.updateMyX(updateMyXCommand);

        Assertions.assertTrue(result.updateMyXSuccess());
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x does not exist - 실패 테스트")
    void updateMyXUseCaseMyXDoesNotExistFailTest() {
        //given
        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, myXId, myXName, myXBirth);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> updateMyXUseCase.updateMyX(updateMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x user does not matched - 실패 테스트")
    void updateMyXUseCaseMyXUserDoesNotMatchedFailTest() {
        //given
        String requestUser = "testRequestUser";

        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(requestUser, myXId, myXName, myXBirth);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> updateMyXUseCase.updateMyX(updateMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x already deleted - 실패 테스트")
    void updateMyXUseCaseMyXAlreadyDeletedFailTest() {
        //given
        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, myXId, myXName, myXBirth);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        myX.deleteMyX();

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> updateMyXUseCase.updateMyX(updateMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 수정 my x already connected - 실패 테스트")
    void updateMyXUseCaseMyXAlreadyConnectedFailTest() {
        //given
        UpdateMyXCommand updateMyXCommand = new UpdateMyXCommand(userId, myXId, myXName, myXBirth);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        RegisterMyXCommand connectedMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX connectedMyX = MyX.createNewMyXByCommand(connectedMyXCommand);

        myX.connectMyX(connectedMyX);

        given(readMyXPort.findById(anyLong())).willReturn(Optional.of(myX));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> updateMyXUseCase.updateMyX(updateMyXCommand));
    }
}