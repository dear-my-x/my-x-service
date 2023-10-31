package com.onetwo.myxservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyX extends BaseDomain {

    private Long id;

    private String userId;

    private Boolean state;
}
