package app.dto.response;

import app.domain.UserStoryStatus;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResUserStoryDTO {

  private Integer id;
  private String storyRef;
  private String description;
  private UserStoryStatus status;
}
