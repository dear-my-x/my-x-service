package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.ConnectMyXCommand;
import com.onetwo.myxservice.application.port.in.response.ConnectMyXResponseDto;

public interface ConnectMyXUseCase {
    
    /**
     * Connect My X use case,
     * connect my x if two my x equal connect them,
     * if not just ready for connect
     *
     * @param connectMyXCommand Request connect my x information
     * @return Boolean about connect success and connect ready success
     */
    ConnectMyXResponseDto connectMyX(ConnectMyXCommand connectMyXCommand);
}
