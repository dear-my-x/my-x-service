package com.onetwo.myxservice.adapter.in.web.myx.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RegisterMyXRequest(@NotEmpty String xsName,
                                 @NotNull Instant xsBirth) {
}
