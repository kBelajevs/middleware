package app.dto.response;

import java.util.List;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ResPlanningPokerSessionDTO extends RepresentationModel<ResPlanningPokerSessionDTO> {

  private Integer sessionId;
  private String title;
  private String deckType;
  private List<ResUserStoryDTO> stories;
  private List<ResMemberDTO> members;

}
