package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;

public interface RegisterMyXUseCase {

    /**
     * Register My X use case,
     * register my x data to persistence
     *
     * @param registerMyXCommand request user id and my x information
     * @return Boolean about register success
     */
    RegisterMyXResponseDto registerMyX(RegisterMyXCommand registerMyXCommand);
}
