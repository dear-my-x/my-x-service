package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.MyXDetailsCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.common.GlobalStatus;
import com.onetwo.myxservice.domain.MyX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@Transactional
class ReadMyXUseCaseBootTest {

    @Autowired
    private ReadMyXUseCase readMyXUseCase;

    @Autowired
    private RegisterMyXPort registerMyXPort;

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[통합][Use Case] MyX list 조회 - 성공 테스트")
    void getMyXDetailsUseCaseSuccessTest() {
        //given
        MyXDetailsCommand myXDetailsCommand = new MyXDetailsCommand(userId);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);

        for (int i = 0; i < GlobalStatus.MAX_MY_X_NUMBER; i++)
            registerMyXPort.registerNewMyX(MyX.createNewMyXByCommand(registerMyXCommand));

        //when
        List<MyXDetailResponseDto> result = readMyXUseCase.getMyXDetails(myXDetailsCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("[통합][Use Case] MyX list 조회 my x 미존재 - 성공 테스트")
    void getMyXDetailsUseCaseEmptySuccessTest() {
        //given
        MyXDetailsCommand myXDetailsCommand = new MyXDetailsCommand(userId);

        //when
        List<MyXDetailResponseDto> result = readMyXUseCase.getMyXDetails(myXDetailsCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}