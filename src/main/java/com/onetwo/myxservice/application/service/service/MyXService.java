package com.onetwo.myxservice.application.service.service;

import com.onetwo.myxservice.application.port.in.command.DeleteMyXCommand;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import com.onetwo.myxservice.application.port.in.response.DeleteMyXResponseDto;
import com.onetwo.myxservice.application.port.in.response.RegisterMyXResponseDto;
import com.onetwo.myxservice.application.port.in.usecase.DeleteMyXUseCase;
import com.onetwo.myxservice.application.port.in.usecase.RegisterMyXUseCase;
import com.onetwo.myxservice.application.port.out.MyXRegisterEventPublisherPort;
import com.onetwo.myxservice.application.port.out.ReadMyXPort;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.application.port.out.UpdateMyXPort;
import com.onetwo.myxservice.application.service.converter.MyXUseCaseConverter;
import com.onetwo.myxservice.common.GlobalStatus;
import com.onetwo.myxservice.common.exceptions.BadRequestException;
import com.onetwo.myxservice.common.exceptions.NotFoundResourceException;
import com.onetwo.myxservice.common.exceptions.ResourceAlreadyExistsException;
import com.onetwo.myxservice.common.exceptions.ResourceAlreadyFullException;
import com.onetwo.myxservice.domain.MyX;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyXService implements RegisterMyXUseCase, DeleteMyXUseCase {

    private final ReadMyXPort readMyXPort;
    private final RegisterMyXPort registerMyXPort;
    private final UpdateMyXPort updateMyXPort;
    private final MyXRegisterEventPublisherPort myXRegisterEventPublisherPort;
    private final MyXUseCaseConverter myXUseCaseConverter;

    /**
     * Register My X use case,
     * check some rules about my x
     * and register my x data to persistence
     * also publish event about register my x
     *
     * @param registerMyXCommand request user id and my x information
     * @return Boolean about register success
     */
    @Override
    @Transactional
    public RegisterMyXResponseDto registerMyX(RegisterMyXCommand registerMyXCommand) {
        List<MyX> myXList = readMyXPort.findByUserId(registerMyXCommand.getUserId());

        if (isMyXNumberIsFull(myXList))
            throw new ResourceAlreadyFullException("My X are could setting only " + GlobalStatus.MAX_MY_X_NUMBER + ". you are already have " + myXList.size());

        if (isSameMyXAlreadyExist(myXList, registerMyXCommand))
            throw new ResourceAlreadyExistsException("Same My X already exist");

        MyX newMyX = MyX.createNewMyXByCommand(registerMyXCommand);

        MyX registeredMyX = registerMyXPort.registerNewMyX(newMyX);

        myXRegisterEventPublisherPort.publishRegisterMyXEvent(registeredMyX);

        return myXUseCaseConverter.myXToRegisterMyXResponseDto(registeredMyX);
    }

    private boolean isMyXNumberIsFull(List<MyX> myXList) {
        return myXList.size() >= GlobalStatus.MAX_MY_X_NUMBER;
    }

    private boolean isSameMyXAlreadyExist(List<MyX> myXList, RegisterMyXCommand registerMyXCommand) {
        return myXList.stream().anyMatch(myX -> isMyXSame(registerMyXCommand, myX));
    }

    private boolean isMyXSame(RegisterMyXCommand registerMyXCommand, MyX myX) {
        return myX.getXsName().equals(registerMyXCommand.getXsName()) && myX.getXsBirth().compareTo(registerMyXCommand.getXsBirth()) == 0;
    }

    /**
     * Delete My X use case,
     * change my x state on persistence
     *
     * @param deleteMyXCommand request user id and my x information
     * @return Boolean about delete success
     */
    @Override
    @Transactional
    public DeleteMyXResponseDto deleteMyX(DeleteMyXCommand deleteMyXCommand) {
        Optional<MyX> optionalMyX = readMyXPort.findByUserIdAndXsNameAndXsBirth(
                deleteMyXCommand.getUserId(),
                deleteMyXCommand.getXsName(),
                deleteMyXCommand.getXsBirth());

        if (optionalMyX.isEmpty()) throw new NotFoundResourceException("My x dose not exist");

        MyX myX = optionalMyX.get();

        if (myX.isDeleted()) throw new BadRequestException("My x already deleted");

        myX.deleteMyX();

        updateMyXPort.updateMyX(myX);

        return myXUseCaseConverter.myXToDeleteMyXResponseDto(myX);
    }
}
