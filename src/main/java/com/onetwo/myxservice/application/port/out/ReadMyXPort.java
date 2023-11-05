package com.onetwo.myxservice.application.port.out;

import com.onetwo.myxservice.domain.MyX;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReadMyXPort {
    List<MyX> findByUserId(String userId);

    Optional<MyX> findByUserIdAndXsNameAndXsBirth(String userId, String xsName, Instant xsBirth);
}
