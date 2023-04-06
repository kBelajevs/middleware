package app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteDTO {

  private Integer id;
  private Integer memberId;
  private Integer userStoryId;
  private Integer value;
}
