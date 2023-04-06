package app.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import app.domain.PlanningPokerSession;
import app.dto.PlanningPokerSessionDTO;
import app.service.PokerPlanningSessionService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PokerPlanningSessionController {

  private final ModelMapper modelMapper;
  private final PokerPlanningSessionService sessionService;

  @GetMapping("/sessions")
  public List<PlanningPokerSessionDTO> getSessions() {
    var sessions = sessionService.findAll();
    var sessionDtos = modelMapper.map(sessions, PlanningPokerSessionDTO[].class);
    return Arrays.asList(sessionDtos);
  }

  @GetMapping("/sessions/{sessionId}")
  public PlanningPokerSessionDTO getSession(@PathVariable Integer sessionId) {
    var session = sessionService.getSessionByIdOrThrow(sessionId);
    return modelMapper.map(session, PlanningPokerSessionDTO.class);
  }

  @DeleteMapping("/sessions/{sessionId}")
  public void deleteSession(@PathVariable Integer sessionId) {
    sessionService.deleteSession(sessionId);
  }

  @PostMapping("/sessions")
  @ResponseStatus(HttpStatus.CREATED)
  public PlanningPokerSessionDTO createSession(
      @RequestBody PlanningPokerSessionDTO sessionBody) {
    var sessionToSave = modelMapper.map(sessionBody, PlanningPokerSession.class);
    var savedSession = sessionService.createSession(sessionToSave);
    var sessionDto = modelMapper.map(savedSession, PlanningPokerSessionDTO.class);
    sessionDto.add(linkTo(MemberController.class).slash("members").slash(sessionDto.getSessionId()).withRel("joinSession"));
    return sessionDto;
  }
}
