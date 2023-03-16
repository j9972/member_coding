package com.j9972.member.service;

import com.j9972.member.dto.MemberDTO;
import com.j9972.member.entity.MemberEntity;
import com.j9972.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
        /*
            repository 를 할때는 DTO 가 아니라 Entity로 객체를 넘겨야 한다
            1. DTO -> Entity ( 1.service 클래스에 별도 메소드, 2.엔티티에 팩토리 메소드 사용 )
            2. repository의 save 메서드 호출
        */
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);

    }

    /*
        Repositoy 에서 Entity를 Service에 전해주고,
        Service에서만 Entity를 사용하고
        Controller에서는 DTO를 사용한다.
     */
    public MemberDTO login(MemberDTO memberDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
            -> 원시적이여서 이렇게 하면 안됨
         */
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()) {
            // 조회 결과가 있다( 해당 이메일을 가진 회원 정보가 있다.) -> 비번이 맞다는 내용은 아니다.
            MemberEntity memberEntity = byMemberEmail.get();

            // String 비교를 할때는 ==이 아니라 equals() 써야함을 잊지말자
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비번 일치

                // entity -> dto 변환후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;

            } else {
                // 불일치
                return null;
            }

        } else {
            // 조회 결과가 없다( 해당 이메일을 가진 회원이 없다 )
            return null;
        }

    }

    public List<MemberDTO> findAll() {
        // Entity가 여러개 담긴 list 객체를 DTO가 여러개 담긴 list로 옮기는 방법
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        // findById는 기본적으로 Optional 을 사용해준다
        // Optional 객체를 get()으로 까야 Entity 객체가 보인다
        // 그리고 우리는 Entity를 DTO로 변환해서 controller에에보내면 된다
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        // update에서는 update라는 메소드가 없고 save를 사용한 ( id 가 같이 넘어왔기에 이걸 쓰면 된다 )
        // 여기서 MemberEntity로 해서 entity 객체로 save하면 update가 안되고 insert가 된다.
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if (byMemberEmail.isPresent()) {
            // 조회결과가 있다 -> 사용할 수 없다.
            return null;
        } else {
            // 조회결과가 없다 -> 사용할 수 있다.
            return "ok";
        }
    }
}
