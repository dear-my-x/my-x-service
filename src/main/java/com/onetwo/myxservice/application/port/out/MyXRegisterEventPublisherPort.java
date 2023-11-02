package com.onetwo.myxservice.application.port.out;

import com.onetwo.myxservice.domain.MyX;

public interface MyXRegisterEventPublisherPort {

    public void publishRegisterMyXEvent(MyX registeredMyX);
}
