package com.onetwo.myxservice.domain;

import com.onetwo.myxservice.adapter.out.persistence.entity.MyXEntity;
import com.onetwo.myxservice.application.port.in.command.RegisterMyXCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyX extends BaseDomain {

    private Long id;

    private String userId;

    private String xsName;

    private Instant xsBirth;

    private boolean isConnected;

    private String xsUserId;

    private boolean state;

    public static MyX entityToDomain(MyXEntity myXEntity) {
        MyX myX = new MyX(
                myXEntity.getId(),
                myXEntity.getUserId(),
                myXEntity.getXsName(),
                myXEntity.getXsBirth(),
                myXEntity.getIsConnected(),
                myXEntity.getXsUserId(),
                myXEntity.getState());

        myX.setMetaDataByEntity(myXEntity);

        return myX;
    }

    public static MyX createNewMyXByCommand(RegisterMyXCommand registerMyXCommand) {
        MyX myX = new MyX(
                null,
                registerMyXCommand.getUserId(),
                registerMyXCommand.getXsName(),
                registerMyXCommand.getXsBirth(),
                false,
                null,
                false);

        myX.setDefaultState();

        return myX;
    }

    private void setDefaultState() {
        setCreatedAt(Instant.now());
        setCreateUser(this.userId);
    }
}
