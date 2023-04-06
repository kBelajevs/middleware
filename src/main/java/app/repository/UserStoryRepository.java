package app.repository;

import app.domain.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Integer> {


}
