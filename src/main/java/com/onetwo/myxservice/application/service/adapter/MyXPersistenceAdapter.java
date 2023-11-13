package com.onetwo.myxservice.application.service.adapter;

import com.onetwo.myxservice.adapter.out.persistence.entity.MyXEntity;
import com.onetwo.myxservice.adapter.out.persistence.repository.myx.MyXRepository;
import com.onetwo.myxservice.application.port.out.ReadMyXPort;
import com.onetwo.myxservice.application.port.out.RegisterMyXPort;
import com.onetwo.myxservice.application.port.out.UpdateMyXPort;
import com.onetwo.myxservice.domain.MyX;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyXPersistenceAdapter implements ReadMyXPort, RegisterMyXPort, UpdateMyXPort {

    private final MyXRepository myXRepository;

    @Override
    public List<MyX> findByUserId(String userId) {
        List<MyXEntity> myXEntityList = myXRepository.findByUserId(userId);

        if (myXEntityList == null || myXEntityList.isEmpty()) return Collections.emptyList();

        return myXEntityList.stream().map(MyX::entityToDomain).filter(MyX::isNotDeleted).toList();
    }

    @Override
    public Optional<MyX> findById(Long id) {
        Optional<MyXEntity> myXEntity = myXRepository.findById(id);

        if (myXEntity.isPresent()) {
            MyX myX = MyX.entityToDomain(myXEntity.get());

            return Optional.of(myX);
        }

        return Optional.empty();
    }

    @Override
    public Optional<MyX> findByUserIdAndXsUserId(String userId, String xsUserId) {
        Optional<MyXEntity> myXEntity = myXRepository.findByUserIdAndXsUserId(userId, xsUserId);

        if (myXEntity.isPresent()) {
            MyX myX = MyX.entityToDomain(myXEntity.get());

            return Optional.of(myX);
        }

        return Optional.empty();
    }

    @Override
    public MyX registerNewMyX(MyX newMyX) {
        MyXEntity newMyXEntity = MyXEntity.domainToEntity(newMyX);

        MyXEntity savedMyXEntity = myXRepository.save(newMyXEntity);

        return MyX.entityToDomain(savedMyXEntity);
    }

    @Override
    public void updateMyX(MyX myX) {
        MyXEntity myXEntity = MyXEntity.domainToEntity(myX);

        myXRepository.save(myXEntity);
    }
}
