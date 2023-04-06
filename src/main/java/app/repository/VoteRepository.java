package app.repository;

import app.domain.Member;
import app.domain.UserStory;
import app.domain.Vote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

  Optional<Vote> findByMemberAndUserStory(Member member, UserStory userStory);
}
