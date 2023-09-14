public class Board {
    int dimension;
    int[][] array;

    Board(int dimension, int[][] arr) {
        this.dimension = dimension;
        array = new int[dimension+1][dimension+1];
        for(int i=0; i<arr.length; i++) {
            System.arraycopy(arr[i], 0, array[i], 0, arr[i].length);
        }
    }

    public int[][] getArray() {
        return array;
    }

    public int getDimension() {
        return dimension;
    }

    public void printBoard() {
        for(int i=1; i<array.length; i++) {
            for(int j=1; j< array.length; j++) {
                if(array[i][j] == -1) System.out.print("* ");
                else System.out.print(array[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("------------------------");
    }

    public boolean isSameBoard(Board board) {
        int[][] tempArr = board.getArray();
        for(int i=1; i<array.length; i++) {
            for(int j=1; j<array.length; j++) {
                if(array[i][j] != tempArr[i][j]) return false;
            }
        }
        return true;
    }

}
