package app.service;


import app.domain.UserStoryStatus;
import app.domain.Vote;
import app.exception.ActionForbiddenException;
import app.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

  private final VoteRepository voteRepository;
  private final StoryService storyService;
  private final MemberService memberService;

  public Vote vote(Integer memberId, Integer storyId, Integer value) {
    var story = storyService.getStoryById(storyId);
    if (!UserStoryStatus.VOTING.equals(story.getStatus())) {
      throw new ActionForbiddenException("Voting not allowed for this story");
    }

    var member = memberService.getMember(memberId);
    voteRepository.findByMemberAndUserStory(member, story).ifPresent(it -> {
      throw new ActionForbiddenException("this member already voted for this story");
    });

    var vote = new Vote();
    vote.setMember(member);
    vote.setUserStory(story);
    vote.setValue(value);

    return voteRepository.save(vote);
  }
}
