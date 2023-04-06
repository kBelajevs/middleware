package app.repository;

import app.domain.PlanningPokerSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokerPlanningSessionRepository extends JpaRepository<PlanningPokerSession, Integer> {

}
