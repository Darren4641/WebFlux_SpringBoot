package com.webflux.member.mapper;

import com.webflux.member.dto.MemberDto;
import com.webflux.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MemberMapper {
    public MemberDto.CRUD toDTO(Member member) {
        return MemberDto.CRUD.builder()
                .id(member.getId())
                .name(member.getName())
                .age(member.getAge())
                .createAt(member.getCreateAt())
                .build();
    }

    public Member toEntity(MemberDto.CRUD memberDTO) {
        return Member.builder()
                .name(memberDTO.getName())
                .age(memberDTO.getAge())
                .createAt(LocalDateTime.now())
                .build();
    }
}
