package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;

public interface DeleteMyXUseCase {

    /**
     * Delete My X use case,
     * delete my x data on persistence
     *
     * @param deleteMyXCommand request user id and my x information
     * @return Boolean about delete success
     */
    DeleteMyXResponseDto deleteMyX(DeleteMyXCommand deleteMyXCommand);
}
