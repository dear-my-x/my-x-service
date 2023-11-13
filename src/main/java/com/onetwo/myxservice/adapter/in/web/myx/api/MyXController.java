package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.onetwo.myxservice.adapter.in.web.myx.mapper.MyXDtoMapper;
import com.onetwo.myxservice.adapter.in.web.myx.request.ConnectMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.UpdateMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.*;
import com.onetwo.myxservice.application.port.in.command.*;
import com.onetwo.myxservice.application.port.in.response.*;
import com.onetwo.myxservice.application.port.in.usecase.*;
import com.onetwo.myxservice.common.GlobalUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyXController {

    private final RegisterMyXUseCase registerMyXUseCase;
    private final DeleteMyXUseCase deleteMyXUseCase;
    private final ReadMyXUseCase readMyXUseCase;
    private final UpdateMyXUseCase updateMyXUseCase;
    private final ConnectMyXUseCase connectMyXUseCase;
    private final MyXDtoMapper myXDtoMapper;

    /**
     * Register My X inbound adapter
     *
     * @param registerMyXRequest Register request my x data
     * @param userId             user authentication id
     * @return Boolean about register success
     */
    @PostMapping(GlobalUrl.MY_X_ROOT)
    public ResponseEntity<RegisterMyXResponse> registerMyX(@RequestBody @Valid RegisterMyXRequest registerMyXRequest, @AuthenticationPrincipal String userId) {
        RegisterMyXCommand registerMyXCommand = myXDtoMapper.registerRequestToCommand(userId, registerMyXRequest);
        RegisterMyXResponseDto registerMyXResponseDto = registerMyXUseCase.registerMyX(registerMyXCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(myXDtoMapper.dtoToRegisterResponse(registerMyXResponseDto));
    }

    /**
     * Delete My X inbound adapter
     *
     * @param deleteMyXRequest Delete request my x information
     * @param userId           user authentication id
     * @return Boolean about delete success
     */
    @DeleteMapping(GlobalUrl.MY_X_ROOT)
    public ResponseEntity<DeleteMyXResponse> deleteMyX(@RequestBody @Valid DeleteMyXRequest deleteMyXRequest, @AuthenticationPrincipal String userId) {
        DeleteMyXCommand deleteMyXCommand = myXDtoMapper.deleteRequestToCommand(userId, deleteMyXRequest);
        DeleteMyXResponseDto deleteMyXResponseDto = deleteMyXUseCase.deleteMyX(deleteMyXCommand);
        return ResponseEntity.ok().body(myXDtoMapper.dtoToDeleteResponse(deleteMyXResponseDto));
    }

    /**
     * Get about My X detail information inbound adapter
     *
     * @param userId user authentication id
     * @return user's my x list
     */
    @GetMapping(GlobalUrl.MY_X_ROOT)
    public ResponseEntity<MyXDetailsResponse> getMyXDetails(@AuthenticationPrincipal String userId) {
        MyXDetailsCommand myXDetailsCommand = myXDtoMapper.getMyXDetailsRequestToCommand(userId);
        List<MyXDetailResponseDto> myXDetailResponseDtoList = readMyXUseCase.getMyXDetails(myXDetailsCommand);
        return ResponseEntity.ok().body(myXDtoMapper.dtoToMyXDetailsResponse(myXDetailResponseDtoList));
    }

    /**
     * Update My X inbound adapter
     *
     * @param updateMyXRequest Request update my x information
     * @param userId           user authentication id
     * @return Boolean about update success
     */
    @PutMapping(GlobalUrl.MY_X_ROOT)
    public ResponseEntity<UpdateMyXResponse> updateMyX(@RequestBody @Valid UpdateMyXRequest updateMyXRequest, @AuthenticationPrincipal String userId) {
        UpdateMyXCommand updateMyXCommand = myXDtoMapper.updateRequestToCommand(userId, updateMyXRequest);
        UpdateMyXResponseDto updateMyXResponseDto = updateMyXUseCase.updateMyX(updateMyXCommand);
        return ResponseEntity.ok().body(myXDtoMapper.dtoToUpdateResponse(updateMyXResponseDto));
    }

    /**
     * Connect My X inbound adapter
     *
     * @param connectMyXRequest Request connect my x information
     * @param userId            user authentication id
     * @return Boolean about connect success and connect ready success
     */
    @PutMapping(GlobalUrl.MY_X_CONNECT)
    public ResponseEntity<ConnectMyXResponse> connectMyX(@RequestBody @Valid ConnectMyXRequest connectMyXRequest, @AuthenticationPrincipal String userId) {
        ConnectMyXCommand connectMyXCommand = myXDtoMapper.connectRequestToCommand(userId, connectMyXRequest);
        ConnectMyXResponseDto connectMyXResponseDto = connectMyXUseCase.connectMyX(connectMyXCommand);
        return ResponseEntity.ok().body(myXDtoMapper.dtoToConnectResponse(connectMyXResponseDto));
    }
}
