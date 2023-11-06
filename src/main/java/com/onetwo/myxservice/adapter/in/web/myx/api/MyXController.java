package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.onetwo.myxservice.adapter.in.web.myx.mapper.MyXDtoMapper;
import com.onetwo.myxservice.adapter.in.web.myx.request.DeleteMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.DeleteMyXResponse;
import com.onetwo.myxservice.adapter.in.web.myx.response.MyXDetailsResponse;
import com.onetwo.myxservice.adapter.in.web.myx.response.RegisterMyXResponse;
import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.command.MyXDetailsCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.in.usecase.DeleteMyXUseCase;
import com.onetwo.myxservice.application.port.in.usecase.ReadMyXUseCase;
import com.onetwo.myxservice.application.port.in.usecase.RegisterMyXUseCase;
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
}
