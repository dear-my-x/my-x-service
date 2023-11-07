package com.onetwo.myxservice.application.port.out;

import com.onetwo.myxservice.domain.MyX;

import java.util.List;
import java.util.Optional;

public interface ReadMyXPort {
    List<MyX> findByUserId(String userId);

    Optional<MyX> findById(Long id);
}
