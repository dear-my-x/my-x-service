package com.onetwo.myxservice.adapter.in.web.myx.mapper;

import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.DeleteMyXResponse;
import com.onetwo.myxservice.adapter.in.web.myx.response.RegisterMyXResponse;
import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MyXDtoMapperImpl implements MyXDtoMapper {
    @Override
    public RegisterMyXCommand registerRequestToCommand(String userId, RegisterMyXRequest registerMyXRequest) {
        return new RegisterMyXCommand(userId, registerMyXRequest.xsName(), registerMyXRequest.xsBirth());
    }

    @Override
    public RegisterMyXResponse dtoToRegisterResponse(RegisterMyXResponseDto registerMyXResponseDto) {
        return new RegisterMyXResponse(registerMyXResponseDto.registerMyXSuccess());
    }

    @Override
    public DeleteMyXCommand deleteRequestToCommand(String userId, DeleteMyXRequest deleteMyXRequest) {
        return new DeleteMyXCommand(userId, deleteMyXRequest.xsName(), deleteMyXRequest.xsBirth());
    }

    @Override
    public DeleteMyXResponse dtoToDeleteResponse(DeleteMyXResponseDto deleteMyXResponseDto) {
        return new DeleteMyXResponse(deleteMyXResponseDto.isDeleteSuccess());
    }
}
