package app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/reset-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class IntegrationTest {

  public static final String SESSION_ID = "1";
  public static final String NON_EXISTING_SESSION_ID = "45";
  public static final Integer VOTING_USER_STORY_ID = 2;
  public static final Integer VOTING_USER_STORY_ID_ANOTHER = 4;
  public static final Integer EXISTING_MEMBER_ID = 1;
  public static final Integer EXISTING_MEMBER_ID_ANOTHER = 2;
  public static final Integer PENDING_USER_STORY_ID = 1;
  public static final Integer VOTED_USER_STORY_ID = 3;

  @Autowired
  MockMvc mockMvc;

  @MockBean
  SimpMessagingTemplate messagingTemplate;

  @Autowired
  ObjectMapper objectMapper;


}
