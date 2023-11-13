package com.onetwo.myxservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

class ConnectMyXCommandValidationTest {

    private final String userId = "testUserId";
    private final Long myXId = 1L;
    private final String xsUserId = "testMyXUserId";

    @Test
    @DisplayName("[단위][Command Validation] Connect MyX Command Validation test - 성공 테스트")
    void connectMyXCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new ConnectMyXCommand(userId, myXId, xsUserId));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Connect MyX Command my x id Validation fail test - 실패 테스트")
    void connectMyXCommandMyXIdValidationFailTest(Long testMyXId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new ConnectMyXCommand(userId, testMyXId, xsUserId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Connect MyX Command user Id Validation fail test - 실패 테스트")
    void connectMyXCommandUserIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new ConnectMyXCommand(testUserId, myXId, xsUserId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Connect MyX Command xs user id Validation fail test - 실패 테스트")
    void connectMyXCommandXsUserIdValidationFailTest(String testXsUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new ConnectMyXCommand(userId, myXId, testXsUserId));
    }
}