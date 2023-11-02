package com.onetwo.myxservice.application.port.out;

import com.onetwo.myxservice.domain.MyX;

import java.util.List;

public interface ReadMyXPort {
    List<MyX> findByUserId(String userId);
}
