package app.aspect;

public class VoteEmit {

  private final int totalVotes;
  private final String voter;

  public VoteEmit(int totalVotes, String voter) {
    this.totalVotes = totalVotes;
    this.voter = voter;
  }

  public int getTotalVotes() {
    return totalVotes;
  }

  public String getVoter() {
    return voter;
  }

}
