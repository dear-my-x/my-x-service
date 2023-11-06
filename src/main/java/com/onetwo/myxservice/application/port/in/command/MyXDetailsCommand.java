package com.onetwo.myxservice.application.port.in.command;

import com.onetwo.myxservice.application.port.in.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MyXDetailsCommand extends SelfValidating<DeleteMyXCommand> {

    @NotEmpty
    private final String userId;

    public MyXDetailsCommand(String userId) {
        this.userId = userId;
        this.validateSelf();
    }
}
