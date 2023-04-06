package app.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vote", schema = "pokerDB")
@Getter
@Setter
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "value", nullable = false)
  private Integer value;

  @ManyToOne
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Vote vote = (Vote) o;

    if (!id.equals(vote.id)) {
      return false;
    }
    return value.equals(vote.value);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + value.hashCode();
    return result;
  }
}
