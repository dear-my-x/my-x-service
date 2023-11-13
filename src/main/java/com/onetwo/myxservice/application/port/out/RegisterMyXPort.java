package com.onetwo.myxservice.application.port.out;

import com.onetwo.myxservice.domain.MyX;

public interface RegisterMyXPort {
    MyX registerNewMyX(MyX newMyX);
}
