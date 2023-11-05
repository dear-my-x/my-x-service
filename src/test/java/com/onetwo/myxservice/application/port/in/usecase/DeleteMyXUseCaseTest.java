package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeleteMyXUseCaseTest {

    @InjectMocks
    private MyXService deleteMyXUseCase;

    @Mock
    private ReadMyXPort readMyXPort;

    @Mock
    private UpdateMyXPort updateMyXPort;

    @Mock
    private MyXUseCaseConverter myXUseCaseConverter;

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[단위][Use Case] MyX 삭제 - 성공 테스트")
    void deleterMyXUseCaseSuccessTest() {
        //given
        DeleteMyXCommand deleteMyXCommand = new DeleteMyXCommand(userId, myXName, myXBirth);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        DeleteMyXResponseDto deleteMyXResponseDto = new DeleteMyXResponseDto(true);

        given(readMyXPort.findByUserIdAndXsNameAndXsBirth(anyString(), anyString(), any(Instant.class))).willReturn(Optional.of(myX));
        given(myXUseCaseConverter.myXToDeleteMyXResponseDto(any(MyX.class))).willReturn(deleteMyXResponseDto);
        //when
        DeleteMyXResponseDto result = deleteMyXUseCase.deleteMyX(deleteMyXCommand);

        //then
        Assertions.assertTrue(result.isDeleteSuccess());
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 삭제 my x does not exist - 실패 테스트")
    void deleterMyXUseCaseMyXDoesNotExistFailTest() {
        //given
        DeleteMyXCommand deleteMyXCommand = new DeleteMyXCommand(userId, myXName, myXBirth);

        given(readMyXPort.findByUserIdAndXsNameAndXsBirth(anyString(), anyString(), any(Instant.class))).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> deleteMyXUseCase.deleteMyX(deleteMyXCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] MyX 삭제 my x already deleted - 실패 테스트")
    void deleterMyXUseCaseMyXAlreadyDeletedFailTest() {
        //given
        DeleteMyXCommand deleteMyXCommand = new DeleteMyXCommand(userId, myXName, myXBirth);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);
        MyX myX = MyX.createNewMyXByCommand(registerMyXCommand);

        myX.deleteMyX();

        given(readMyXPort.findByUserIdAndXsNameAndXsBirth(anyString(), anyString(), any(Instant.class))).willReturn(Optional.of(myX));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> deleteMyXUseCase.deleteMyX(deleteMyXCommand));
    }
}