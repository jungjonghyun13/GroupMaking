import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TaskManager {
	@SuppressWarnings("unchecked")
	//각 팀에 사람들 중복안되기 배정
	void setTeam(ArrayList<Team> rooms, HashMap<String, Set<String>> nameList) {

		for (int i = 0; i < rooms.size(); i++) {
	
			HashMap<String, Set<String>> cs = null;
			ByteArrayOutputStream byteArrOs;
			ObjectOutputStream objOs;
			ByteArrayInputStream byteArrIs;
			ObjectInputStream objIs;
			Object deepCopy;

			byteArrOs = new ByteArrayOutputStream();
			try {
				objOs = new ObjectOutputStream(byteArrOs);
				objOs.writeObject(nameList);
				byteArrIs = new ByteArrayInputStream(byteArrOs.toByteArray());
				objIs = new ObjectInputStream(byteArrIs);
				deepCopy = objIs.readObject();
				cs = (HashMap<String, Set<String>>) deepCopy;
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while ((rooms.get(i).getMember().size() < rooms.get(i).getMaxCapacity()) && cs.size() > 0) {
				Random generator = new Random();
				ArrayList<String> keys = new ArrayList<String>(cs.keySet());
				String randomPeople = keys.get(generator.nextInt(keys.size()));
				boolean overlap = false;

				for (String p : rooms.get(i).getMember()) {
					if (nameList.get(randomPeople).contains(p)) {
						overlap = true;
						break;
					}
				}
				if (!overlap) {
					rooms.get(i).addMember(randomPeople);
				}
				cs.remove(randomPeople);


			}

			for (String p : rooms.get(i).getMember()) {
				nameList.remove(p);
			}
		

			while ((rooms.get(i).getMember().size() < rooms.get(i).getMaxCapacity()) && nameList.size() > 0) {
				Random generator = new Random();
				ArrayList<String> keys = new ArrayList<String>(nameList.keySet());

				String randomPeople = keys.get(generator.nextInt(keys.size()));

				rooms.get(i).addMember(randomPeople);
				nameList.remove(randomPeople);
	
			}


		}
	}
	
	//각자 어떤 팀에 배정받았는지 반환하는 함수
	HashMap<String, String> teamForHuman(ArrayList<Team> teams){
		HashMap<String, String> teamInfo = new HashMap<>();
		for(int i = 0; i < teams.size(); i++){
			for(int j = 0; j < teams.get(i).getMember().size(); j++){
				System.out.println("teamName : " + teams.get(i).getTeamName());
				teamInfo.put(teams.get(i).getMember().get(j), teams.get(i).getTeamName());
			}
		}
		return teamInfo;
	}
}
