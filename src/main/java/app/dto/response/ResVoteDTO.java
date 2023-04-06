package app.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResVoteDTO {

  private String memberName;
  private Integer userStoryId;
  private String storyRef;
  private Integer value;
}
