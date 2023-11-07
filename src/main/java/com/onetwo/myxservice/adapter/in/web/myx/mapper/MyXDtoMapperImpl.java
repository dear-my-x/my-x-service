package com.onetwo.myxservice.adapter.in.web.myx.mapper;

import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.UpdateMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.DeleteMyXResponse;
import com.onetwo.myxservice.adapter.in.web.myx.response.MyXDetailsResponse;
import com.onetwo.myxservice.adapter.in.web.myx.response.RegisterMyXResponse;
import com.onetwo.myxservice.adapter.in.web.myx.response.UpdateMyXResponse;
import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.command.MyXDetailsCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.command.UpdateMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.UpdateMyXResponseDto;
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
}
