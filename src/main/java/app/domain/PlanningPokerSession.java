package app.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "planning_poker_session", schema = "pokerDB")
@Getter
@Setter
public class PlanningPokerSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "title")
  private String title;

  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<Member> members = new HashSet<>();

  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<UserStory> userStories = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlanningPokerSession that = (PlanningPokerSession) o;

    if (!id.equals(that.id)) {
      return false;
    }
    return title.equals(that.title);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + title.hashCode();
    return result;
  }
}
