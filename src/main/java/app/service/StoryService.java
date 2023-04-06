package app.service;

import app.aspect.PlaningPokerSessionUpdate;
import app.domain.UserStory;
import app.domain.UserStoryStatus;
import app.exception.ActionForbiddenException;
import app.exception.NoContentException;
import app.exception.NotFoundException;
import app.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryService {

  private final PokerPlanningSessionService sessionService;
  private final UserStoryRepository storyRepository;

  @PlaningPokerSessionUpdate
  public UserStory addStory(UserStory userStory, Integer sessionId) {
    var session = sessionService.getSessionByIdOrThrow(sessionId);
    userStory.setSession(session);
    storyRepository.save(userStory);
    return userStory;
  }

  @PlaningPokerSessionUpdate
  public UserStory removeStory(Integer storyId) {
    var storyToDelete = storyRepository.findById(storyId)
        .orElseThrow(() -> new NoContentException("Story to delete not found"));
    if (UserStoryStatus.PENDING.equals(storyToDelete.getStatus())) {
      storyRepository.delete(storyToDelete);
      return storyToDelete;
    } else {
      throw new ActionForbiddenException("Only PENDING stories allowed to be deleted");
    }
  }

  public UserStory getStoryById(Integer id) {
    return storyRepository.findById(id).orElseThrow(() -> new NotFoundException("Story not found"));
  }

  public UserStory openVoting(Integer id) {
    var story = getStoryById(id);
    story.setStatus(UserStoryStatus.VOTING);
    storyRepository.save(story);
    return story;
  }

  public UserStory closeVoting(Integer id) {
    var story = getStoryById(id);
    story.setStatus(UserStoryStatus.VOTED);
    storyRepository.save(story);
    return story;
  }

}
