package com.onetwo.myxservice.application.service.adapter;

import com.onetwo.myxservice.adapter.out.persistence.entity.MyXEntity;
import com.onetwo.myxservice.adapter.out.persistence.repository.myx.MyXRepository;
import com.onetwo.myxservice.application.port.out.ReadMyXPort;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.domain.MyX;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyXPersistenceAdapter implements ReadMyXPort, RegisterMyXPort {

    private final MyXRepository myXRepository;

    @Override
    public List<MyX> findByUserId(String userId) {
        List<MyXEntity> myXEntityList = myXRepository.findByUserId(userId);

        if (myXEntityList == null || myXEntityList.isEmpty()) return Collections.emptyList();

        return myXEntityList.stream().map(MyX::entityToDomain).toList();
    }

    @Override
    public MyX registerNewMyX(MyX newMyX) {
        MyXEntity newMyXEntity = MyXEntity.domainToEntity(newMyX);

        MyXEntity savedMyXEntity = myXRepository.save(newMyXEntity);

        return MyX.entityToDomain(savedMyXEntity);
    }
}
