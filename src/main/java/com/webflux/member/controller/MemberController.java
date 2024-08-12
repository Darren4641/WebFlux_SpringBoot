package com.webflux.member.controller;

import com.webflux.common.APIResponse;
import com.webflux.exception.ApiException;
import com.webflux.member.dto.MemberDto;
import com.webflux.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public Mono<APIResponse<?>> create(@RequestBody MemberDto.CRUD memberDto) {
        return memberService.create(memberDto)
                .map(APIResponse::new);
    }

    @GetMapping("/{id}")
    public Mono<APIResponse<?>> getMember(@PathVariable("id") Integer id) {
        return memberService.item(id)
                .map(APIResponse::new);
    }

    @GetMapping("/")
    public Flux<APIResponse<?>> getMemberList(@RequestParam("page") Integer page,
                                              @RequestParam("size") Integer size) {
        return memberService.list(PageRequest.of(page - 1, size, Sort.by("id").descending()))
                .map(APIResponse::new);
    }
}
