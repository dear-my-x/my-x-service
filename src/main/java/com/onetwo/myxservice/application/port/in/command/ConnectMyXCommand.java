package com.onetwo.myxservice.application.port.in.command;

import com.onetwo.myxservice.application.port.in.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public final class ConnectMyXCommand extends SelfValidating<ConnectMyXCommand> {

    @NotEmpty
    private final String userId;

    @NotNull
    private final Long myXId;

    @NotEmpty
    private final String xsUserId;

    public ConnectMyXCommand(String userId, Long myXId, String xsUserId) {
        this.userId = userId;
        this.myXId = myXId;
        this.xsUserId = xsUserId;
        this.validateSelf();
    }
}
