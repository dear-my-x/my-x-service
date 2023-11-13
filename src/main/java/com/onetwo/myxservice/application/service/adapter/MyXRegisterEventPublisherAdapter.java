package com.onetwo.myxservice.application.service.adapter;

import com.onetwo.myxservice.adapter.out.event.RegisterMyXEvent;
import com.onetwo.myxservice.application.port.out.MyXRegisterEventPublisherPort;
import com.onetwo.myxservice.domain.MyX;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyXRegisterEventPublisherAdapter implements MyXRegisterEventPublisherPort {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publish event when my x registered
     *
     * @param registeredMyX registered my x
     */
    @Override
    public void publishRegisterMyXEvent(MyX registeredMyX) {
        RegisterMyXEvent registerMyXEvent = new RegisterMyXEvent(registeredMyX.getId());
        eventPublisher.publishEvent(registerMyXEvent);
    }
}
