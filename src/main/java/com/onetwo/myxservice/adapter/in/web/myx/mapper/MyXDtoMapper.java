package com.onetwo.myxservice.adapter.in.web.myx.mapper;

import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.DeleteMyXResponse;
import com.onetwo.myxservice.adapter.in.web.myx.response.RegisterMyXResponse;
import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;

public interface MyXDtoMapper {
    RegisterMyXCommand registerRequestToCommand(String userId, RegisterMyXRequest registerMyXRequest);

    RegisterMyXResponse dtoToRegisterResponse(RegisterMyXResponseDto registerMyXResponseDto);

    DeleteMyXCommand deleteRequestToCommand(String userId, DeleteMyXRequest deleteMyXRequest);

    DeleteMyXResponse dtoToDeleteResponse(DeleteMyXResponseDto deleteMyXResponseDto);
}