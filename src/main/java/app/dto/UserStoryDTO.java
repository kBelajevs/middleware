package app.dto;

import app.domain.UserStoryStatus;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserStoryDTO {

  private Integer id;
  private String storyRef;
  private String description;
  @NotNull
  private UserStoryStatus status;
}
