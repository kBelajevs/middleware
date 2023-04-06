package app.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "user_story", schema = "pokerDB")
@Getter
@Setter
public class UserStory implements ContainsSession{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "story_ref")
  private String storyRef;

  @Column(name = "description")
  private String description;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStoryStatus status = UserStoryStatus.PENDING;

  @OneToMany(mappedBy = "userStory")
  private Set<Vote> votes = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "session_id")
  private PlanningPokerSession session;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserStory userStory = (UserStory) o;

    if (!id.equals(userStory.id)) {
      return false;
    }
    if (!Objects.equals(description, userStory.description)) {
      return false;
    }
    return status == userStory.status;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + status.hashCode();
    return result;
  }
}
