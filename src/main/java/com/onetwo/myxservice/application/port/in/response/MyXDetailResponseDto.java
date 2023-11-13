package com.onetwo.myxservice.application.port.in.response;

import java.time.Instant;

public record MyXDetailResponseDto(long id,
                                   String userId,
                                   String xsName,
                                   Instant xsBirth,
                                   boolean isConnected,
                                   String xsUserId,
                                   boolean state) {
}
