package com.onetwo.myxservice.adapter.in.web.myx.mapper;

import com.onetwo.myxservice.adapter.in.web.myx.request.ConnectMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.UpdateMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.*;
import com.onetwo.myxservice.application.port.in.command.*;
import com.onetwo.myxservice.application.port.in.response.*;

import java.util.List;

public interface MyXDtoMapper {
    RegisterMyXCommand registerRequestToCommand(String userId, RegisterMyXRequest registerMyXRequest);

    RegisterMyXResponse dtoToRegisterResponse(RegisterMyXResponseDto registerMyXResponseDto);

    DeleteMyXCommand deleteRequestToCommand(String userId, DeleteMyXRequest deleteMyXRequest);

    DeleteMyXResponse dtoToDeleteResponse(DeleteMyXResponseDto deleteMyXResponseDto);

    MyXDetailsCommand getMyXDetailsRequestToCommand(String userId);

    MyXDetailsResponse dtoToMyXDetailsResponse(List<MyXDetailResponseDto> myXDetailResponseDtoList);

    UpdateMyXCommand updateRequestToCommand(String userId, UpdateMyXRequest updateMyXRequest);

    UpdateMyXResponse dtoToUpdateResponse(UpdateMyXResponseDto updateMyXResponseDto);

    ConnectMyXCommand connectRequestToCommand(String userId, ConnectMyXRequest connectMyXRequest);

    ConnectMyXResponse dtoToConnectResponse(ConnectMyXResponseDto connectMyXResponseDto);
}
