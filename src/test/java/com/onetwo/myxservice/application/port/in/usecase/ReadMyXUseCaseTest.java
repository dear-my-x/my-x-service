package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.MyXDetailsCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;
import com.onetwo.myxservice.application.port.out.ReadMyXPort;
import com.onetwo.myxservice.application.service.converter.MyXUseCaseConverter;
import com.onetwo.myxservice.application.service.service.MyXService;
import com.onetwo.myxservice.common.GlobalStatus;
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

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReadMyXUseCaseTest {

    @InjectMocks
    private MyXService readMyXUseCase;

    @Mock
    private ReadMyXPort readMyXPort;

    @Mock
    private MyXUseCaseConverter myXUseCaseConverter;

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[단위][Use Case] MyX list 조회 - 성공 테스트")
    void getMyXDetailsUseCaseSuccessTest() {
        //given
        MyXDetailsCommand myXDetailsCommand = new MyXDetailsCommand(userId);

        RegisterMyXCommand registerMyXCommand = new RegisterMyXCommand(userId, myXName, myXBirth);

        List<MyX> myXList = new ArrayList<>();
        for (int i = 0; i < GlobalStatus.MAX_MY_X_NUMBER; i++)
            myXList.add(MyX.createNewMyXByCommand(registerMyXCommand));

        given(readMyXPort.findByUserId(userId)).willReturn(myXList);

        //when
        List<MyXDetailResponseDto> result = readMyXUseCase.getMyXDetails(myXDetailsCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("[단위][Use Case] MyX list 조회 my x 미존재 - 성공 테스트")
    void getMyXDetailsUseCaseEmptySuccessTest() {
        //given
        MyXDetailsCommand myXDetailsCommand = new MyXDetailsCommand(userId);

        given(readMyXPort.findByUserId(userId)).willReturn(Collections.emptyList());

        //when
        List<MyXDetailResponseDto> result = readMyXUseCase.getMyXDetails(myXDetailsCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}