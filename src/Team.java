import java.util.ArrayList;

public class Team {
	private int MaxCapacity;
	private ArrayList<String> member;
	private String teamName;
	public Team(int MaxCapacity,String roomName) {
		this.MaxCapacity = MaxCapacity;
		this.teamName = roomName;
		member = new ArrayList<String>();
	}
	
	
	public int getMaxCapacity() {
		return MaxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		MaxCapacity = maxCapacity;
	}


	public String getTeamName(){
		return this.teamName;
	}
	
	public ArrayList<String> getMember() {
		return member;
	}


	public void addMember(String member) {
		this.member.add(member);
	}
	
}
