package com.onetwo.myxservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MyXDetailsCommandValidationTest {

    private final String userId = "testUserId";

    @Test
    @DisplayName("[단위][Command Validation] MyX Details Command Validation test - 성공 테스트")
    void myXDetailsCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new MyXDetailsCommand(userId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] MyX Details Command user Id Validation fail test - 실패 테스트")
    void myXDetailsCommandUserIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new MyXDetailsCommand(testUserId));
    }
}