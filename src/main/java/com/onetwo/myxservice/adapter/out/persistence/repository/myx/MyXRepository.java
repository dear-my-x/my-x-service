package com.onetwo.myxservice.adapter.out.persistence.repository.myx;

import com.onetwo.myxservice.adapter.out.persistence.entity.MyXEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyXRepository extends JpaRepository<MyXEntity, Long> {
    List<MyXEntity> findByUserId(String userId);

    Optional<MyXEntity> findByUserIdAndXsUserId(String userId, String xsUserId);
}
