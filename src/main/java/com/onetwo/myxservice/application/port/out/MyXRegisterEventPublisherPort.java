package com.onetwo.myxservice.application.port.out;

import com.onetwo.myxservice.domain.MyX;

public interface MyXRegisterEventPublisherPort {

    void publishRegisterMyXEvent(MyX registeredMyX);
}
