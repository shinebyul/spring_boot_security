package com.example.loginFilter.member;

import com.example.loginFilter.member.model.CustomUserDetails;
import com.example.loginFilter.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).get();
        CustomUserDetails userDetails = new CustomUserDetails(member);

//        UserDetails userDetails = new UserDetails() { //userDetails는 구현된 클래스가 없으므로 반드시 어디선가는 구현 해줘야하네 (User가 있는 것 같긴 함)
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                return null;
//            }
//
//            @Override
//            public String getPassword() {
//                return member.getPassword();
//            }
//
//            @Override
//            public String getUsername() {
//                return member.getEmail();
//            }
//
//            public Long getId(){
//                return member.getId();
//            }
//        };

        return userDetails;
    }
}
