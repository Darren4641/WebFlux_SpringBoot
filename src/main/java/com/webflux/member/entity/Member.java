package com.webflux.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("member")
@Builder
@Getter
public class Member {
    @Id
    @Column("id")
    private Integer id;

    @Column("name")
    private String name;

    @Column("age")
    private Integer age;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createAt;
}
