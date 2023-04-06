package app.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqVoteDTO {

  private Integer memberId;
  private Integer userStoryId;
  private Integer value;
}
