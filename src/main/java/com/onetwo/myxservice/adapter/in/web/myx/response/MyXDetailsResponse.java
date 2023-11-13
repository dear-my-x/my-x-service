package com.onetwo.myxservice.adapter.in.web.myx.response;

import com.onetwo.myxservice.application.port.in.response.MyXDetailResponseDto;

import java.util.List;

public record MyXDetailsResponse(List<MyXDetailResponseDto> myXDetailResponseDtoList) {
}
