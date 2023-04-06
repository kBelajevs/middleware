package app.dto.request;

import app.domain.UserStoryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqUserStoryDTO {

  private String storyRef;
  private String description;
  private UserStoryStatus status = UserStoryStatus.PENDING;
}
