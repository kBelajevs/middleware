package app.dto.request;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqPlanningPokerSessionDTO {

  @NotNull
  private String title;
  private String deckType;
}
