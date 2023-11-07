package com.onetwo.myxservice.application.service.converter;

import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.UpdateMyXResponseDto;
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

    @Override
    public MyXDetailResponseDto myXToDetailResponseDto(MyX myX) {
        return new MyXDetailResponseDto(
                myX.getId(),
                myX.getUserId(),
                myX.getXsName(),
                myX.getXsBirth(),
                myX.isConnected(),
                myX.getUserId(),
                myX.isState()
        );
    }

    @Override
    public UpdateMyXResponseDto myXToUpdateResponseDto(boolean updateMyXSuccess) {
        return new UpdateMyXResponseDto(updateMyXSuccess);
    }
}
