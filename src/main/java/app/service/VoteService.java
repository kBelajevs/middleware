package app.service;


import app.domain.UserStoryStatus;
import app.domain.Vote;
import app.exception.ActionForbiddenException;
import app.repository.VoteRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

  private final VoteRepository voteRepository;
  private final StoryService storyService;
  private final MemberService memberService;

  public Set<Vote> getVotes(Integer storyId) {
    var story = storyService.getStoryById(storyId);
    return story.getVotes();
  }

  public Vote vote(Integer memberId, Integer storyId, Integer value) {
    var member = memberService.getMember(memberId);
    var story = storyService.getStoryById(storyId);

    if (!UserStoryStatus.VOTING.equals(story.getStatus())) {
      throw new ActionForbiddenException("Voting not allowed for this story");
    }

    var vote = new Vote();
    vote.setMember(member);
    vote.setUserStory(story);
    vote.setValue(value);

    return voteRepository.save(vote);
  }
}
