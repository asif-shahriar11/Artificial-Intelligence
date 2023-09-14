import java.util.ArrayList;

public class Node implements Comparable<Node> {
    private Board board;
    private Board finalBoard;
    private int f;
    private int g;
    private int h;
    private Node parent;


    Node(Board board, Board finalBoard, Node parent ,boolean isHamming) {
        this.board = board;
        this.finalBoard = finalBoard;
        this.parent = parent;
        if(isHamming) setHammingVal();
        else setManhattanVal();
    }

    public Board getBoard() { return board; }

    public void setBoard(Board board) { this.board = board; }

    public int getF() { return f; }

    public void setF(int f) { this.f = f; }

    public int getG() { return g; }

    public void setG(int g) { this.g = g; }

    public int getH() { return h; }

    public void setH(int h) { this.h = h; }

    public Node getParent() { return parent; }

    public void setParent(Node parent) { this.parent = parent; }

    public void setHammingVal() {
        if(parent == null) g = 0;
        else g = parent.getG() + 1;
        int[][] arr = board.getArray();
        int[][] finalArray = finalBoard.getArray();
        int ham = 0;
        for(int i=1; i<arr.length; i++) {
            for(int j=1; j<arr.length; j++) {
                if(arr[i][j] != finalArray[i][j] && arr[i][j] != -1)   ham += 1;
            }
        }
        h = ham;
        f = g + h;
    }

    public void setManhattanVal() {
        if(parent == null) g = 0;
        else g = parent.getG() + 1;
        int[][] arr = board.getArray();
        int[][] finalArray = finalBoard.getArray();
        int dim = finalBoard.getDimension();
        int goalRow, goalCol;
        int man = 0;
        for(int i=1; i<arr.length; i++) {
            for(int j=1; j<arr.length; j++) {
                if(arr[i][j] != -1) {
                    goalRow = (int) Math.ceil(arr[i][j]*1.0/dim);
                    goalCol = arr[i][j] % dim;
                    if(goalCol == 0) goalCol = dim;
                    man += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                }
            }
        }
        h = man;
        f = g + h;
    }




    public ArrayList<Node> getNeighbors(boolean isHamming) {
        int[][] arr = this.getBoard().getArray();
        int[][] tempArr = new int[arr.length][arr.length];
        int row = 0, col = 0;
        int dim = this.getBoard().getDimension();
        for(int i=1; i< arr.length; i++) {
            for(int j=1; j< arr.length; j++) {
                if(arr[i][j] == -1) {
                    row = i;
                    col = j;
                }
                tempArr[i][j] = arr[i][j];
            }
        }
        ArrayList<Node> neighbors = new ArrayList<>();
        int retVal1, retVal2;
        if(col != dim) {
            // left-move
            retVal1 = tempArr[row][col];
            retVal2 = tempArr[row][col+1];
            tempArr[row][col] = arr[row][col+1];
            tempArr[row][col+1] = -1;
            neighbors.add(new Node(new Board(dim, tempArr), finalBoard, this, isHamming));
            tempArr[row][col] = retVal1;
            tempArr[row][col+1] = retVal2;
        }
        if(col != 1) {
            // right-move
            retVal1 = tempArr[row][col];
            retVal2 = tempArr[row][col-1];
            tempArr[row][col] = arr[row][col-1];
            tempArr[row][col-1] = -1;
            neighbors.add(new Node(new Board(dim, tempArr), finalBoard, this, isHamming));
            tempArr[row][col] = retVal1;
            tempArr[row][col-1] = retVal2;
        }
        if(row != dim) {
            // up-move
            retVal1 = tempArr[row][col];
            retVal2 = tempArr[row+1][col];
            tempArr[row][col] = arr[row+1][col];
            tempArr[row+1][col] = -1;
            neighbors.add(new Node(new Board(dim, tempArr), finalBoard, this, isHamming));
            tempArr[row][col] = retVal1;
            tempArr[row+1][col] = retVal2;
        }
        if(row != 1) {
            // down-move
            retVal1 = tempArr[row][col];
            retVal2 = tempArr[row-1][col];
            tempArr[row][col] = arr[row-1][col];
            tempArr[row-1][col] = -1;
            neighbors.add(new Node(new Board(dim, tempArr), finalBoard, this, isHamming));
            tempArr[row][col] = retVal1;
            tempArr[row-1][col] = retVal2;
        }


        return neighbors;
    }


    @Override
    public int compareTo(Node node) {
        return (int) (this.f-node.f);
    }
}
