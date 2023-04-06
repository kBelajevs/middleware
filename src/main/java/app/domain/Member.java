package app.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MEMBER", schema = "pokerDB")
@Getter
@Setter
public class Member implements ContainsSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "session_id")
  private PlanningPokerSession session;

  @OneToMany
  @JoinColumn(name = "member_id")
  private Set<Vote> votes = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Member member = (Member) o;

    if (!id.equals(member.id)) {
      return false;
    }
    return name.equals(member.name);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }
}
