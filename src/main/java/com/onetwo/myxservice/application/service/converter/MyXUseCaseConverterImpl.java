package com.onetwo.myxservice.application.service.converter;

import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.domain.MyX;
import org.springframework.stereotype.Component;

@Component
public class MyXUseCaseConverterImpl implements MyXUseCaseConverter {
    @Override
    public RegisterMyXResponseDto myXToRegisterMyXResponseDto(MyX registeredMyX) {
        return new RegisterMyXResponseDto(registeredMyX.getId() != null);
    }

    @Override
    public DeleteMyXResponseDto myXToDeleteMyXResponseDto(MyX myX) {
        return new DeleteMyXResponseDto(myX.isState());
    }
}
