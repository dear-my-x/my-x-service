package com.onetwo.myxservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.Instant;

class DeleteMyXCommandValidationTest {

    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[단위][Command Validation] Delete MyX Command Validation test - 성공 테스트")
    void deleteMyXCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new DeleteMyXCommand(userId, myXName, myXBirth));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Delete MyX Command user Id Validation fail test - 실패 테스트")
    void deleteMyXCommandUserIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeleteMyXCommand(testUserId, myXName, myXBirth));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Delete MyX Command user Id Validation fail test - 실패 테스트")
    void deleteMyXCommandMyXNameValidationFailTest(String testMyXName) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeleteMyXCommand(userId, testMyXName, myXBirth));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Delete MyX Command user Id Validation fail test - 실패 테스트")
    void deleteMyXCommandMyXBirthValidationFailTest(Instant testMyXBirth) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeleteMyXCommand(userId, myXName, testMyXBirth));
    }
}