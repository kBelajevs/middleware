package app.dto.request;

import app.domain.UserStoryStatus;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqUserStoryDTO {

  private String storyRef;
  @NotNull
  private String description;
  @NotNull
  private UserStoryStatus status;
}
