package app.dto.request;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqMemberDTO {

  @NotNull
  private String name;

}
