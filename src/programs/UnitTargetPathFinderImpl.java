package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {
            {0, 1}, {0, -1}, {1, 0}, {-1, 0},
            {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Set<Node> occupiedCells = new HashSet<>();
        for (Unit unit: existingUnitList) {
            if (!(unit.getxCoordinate() == attackUnit.getxCoordinate() &&
                    unit.getyCoordinate() == attackUnit.getyCoordinate()) &&
                    !(unit.getxCoordinate() == targetUnit.getxCoordinate() &&
                            unit.getyCoordinate() == targetUnit.getyCoordinate())) {
                occupiedCells.add(new Node(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }

        return aStarPathSearch(new Node(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()),
                new Node(targetUnit.getxCoordinate(), targetUnit.getyCoordinate()),
                occupiedCells);
    }

    private List<Edge> aStarPathSearch(Node start, Node end, Set<Node> obstacles) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getWeight));

        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Integer> gScore = new HashMap<>();
        Map<Node, Integer> fScore = new HashMap<>();

        gScore.put(start, 0);
        fScore.put(start, heuristic(start, end));
        start.weight = fScore.get(start);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (currentNode.equals(end)) {
                return reconstructPath(cameFrom, currentNode);
            }

            for (int[] dir: DIRECTIONS) {
                int x = currentNode.x + dir[0];
                int y = currentNode.y + dir[1];

                if (!isValid(x, y)) {
                    continue;
                }

                Node neighbor = new Node(x, y);
                if (obstacles.contains(neighbor)) {
                    continue;
                }

                int moveCost = (dir[0] != 0 && dir[1] != 0) ? 14 : 10;
                int tentativeGScore = gScore.getOrDefault(currentNode, Integer.MAX_VALUE) + moveCost;

                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, currentNode);
                    gScore.put(neighbor, tentativeGScore);
                    int weight = tentativeGScore + heuristic(neighbor, end);
                    fScore.put(neighbor, weight);
                    neighbor.weight = weight;
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Edge> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        List<Edge> path = new ArrayList<>();
        path.add(new Edge(current.x, current.y));

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.addFirst(new Edge(current.x, current.y));
        }

        return path;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private int heuristic(Node a, Node b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);

        return 10 * Math.max(dx, dy);
    }

    private static class Node {
        int x;
        int y;
        int weight;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.weight = Integer.MAX_VALUE;
        }

        int getWeight() { return weight; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
