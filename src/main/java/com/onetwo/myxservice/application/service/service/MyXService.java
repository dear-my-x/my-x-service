package com.onetwo.myxservice.application.service.service;

import com.onetwo.myxservice.application.port.in.command.*;
import com.onetwo.myxservice.application.port.in.response.*;
import com.onetwo.myxservice.application.port.in.usecase.*;
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
public class MyXService implements RegisterMyXUseCase, DeleteMyXUseCase, ReadMyXUseCase, UpdateMyXUseCase, ConnectMyXUseCase {

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
        MyX myX = checkMyXExistAndGetMyX(deleteMyXCommand.getMyXId());

        if (isRequestUserAndMyXRegisterUserNotSame(deleteMyXCommand.getUserId(), myX))
            throw new BadRequestException("User is not same");

        myX.deleteMyX();

        updateMyXPort.updateMyX(myX);

        return myXUseCaseConverter.myXToDeleteMyXResponseDto(myX);
    }

    private MyX checkMyXExistAndGetMyX(Long myXId) {
        Optional<MyX> optionalMyX = readMyXPort.findById(myXId);

        if (optionalMyX.isEmpty()) throw new NotFoundResourceException("My x dose not exist");

        if (optionalMyX.get().isDeleted()) throw new BadRequestException("My x already deleted");

        return optionalMyX.get();
    }

    private boolean isRequestUserAndMyXRegisterUserNotSame(String userId, MyX myX) {
        return !myX.getUserId().equals(userId);
    }

    /**
     * Get about My X detail information use case
     *
     * @param myXDetailsCommand request user id
     * @return user's my x list
     */
    @Override
    @Transactional(readOnly = true)
    public List<MyXDetailResponseDto> getMyXDetails(MyXDetailsCommand myXDetailsCommand) {
        List<MyX> myXList = readMyXPort.findByUserId(myXDetailsCommand.getUserId());

        return myXList.stream().map(myXUseCaseConverter::myXToDetailResponseDto).toList();
    }

    /**
     * Update My X use case,
     * update my x data to persistence
     *
     * @param updateMyXCommand Request update my x information
     * @return Boolean about update success
     */
    @Override
    @Transactional
    public UpdateMyXResponseDto updateMyX(UpdateMyXCommand updateMyXCommand) {
        MyX myX = checkMyXExistAndGetMyX(updateMyXCommand.getMyXId());

        if (isRequestUserAndMyXRegisterUserNotSame(updateMyXCommand.getUserId(), myX))
            throw new BadRequestException("User is not same");

        if (myX.isConnected())
            throw new BadRequestException("My X already connected. It's can't update. There only can delete");

        myX.updateMyX(updateMyXCommand);

        updateMyXPort.updateMyX(myX);

        return myXUseCaseConverter.myXToUpdateResponseDto(true);
    }

    /**
     * Connect My X use case,
     * connect my x if two my x equal connect them,
     * if not just ready for connect
     *
     * @param connectMyXCommand Request connect my x information
     * @return Boolean about connect success and connect ready success
     */
    @Override
    @Transactional
    public ConnectMyXResponseDto connectMyX(ConnectMyXCommand connectMyXCommand) {
        MyX myX = checkMyXExistAndGetMyX(connectMyXCommand.getMyXId());

        if (isRequestUserAndMyXRegisterUserNotSame(connectMyXCommand.getUserId(), myX))
            throw new BadRequestException("User is not same");

        if (myX.isConnected())
            throw new BadRequestException("My X already connected.");

        Optional<MyX> optionalMyXsMyX = readMyXPort.findByUserIdAndXsUserId(connectMyXCommand.getXsUserId(), connectMyXCommand.getUserId());

        if (optionalMyXsMyX.isPresent()) {
            MyX myXsMyX = optionalMyXsMyX.get();
            myX.connectMyX(myXsMyX);
            updateMyXPort.updateMyX(myXsMyX);
        } else myX.readyToConnect(connectMyXCommand);

        updateMyXPort.updateMyX(myX);

        return myXUseCaseConverter.myXToConnectResponseDto(myX);
    }
}
