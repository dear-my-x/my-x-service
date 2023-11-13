package com.onetwo.myxservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

class DeleteMyXCommandValidationTest {

    private final String userId = "testUserId";
    private final Long myXId = 1L;

    @Test
    @DisplayName("[단위][Command Validation] Delete MyX Command Validation test - 성공 테스트")
    void deleteMyXCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new DeleteMyXCommand(userId, myXId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Delete MyX Command user Id Validation fail test - 실패 테스트")
    void deleteMyXCommandUserIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeleteMyXCommand(testUserId, myXId));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Delete MyX Command my x id Validation fail test - 실패 테스트")
    void deleteMyXCommandMyXIdValidationFailTest(Long testMyXId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeleteMyXCommand(userId, testMyXId));
    }
}