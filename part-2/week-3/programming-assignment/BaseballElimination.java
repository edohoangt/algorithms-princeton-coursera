import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class BaseballElimination {

    private int numOfTeams;
    private String[] teamNames;
    private Map<String, Integer> teamToId;
    private int[] wArr;
    private int[] lArr;
    private int[] rArr;
    private int[][] gMat;
    private boolean[] isEliminatedArr;
    private int[] maxAllowedNetworkValue;
    private List<List<String>> certificates;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        // Initialize all fields
        this.numOfTeams = in.readInt();
        this.teamNames = new String[numOfTeams];
        this.teamToId = new HashMap<>();
        this.wArr = new int[numOfTeams];
        this.lArr = new int[numOfTeams];
        this.rArr = new int[numOfTeams];
        this.gMat = new int[numOfTeams][numOfTeams];
        this.isEliminatedArr = new boolean[numOfTeams];
        this.maxAllowedNetworkValue = new int[numOfTeams];
        this.certificates = new ArrayList<>(numOfTeams);

        for (int i = 0; i < numOfTeams; i++) {
            teamNames[i] = in.readString();
            teamToId.put(teamNames[i], i);
            wArr[i] = in.readInt();
            lArr[i] = in.readInt();
            rArr[i] = in.readInt();
            for (int j = 0; j < numOfTeams; j++) {
                gMat[i][j] = in.readInt();
            }
        }

        in.close();

        for (int teamId = 0; teamId < numOfTeams; teamId++) {
            int trivialCheck = isTriviallyEliminated(teamId);
            if (trivialCheck != -1) {
                isEliminatedArr[teamId] = true;
                certificates.add(List.of(teamNames[trivialCheck]));
                continue;
            }

            FlowNetwork network = createFlowNetwork(teamId);
            FordFulkerson ff = new FordFulkerson(network, 0, network.V()-1);
            isEliminatedArr[teamId] = ff.value() < maxAllowedNetworkValue[teamId];

            if (isEliminatedArr[teamId]) {
                int teamVertexStart = network.V() - 1 - (numOfTeams - 1);
                List<String> certificate = new ArrayList<>();
                for (int j = 0; j < numOfTeams - 1; j++) {
                    if (ff.inCut(teamVertexStart + j))
                        certificate.add(teamNames[teamVertexPosToId(teamId, j)]);
                }
                certificates.add(certificate);
            } else {
                certificates.add(null);
            }
        }
    }

    private int isTriviallyEliminated(int teamId) {
        int maxScore = wArr[teamId] + rArr[teamId];
        for (int i = 0; i < numOfTeams; i++) {
            if (i == teamId) continue;
            if (wArr[i] > maxScore) {
                return i;
            }
        }
        return -1;
    }

    private FlowNetwork createFlowNetwork(int curId) {
        int numGameVertices = (numOfTeams-1)*(numOfTeams-2)/2;
        int numTeamVertices = numOfTeams-1;
        int networkSize = 1 + numTeamVertices + numGameVertices + 1;
        FlowNetwork network = new FlowNetwork(networkSize);

        int curGameVertex = 1;
        int maxNetworkValue = 0;
        for (int r = 0; r < gMat.length; r++) {
            if (r == curId) continue;
            for (int c = r+1; c < gMat[0].length; c++) {
                if (c == curId) continue;
                network.addEdge(new FlowEdge(0, curGameVertex++, gMat[r][c]));
                maxNetworkValue += gMat[r][c];
            }
        }
        maxAllowedNetworkValue[curId] = maxNetworkValue;

        int curTeamVertex = 0;
        for (int id = 0; id < numOfTeams; id++) {
            if (id == curId) continue;
            int teamVerticesStart = 1+numGameVertices;
            network.addEdge(new FlowEdge(teamVerticesStart+curTeamVertex++, networkSize-1, Math.max(wArr[curId] + rArr[curId] - wArr[id], 0)));
        }

        curGameVertex = 1;
        int teamVertexStart = 1 + numGameVertices;
        for (int i = 0; i < numOfTeams-1; i++) {
            for (int j = i+1; j < numOfTeams-1; j++) {
                network.addEdge(new FlowEdge(curGameVertex, teamVertexStart+i, Double.MAX_VALUE));
                network.addEdge(new FlowEdge(curGameVertex, teamVertexStart+j, Double.MAX_VALUE));
                curGameVertex += 1;
            }
        }
        return network;
    }

    private int teamVertexPosToId(int teamId, int teamVertexPos) {
        return teamVertexPos < teamId ? teamVertexPos : teamVertexPos + 1;
    }

    // number of teams
    public int numberOfTeams() {
        return this.numOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(this.teamNames);
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeamName(team);

        int id = teamToId.get(team);
        return wArr[id];
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeamName(team);

        int id = teamToId.get(team);
        return lArr[id];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeamName(team);

        int id = teamToId.get(team);
        return rArr[id];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeamName(team1);
        validateTeamName(team2);

        int id1 = teamToId.get(team1);
        int id2 = teamToId.get(team2);
        return gMat[id1][id2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeamName(team);

        int id = teamToId.get(team);
        return isEliminatedArr[id];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeamName(team);

        int id = teamToId.get(team);
        return certificates.get(id);
    }

    private void validateTeamName(String teamName) {
        if (!teamToId.containsKey(teamName)) {
            throw new IllegalArgumentException("Team name '" + teamName + "' is invalid.");
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams5.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
