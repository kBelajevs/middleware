package app.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {


  private Integer memberId;
  @NotNull
  private String name;

}
