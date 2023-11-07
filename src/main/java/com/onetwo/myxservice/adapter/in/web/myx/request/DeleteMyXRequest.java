package com.onetwo.myxservice.adapter.in.web.myx.request;

import jakarta.validation.constraints.NotNull;

public record DeleteMyXRequest(@NotNull Long myXId) {
}
