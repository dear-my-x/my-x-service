package com.onetwo.myxservice.application.port.in.command;

import com.onetwo.myxservice.application.port.in.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;

@Getter
public final class RegisterMyXCommand extends SelfValidating<RegisterMyXCommand> {

    @NotEmpty
    private final String userId;

    @NotEmpty
    private final String xsName;

    @NotNull
    private final Instant xsBirth;

    public RegisterMyXCommand(String userId, String xsName, Instant xsBirth) {
        this.userId = userId;
        this.xsName = xsName;
        this.xsBirth = xsBirth;
        this.validateSelf();
    }
}
