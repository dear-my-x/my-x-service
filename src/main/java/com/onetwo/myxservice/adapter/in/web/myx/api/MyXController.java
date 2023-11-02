package com.onetwo.myxservice.adapter.in.web.myx.api;

import com.onetwo.myxservice.adapter.in.web.myx.mapper.MyXDtoMapper;
import com.onetwo.myxservice.adapter.in.web.myx.request.RegisterMyXRequest;
import com.onetwo.myxservice.adapter.in.web.myx.response.RegisterMyXResponse;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.in.usecase.RegisterMyXUseCase;
import com.onetwo.myxservice.common.GlobalUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyXController {

    private final RegisterMyXUseCase registerMyXUseCase;

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
}
