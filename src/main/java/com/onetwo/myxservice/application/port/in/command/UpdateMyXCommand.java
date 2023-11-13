package com.onetwo.myxservice.application.port.in.command;

import com.onetwo.myxservice.application.port.in.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;

@Getter
public final class UpdateMyXCommand extends SelfValidating<UpdateMyXCommand> {

    @NotEmpty
    private final String userId;

    @NotNull
    private final Long myXId;

    @NotEmpty
    private final String xsName;

    @NotNull
    private final Instant xsBirth;

    public UpdateMyXCommand(String userId, Long myXId, String xsName, Instant xsBirth) {
        this.userId = userId;
        this.myXId = myXId;
        this.xsName = xsName;
        this.xsBirth = xsBirth;
        this.validateSelf();
    }
}
