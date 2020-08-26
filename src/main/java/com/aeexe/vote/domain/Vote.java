package com.aeexe.vote.domain;

import com.aeexe.vote.domain.gloval.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * vote main entity
 * py panda
 *
 * vote service main entity
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "voteId")
    private Long id;

    @Column
    private String serialNo;

    public static Long getLong(String nm) {
        return Long.getLong(nm);
    }
}
