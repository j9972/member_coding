package com.j9972.member.repository;

import com.j9972.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
    repository에서 객체를 넘겨줘야 할때는 항상 DTO 가 아니라 Entity로 데이터를 넘겨야 한
 */
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 이메일로 회원 정보 조회 ( select * from member_table where member_email =? ) 를 의미
    // Optional 은 null을 방지해준다.
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
