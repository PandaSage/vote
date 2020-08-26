package com.aeexe.vote.domain.gloval;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {
    private LocalDateTime regDtm;

    private LocalDateTime uptDtm;

    private String regUserId;

    private String uptUserId;
}
