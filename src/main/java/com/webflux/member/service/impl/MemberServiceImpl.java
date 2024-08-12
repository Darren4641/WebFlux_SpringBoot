package com.webflux.member.service.impl;


import com.webflux.exception.ApiException;
import com.webflux.member.dto.MemberDto;
import com.webflux.member.mapper.MemberMapper;
import com.webflux.member.repository.MemberRepository;
import com.webflux.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public Mono<MemberDto.CRUD> create(MemberDto.CRUD memberDto) {
        return memberRepository.save(memberMapper.toEntity(memberDto))
                .flatMap(member -> memberRepository.findById(member.getId()))
                .map(memberMapper::toDTO);
    }

    @Override
    public Mono<MemberDto.CRUD> item(Integer id) {
        return memberRepository.findById(id)
                .map(memberMapper::toDTO)
                .switchIfEmpty(Mono.error(new ApiException("no data")));
    }

    @Override
    public Flux<Page<MemberDto.CRUD>> list(PageRequest pageRequest) {
        return memberRepository.findAllBy(pageRequest)
                .map(memberMapper::toDTO)
                .collectList()
                .flatMapMany(list ->
                        memberRepository.count()
                                .map(total -> new PageImpl<>(list, pageRequest, total))
                                .flux()
                );
    }
}
