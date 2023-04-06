package app.aspect;

import app.domain.ContainsSession;
import app.dto.response.ResPlanningPokerSessionDTO;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PlaningPokerSessionUpdatedAspect {

  private final SimpMessagingTemplate messagingTemplate;
  private final ModelMapper modelMapper;

  @AfterReturning(pointcut = "@annotation(PlaningPokerSessionUpdate)", returning = "result")
  public void afterMyAnnotation(Object result) {
    ContainsSession containsSession = (ContainsSession) result;
    var session = containsSession.getSession();
    var sessionDto = modelMapper.map(session, ResPlanningPokerSessionDTO.class);
    messagingTemplate.convertAndSend("/topic/session/" + session.getId(),
        sessionDto);
  }
}
