package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.UpdateMyXCommand;
import com.onetwo.myxservice.application.port.in.response.UpdateMyXResponseDto;

public interface UpdateMyXUseCase {
    UpdateMyXResponseDto updateMyX(UpdateMyXCommand updateMyXCommand);
}
