package app.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import app.domain.PlanningPokerSession;
import app.dto.request.ReqPlanningPokerSessionDTO;
import app.dto.response.ResPlanningPokerSessionDTO;
import app.service.PokerPlanningSessionService;
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

  @GetMapping("/sessions/{sessionId}")
  public ResPlanningPokerSessionDTO getSession(@PathVariable Integer sessionId) {
    var session = sessionService.getSessionByIdOrThrow(sessionId);
    return modelMapper.map(session, ResPlanningPokerSessionDTO.class);
  }

  @DeleteMapping("/sessions/{sessionId}")
  public void deleteSession(@PathVariable Integer sessionId) {
    sessionService.deleteSession(sessionId);
  }

  @PostMapping("/sessions")
  @ResponseStatus(HttpStatus.CREATED)
  public ResPlanningPokerSessionDTO createSession(
      @RequestBody ReqPlanningPokerSessionDTO sessionBody) {
    var sessionToSave = modelMapper.map(sessionBody, PlanningPokerSession.class);
    var savedSession = sessionService.createSession(sessionToSave);
    var sessionDto = modelMapper.map(savedSession, ResPlanningPokerSessionDTO.class);

    sessionDto.add(linkTo(MemberController.class)
        .slash("members")
        .slash(sessionDto.getSessionId())
        .withRel("joinSession"));

    sessionDto.add(linkTo(PokerPlanningSessionController.class)
        .slash("sessionSocket")
        .slash("planning-poker-websocket").
        withRel("sessionSocket"));

    sessionDto.add(linkTo(PokerPlanningSessionController.class)
        .slash("sessionSocket")
        .slash("topic")
        .slash("session")
        .slash(sessionDto.getSessionId())
        .withRel("sessionUpdateTopic"));

    sessionDto.add(linkTo(PokerPlanningSessionController.class)
        .slash("sessionSocket")
        .slash("topic")
        .slash("session")
        .slash(sessionDto.getSessionId())
        .slash("vote-emit")
        .withRel("voteEmitTopic"));

    sessionDto.add(linkTo(PokerPlanningSessionController.class)
        .slash("sessionSocket")
        .slash("topic")
        .slash("session")
        .slash(sessionDto.getSessionId())
        .slash("vote-finish")
        .withRel("voteFinishedTopic"));

    return sessionDto;
  }
}
