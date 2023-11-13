package com.onetwo.myxservice.application.port.in.usecase;

import com.onetwo.myxservice.application.port.in.command.MyXDetailsCommand;
import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;

import java.util.List;

public interface ReadMyXUseCase {
    /**
     * Get about My X detail information use case
     *
     * @param myXDetailsCommand request user id
     * @return user's my x list
     */
    List<MyXDetailResponseDto> getMyXDetails(MyXDetailsCommand myXDetailsCommand);
}
