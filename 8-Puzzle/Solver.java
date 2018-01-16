import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private SearchNode currentNode;
    private SearchNode twinCurrentNode;
    private Stack<Board> solution;

    private class SearchNode implements Comparable<SearchNode> {
        public Board board;
        public int moves;
        public SearchNode preSearchNode;
        public int priority;

        public SearchNode(Board currentBoard, SearchNode currentPreSearchNode) {
            board = currentBoard;
            preSearchNode = currentPreSearchNode;

            if (currentPreSearchNode == null) {
                moves = 0;
            } else {
                moves = currentPreSearchNode.moves + 1;
            }

            priority = moves + board.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        currentNode = new SearchNode(initial, null);
        twinCurrentNode = new SearchNode(initial.twin(), null);

        MinPQ<SearchNode> priorityQueue = new MinPQ<>();
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<>();
        priorityQueue.insert(currentNode);
        twinPriorityQueue.insert(twinCurrentNode);

        while (true) {
            currentNode = priorityQueue.delMin();
            if (currentNode.board.isGoal()) {
                break;
            } else {
                putNeighborsInPQ(currentNode, priorityQueue);
            }

            twinCurrentNode = twinPriorityQueue.delMin();
            if (twinCurrentNode.board.isGoal()) {
                break;
            } else {
                putNeighborsInPQ(twinCurrentNode, twinPriorityQueue);
            }
        }

    }

    private void putNeighborsInPQ(SearchNode searchNode, MinPQ<SearchNode> pq) {
        Iterable<Board> neighbors = searchNode.board.neighbors();

        for (Board neighbor : neighbors) {
            if (searchNode.preSearchNode == null || !neighbor.equals(searchNode.preSearchNode.board)) {
                pq.insert(new SearchNode(neighbor, searchNode));
            }
        }
    }

    public boolean isSolvable() {
        return currentNode.board.isGoal();
    }

    public int moves() {
        if (currentNode.board.isGoal()) {
            return currentNode.moves;
        } else {
            return -1;
        }
    }

    public Iterable<Board> solution() {
        if (currentNode.board.isGoal()) {
            solution = new Stack<>();
            SearchNode node = currentNode;

            while (node != null) {
                solution.push(node.board);
                node = node.preSearchNode;
            }

            return solution;
        } else {
            return null;
        }
    }

}
