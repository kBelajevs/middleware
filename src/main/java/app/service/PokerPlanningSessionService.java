package app.service;


import app.domain.PlanningPokerSession;
import app.exception.NoContentException;
import app.exception.NotFoundException;
import app.repository.PokerPlanningSessionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PokerPlanningSessionService {

  private final PokerPlanningSessionRepository sessionRepository;

  public PlanningPokerSession createSession(PlanningPokerSession session) {
    return sessionRepository.save(session);
  }

  public void deleteSession(Integer sessionId) {
    try {
      sessionRepository.deleteById(sessionId);
    } catch (Exception e) {
      throw new NoContentException("No session to delete");
    }
  }

  public List<PlanningPokerSession> findAll() {
    return sessionRepository.findAll();
  }

  public PlanningPokerSession getSessionByIdOrThrow(Integer sessionId) {
    return sessionRepository.findById(sessionId)
        .orElseThrow(() -> new NotFoundException("Session not found"));
  }
}
