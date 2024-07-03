package com.example.loginFilter.member;

import com.example.loginFilter.member.model.Member;
import com.example.loginFilter.member.model.dto.MemberSignupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
   private final MemberRepository memberRepository;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public String signup(MemberSignupReq memberSignupReq){
        Member member = Member.builder()
                .name(memberSignupReq.getName())
                .email(memberSignupReq.getEmail())
                .password(bCryptPasswordEncoder.encode(memberSignupReq.getPassword()))
                .build();

        memberRepository.save(member);
        return "저장 성공";
    }
}
