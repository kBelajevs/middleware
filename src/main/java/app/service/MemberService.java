package app.service;


import app.aspect.PlaningPokerSessionUpdate;
import app.domain.Member;
import app.exception.NoContentException;
import app.exception.NotFoundException;
import app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final PokerPlanningSessionService sessionService;
  private final MemberRepository memberRepository;

  @PlaningPokerSessionUpdate
  public Member addMember(Member member, Integer sessionId) {
    var session = sessionService.getSessionByIdOrThrow(sessionId);
    member.setSession(session);
    memberRepository.save(member);
    return member;
  }

  @PlaningPokerSessionUpdate
  public void removeMember(Integer memberId) {
    try {
      var member = getMember(memberId);
      memberRepository.delete(member);
    } catch (Exception e) {
      throw new NoContentException("No user to delete");
    }
  }

  public Member getMember(Integer id){
    return memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
  }

}
