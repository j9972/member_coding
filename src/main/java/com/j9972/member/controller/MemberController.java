package com.j9972.member.controller;

import com.j9972.member.dto.MemberDTO;
import com.j9972.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor // 생성자DI
public class MemberController {

    private final MemberService memberService;

    /*
        html에서 어떤 링클를 클릭하는것들은 사용하는것은 다 GetMapping 이다.
        회면만 필요한 경우는 -> controller에서 곧 바로 return을 하고,
        DB 처리가 필요한 경우는 -> service,repository 를 거쳐서 작업을 한다
     */
    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save") // localhost:3030/member/save
    public String saveForm() {
        return "save";
    }

    // 회원가입 출력
    @PostMapping("/member/save") // localhost:3030/member/save
    public String save(@ModelAttribute MemberDTO memberDTO) {
        log.info("MemberController.save");
        log.info("memberEmail = " + memberDTO);
        memberService.save(memberDTO);
        return "index";
    }

    // 로그인 요청이 왔을때 띄워줄 페이지 표시
    @GetMapping("/member/login") // localhost:3030/member/login
    public String loadForm() {
        return "login";
    }

    // 로그인
    @PostMapping("/member/login") // localhost:3030/member/login
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            // loginEmail -> ${session.loginEmail} front에서 이런식으로 사용하면 된다
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            // login 실패
            return "login";
        }
    }

    // 회원 목록을 보여줌
    @GetMapping("/member/") // localhost:3030/member/
    public String findAll(Model model) {
        // 어떤 html로 가져갈 데이터가 있더면, model에 데이터를(list)를 담아갈것이다
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    // 각 회원 정보 보여주기
    @GetMapping("/member/{id}") // localhost:3030/member/{id}
    public String findById(@PathVariable("id") Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    // 회원 수정 페이지 요청
    @GetMapping("/member/update") // localhost:3030/member/update
    public String updateForm(HttpSession session, Model model) {
        // getAttribute 은 Object를 리턴하는데, String보다 Object보다 작기에 형변환 필요
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    // 회원 수정하기
    @PostMapping("/member/update") // localhost:3030/member/update
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        // controller가 끝나고 다른 controller를 부르는 방법
        return "redirect:/member/" + memberDTO.getId();
    }

    // 회원 삭제하기 ( 탈퇴하기 )
    @GetMapping("/member/delete/{id}") // localhost:3030/member/delete/{id}
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    // 로그아웃 페이지
    @GetMapping("/member/logout") // localhost:3030/member/logout
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // 이메일 중복 체크
    @PostMapping("/member/email-check") // localhost:3030/member/email-check
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        log.info("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
//        if (checkResult != null) {
//            return "ok";
//        } else {
//            return "no";
//        }
    }
}
