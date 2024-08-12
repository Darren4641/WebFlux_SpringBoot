package com.webflux.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class MemberDto {

    @Builder
    @Getter
    public static class CRUD {
        private Integer id;

        private String name;

        private Integer age;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createAt;

    }
}
