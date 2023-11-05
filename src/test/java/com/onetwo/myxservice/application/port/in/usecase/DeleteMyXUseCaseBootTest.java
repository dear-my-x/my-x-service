package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
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
class DeleteMyXUseCaseBootTest {

    @Autowired
    private DeleteMyXUseCase deleteMyXUseCase;

    @Autowired
    private RegisterMyXPort registerMyXPort;

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
        registerMyXPort.registerNewMyX(myX);

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

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> deleteMyXUseCase.deleteMyX(deleteMyXCommand));
    }
}
