package com.webflux.member.repository;

import com.webflux.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, Integer> {
    Flux<Member> findByName(Mono<String> name);
    Flux<Member> findAllBy(Pageable pageable);
}
