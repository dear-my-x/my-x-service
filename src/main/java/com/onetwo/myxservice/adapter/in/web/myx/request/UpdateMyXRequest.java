package com.onetwo.myxservice.adapter.in.web.myx.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UpdateMyXRequest(@NotNull Long myXId,
                               @NotEmpty String xsName,
                               @NotNull Instant xsBirth) {
}
