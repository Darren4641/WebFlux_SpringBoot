package com.webflux.member.service;


import com.webflux.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberService {
    Mono<MemberDto.CRUD> create(MemberDto.CRUD memberDto);
    Mono<MemberDto.CRUD> item(Integer id);
    Flux<Page<MemberDto.CRUD>> list(PageRequest pageRequest);

}
