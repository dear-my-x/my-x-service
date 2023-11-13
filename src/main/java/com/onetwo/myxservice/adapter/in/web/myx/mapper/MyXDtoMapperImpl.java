package com.onetwo.myxservice.adapter.in.web.myx.mapper;

import com.onetwo.myxservice.adapter.in.web.myx.request.ConnectMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.UpdateMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.*;
import com.onetwo.myxservice.application.port.in.command.*;
import com.onetwo.myxservice.application.port.in.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return new DeleteMyXCommand(userId, deleteMyXRequest.myXId());
    }

    @Override
    public DeleteMyXResponse dtoToDeleteResponse(DeleteMyXResponseDto deleteMyXResponseDto) {
        return new DeleteMyXResponse(deleteMyXResponseDto.isDeleteSuccess());
    }

    @Override
    public MyXDetailsCommand getMyXDetailsRequestToCommand(String userId) {
        return new MyXDetailsCommand(userId);
    }

    @Override
    public MyXDetailsResponse dtoToMyXDetailsResponse(List<MyXDetailResponseDto> myXDetailResponseDtoList) {
        return new MyXDetailsResponse(myXDetailResponseDtoList);
    }

    @Override
    public UpdateMyXCommand updateRequestToCommand(String userId, UpdateMyXRequest updateMyXRequest) {
        return new UpdateMyXCommand(userId, updateMyXRequest.myXId(), updateMyXRequest.xsName(), updateMyXRequest.xsBirth());
    }

    @Override
    public UpdateMyXResponse dtoToUpdateResponse(UpdateMyXResponseDto updateMyXResponseDto) {
        return new UpdateMyXResponse(updateMyXResponseDto.updateMyXSuccess());
    }

    @Override
    public ConnectMyXCommand connectRequestToCommand(String userId, ConnectMyXRequest connectMyXRequest) {
        return new ConnectMyXCommand(userId, connectMyXRequest.myXId(), connectMyXRequest.xsUserId());
    }

    @Override
    public ConnectMyXResponse dtoToConnectResponse(ConnectMyXResponseDto connectMyXResponseDto) {
        return new ConnectMyXResponse(connectMyXResponseDto.isConnected(), connectMyXResponseDto.isReadyToConnect());
    }
}
