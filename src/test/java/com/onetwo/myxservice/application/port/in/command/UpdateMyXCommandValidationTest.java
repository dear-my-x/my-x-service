package com.onetwo.myxservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.Instant;

class UpdateMyXCommandValidationTest {

    private final Long myXId = 1L;
    private final String userId = "testUserId";
    private final String myXName = "정정일";
    private final Instant myXBirth = Instant.parse("1998-04-28T00:00:00Z");

    @Test
    @DisplayName("[단위][Command Validation] Update MyX Command Validation test - 성공 테스트")
    void updateMyXCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new UpdateMyXCommand(userId, myXId, myXName, myXBirth));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Update MyX Command user Id Validation fail test - 실패 테스트")
    void updateMyXCommandUserIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UpdateMyXCommand(testUserId, myXId, myXName, myXBirth));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Update MyX Command my x Id Validation fail test - 실패 테스트")
    void updateMyXCommandMyXIdValidationFailTest(Long testMyXId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UpdateMyXCommand(userId, testMyXId, myXName, myXBirth));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Update MyX Command user Id Validation fail test - 실패 테스트")
    void updateMyXCommandMyXNameValidationFailTest(String testMyXName) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UpdateMyXCommand(userId, myXId, testMyXName, myXBirth));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Update MyX Command user Id Validation fail test - 실패 테스트")
    void updateMyXCommandMyXBirthValidationFailTest(Instant testMyXBirth) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UpdateMyXCommand(userId, myXId, myXName, testMyXBirth));
    }
}