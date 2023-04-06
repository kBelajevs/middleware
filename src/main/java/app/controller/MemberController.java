package app.controller;

import app.domain.Member;
import app.dto.MemberDTO;
import app.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final ModelMapper modelMapper;
  private final MemberService memberService;

  @PostMapping("/members/{sessionId}")
  @ResponseStatus(HttpStatus.CREATED)
  public MemberDTO joinMemberToSession(@PathVariable Integer sessionId,
      @RequestBody MemberDTO memberDTO) {
    var member = modelMapper.map(memberDTO, Member.class);
    var savedMember = memberService.addMember(member, sessionId);
    return modelMapper.map(savedMember, MemberDTO.class);
  }

  @DeleteMapping("/members/{memberId}")
  public void logOutMemberFromSession(@PathVariable Integer memberId) {
    memberService.removeMember(memberId);
  }
}
