package com.onetwo.myxservice.application.port.in.command;

import com.onetwo.myxservice.application.port.in.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RegisterMyXCommand extends SelfValidating<RegisterMyXCommand> {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String xsName;

    @NotNull
    private Instant xsBirth;

    public RegisterMyXCommand(String userId, String xsName, Instant xsBirth) {
        this.userId = userId;
        this.xsName = xsName;
        this.xsBirth = xsBirth;
        this.validateSelf();
    }
}
