package com.onetwo.myxservice.application.service.converter;

import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.domain.MyX;

public interface MyXUseCaseConverter {
    RegisterMyXResponseDto myXToRegisterMyXResponseDto(MyX registeredMyX);

    DeleteMyXResponseDto myXToDeleteMyXResponseDto(MyX myX);

    MyXDetailResponseDto myXToDetailResponseDto(MyX myX);
}
