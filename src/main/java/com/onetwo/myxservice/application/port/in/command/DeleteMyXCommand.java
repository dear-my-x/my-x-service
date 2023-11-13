package com.onetwo.myxservice.application.port.in.command;

import com.onetwo.myxservice.application.port.in.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public final class DeleteMyXCommand extends SelfValidating<DeleteMyXCommand> {

    @NotEmpty
    private final String userId;

    @NotNull
    private final Long myXId;

    public DeleteMyXCommand(String userId, Long myXId) {
        this.userId = userId;
        this.myXId = myXId;
        this.validateSelf();
    }
}
