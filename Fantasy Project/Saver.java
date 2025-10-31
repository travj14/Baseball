import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Saver {

    public Saver() {

    }

    public boolean saveLeague(LeagueInfo league) {
        String filePath = "leagueData.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("name,numTeams,budget,maxRosterN,startN\n");

            writer.append(league.getLeagueName() + "," + league.getNumTeams() + "," + 
                                league.getBudget() + "," + league.getRosterSizes() + "," + league.getStartSize());
            
            writer.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LeagueInfo getLeague() {
        String filePath = "leagueData.csv";

        try (FileReader reader = new FileReader(filePath)) {

            String[] info = reader.toString().split("\n")[1].split(",");
            reader.close();

            LeagueInfo league = new LeagueInfo(info[0], info[1], info[2], info[3], info[4]);
            
            return league;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveTeams(ArrayList<Team> teams) {
        String filePath = "teamData.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("teamID,teamName,ownerUser,ownerPass\n");

            for (Team team : teams) {
                writer.append(team.getTeamID() + "," + team.getName() + "," + team.getUserName() + "," + team.getPassWord() + "\n");
            }

            writer.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Team> getTeams() {
        String filePath = "teamData.csv";
        ArrayList<Team> teams = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {

            String[] teamsInfo = reader.toString().split("\n");
            Team team;

            for (int i = 1; i < teamsInfo.length; i++) {
                String[] teamInfo = teamsInfo[i].split(",");

                team = new Team();
                team.OverrideTeamID(Integer.parseInt(teamInfo[0]));
                team.setName(teamInfo[1]);
                team.setUserName(teamInfo[2]);
                team.setPassWord(teamInfo[3]);

                teams.add(team);
            }


            return teams;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean savePlayers(ArrayList<Player> players) {
        String filePath = "playerData.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("playerID,playerFirst,playerLast,prefix,teamID,age,height,weight,position,NFLTeam,draftRound,originalTeam,draftYear,pickInRound\n");

            for (Player player : players) {
                writer.append(player.getPlayerID() + ",");

                writer.append(player.getFirstName() + ",");
                writer.append(player.getLastName() + ",");
                writer.append(player.getSuffix() + ",");
                writer.append(player.getTeamID() + ",");
                writer.append(player.getAge() + ",");
                writer.append(player.getHeight() + ",");
                writer.append(player.getWeight() + ",");
                writer.append(player.getPos() + ",");
                writer.append(player.getNFLTeam() + ",");
                
            }

            writer.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
