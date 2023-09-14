import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        LinkedList<String> fileInput = new LinkedList<>();
        int N = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("input.txt"));
            fileInput.add(br.readLine()); // first line : N
            N = Integer.parseInt(fileInput.get(0));
            for(int i=1; i<=N; i++) fileInput.add(br.readLine());
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[][] sudoku = new int[N+1][N+1];

        for(int i=0; i<=N; i++) {
            sudoku[i][0] = -1;
            sudoku[0][i] = -1;
        }

        String[] inputs;
        for(int i=1; i<=N; i++) {
            inputs = fileInput.get(i).split(" ");
            for(int j=1; j<=N; j++) sudoku[i][j] = Integer.parseInt(inputs[j-1]);
        }

        CSPSolver cspSolver = new CSPSolver(sudoku, N, 1);


        long startTime = System.currentTimeMillis();
        System.out.println(cspSolver.backtrackSolve());
        //System.out.println(cspSolver.forwardCheckingSolve());
        long finishTime = System.currentTimeMillis();
        cspSolver.showCSPSolved();
        System.out.println("Time: " + (finishTime - startTime));



    }
}
