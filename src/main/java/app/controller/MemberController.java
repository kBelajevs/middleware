package app.controller;

import app.domain.Member;
import app.dto.request.ReqMemberDTO;
import app.dto.response.ResMemberDTO;
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
  public ResMemberDTO joinMemberToSession(@PathVariable Integer sessionId,
      @RequestBody ReqMemberDTO reqMemberDTO) {
    var member = modelMapper.map(reqMemberDTO, Member.class);
    var savedMember = memberService.addMember(member, sessionId);
    return modelMapper.map(savedMember, ResMemberDTO.class);
  }

  @DeleteMapping("/members/{memberId}")
  public void logOutMemberFromSession(@PathVariable Integer memberId) {
    memberService.removeMember(memberId);
  }
}
