package com.onetwo.myxservice.adapter.in.web.myx.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ConnectMyXRequest(@NotNull Long myXId,
                                @NotEmpty String xsUserId) {
}
