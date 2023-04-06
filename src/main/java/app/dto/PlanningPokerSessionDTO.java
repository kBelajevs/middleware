package app.dto;

import java.util.List;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class PlanningPokerSessionDTO extends RepresentationModel<PlanningPokerSessionDTO> {

  private Integer sessionId;
  private String title;
  private String deckType;
  private List<UserStoryDTO> stories;
  private List<MemberDTO> members;

}
