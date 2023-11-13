package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.UpdateMyXCommand;
import com.onetwo.myxservice.application.port.in.response.UpdateMyXResponseDto;

public interface UpdateMyXUseCase {
    
    /**
     * Update My X use case,
     * update my x data to persistence
     *
     * @param updateMyXCommand Request update my x information
     * @return Boolean about update success
     */
    UpdateMyXResponseDto updateMyX(UpdateMyXCommand updateMyXCommand);
}
