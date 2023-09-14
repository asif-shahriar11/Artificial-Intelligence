import java.util.ArrayList;
import java.util.Collections;

public class CSPSolver {
    private VariableHeuristic vh;
    private int N;
    private CSP csp;
    private int[][] sudoku;
    private int nodes;
    private int backtracks;
    private int faultRow, faultCol;

    private int choice;

    CSPSolver(int[][] sudoku, int N, int choice) {
        this.N = N;
        this.sudoku = sudoku;
        this.choice = choice;
        vh = new VariableHeuristic();
        csp = new CSP();
        nodes = 0;
        backtracks = 0;

        faultRow = 0;
        faultCol = 0;

        initialize();
    }

    boolean isAllowed(int val, int row, int col) {
        for(int i=1; i<=N; i++) {
            if(sudoku[row][i] == val) return false;
        }
        for(int i=1; i<=N; i++) {
            if(sudoku[i][col] == val) return false;
        }
        return true;
    }


    void initialize() {
        // creating variables
        Variable var;
        for(int i=1; i<=N; i++) {
            for(int j=1; j<=N; j++) {
                var = new Variable(i, j);
                if(sudoku[i][j] != 0) {
                    var.addToDomain(sudoku[i][j]);
                    var.setValue(sudoku[i][j]);
                }
                else {
                    for(int k=1; k<=N; k++) {
                        if(isAllowed(k, i, j)) var.addToDomain(k);
                    }
                    csp.addVariable(var);
                    vh.addVariable(var);
                }

            }
        }
        // all variables created and added to CSP
        // creating constraints - one constraint for each row and each col
        Constraint con;
        for(int i=1; i<=N; i++) {
            con = new Constraint();
            for(int j=1; j<=N; j++) {
                if(sudoku[i][j] == 0) con.addVariable(csp.getVariable(i, j));
            }
            csp.addConstraint(con);
        }
        for(int j=1; j<=N; j++) {
            con = new Constraint();
            for(int i=1; i<=N; i++) {
                if(sudoku[i][j] == 0) con.addVariable(csp.getVariable(i, j));
            }
            csp.addConstraint(con);
        }
        // all constraints created and added to CSP
    }

    public int getNodes() { return nodes; }

    public int getBacktracks() { return backtracks; }



    boolean infer2(int val, int row, int col) {
        // removing val from the domain of all variables in that row and in that col
        Variable var;
        for(int i=1; i<=N; i++) {
            if(i != col) {
                var = csp.getVariable(row, i);
                if(var != null) {
                    var.removeFromDomain(val);
                    if(var.getDomain().size() == 0) {
                        faultCol = i;
                        return false;
                    }
                }
            }
        }
        for(int i=1; i<=N; i++) {
            if(i != row) {
                var = csp.getVariable(i, col);
                if(var != null) {
                    var.removeFromDomain(val);
                    if(var.getDomain().size() == 0) {
                        faultRow = i;
                        return false;
                    }
                }
            }
        }
        return true;
    }

    void infer(int val, int row, int col) {
        // removing val from the domain of all variables in that row and in that col
        Variable var;
        for(int i=1; i<=N; i++) {
            if(i != col) {
                var = csp.getVariable(row, i);
                if(var != null) var.removeFromDomain(val);
            }
        }
        for(int i=1; i<=N; i++) {
            if(i != row) {
                var = csp.getVariable(i, col);
                if(var != null) var.removeFromDomain(val);
            }
        }
    }

    void addBack(int val, int row, int col) {
        // adding back the domain of all variables in that row and in that col
        Variable var;

        System.out.println("add back: "+ val +" ("+row+", "+col+")");
        for(int i=1; i<=N; i++) {
            if(i != col) {
                var = csp.getVariable(row, i);
                if(isAllowed(val, row, i)) {
                    if(var != null && !(var.getDomain().contains(val)))
                        var.addToDomain(val);
                }
            }
        }
        for(int i=1; i<=N; i++) {
            if(i != row) {
                var = csp.getVariable(i, col);
                if(isAllowed(val, i, col)) {
                    if(var != null && !(var.getDomain().contains(val))) var.addToDomain(val);
                }

            }
        }


    }

    void addBack2(int val, int row, int col) {
        // adding back the domain of all variables in that row and in that col
        Variable var;


        System.out.println("add back: "+ val +" ("+row+", "+col+")");
        for(int i=1; i<=faultCol; i++) {
            if(i != col) {
                var = csp.getVariable(row, i);
                if(isAllowed(val, row, i)) {
                    if(var != null && !(var.getDomain().contains(val)))
                        var.addToDomain(val);
                }
            }
        }
        for(int i=1; i<=faultRow; i++) {
            if(i != row) {
                var = csp.getVariable(i, col);
                if(isAllowed(val, i, col)) {
                    if(var != null && !(var.getDomain().contains(val))) var.addToDomain(val);
                }

            }
        }


    }



    boolean backtrackSolve() {
        if(csp.isComplete()) return true;
        Variable var = vh.getNextVariable(choice);
        if(var == null) return true;
        nodes++;
        //System.out.println("Selected first: "+ var.getRow()+", "+ var.getCol());
        int row = var.getRow();
        int col = var.getCol();
        //System.out.println(row+" "+col);
        Constraint c1 = csp.getConstraint(row-1);
        Constraint c2 = csp.getConstraint(N+col-1);
        ArrayList<Integer> dom = var.getDomain();
        //Collections.sort(dom);
        for(int i=0; i<dom.size(); i++) {
            int val = dom.get(i);
            //System.out.println("Val: "+val);
            if(c1.holds(val) && (c2.holds(val))) {
                // consistent
                var.setValue(val);
                infer(val, row, col);
                //showCSPSolved();
                //System.out.println("-------------------");
                boolean result = backtrackSolve();
                if(result) return true; // success
                if(var.isValued()) {
                    var.removeValue();
                    addBack(val, row, col);
                }
            }
            //var.removeValue();
            //addBack(val, row, col);
        }
        vh.addVariable(var);
        backtracks++;
        return false;
    }


    boolean forwardCheckingSolve() {
        //if(csp.isComplete()) return true;
        Variable var = vh.getNextVariable(choice);
        if(var == null) return true;
        nodes++;
        //System.out.println("Selected first: "+ var.getRow()+", "+ var.getCol());
        int row = var.getRow();
        int col = var.getCol();
        //System.out.println(row+" "+col);
        Constraint c1 = csp.getConstraint(row-1);
        Constraint c2 = csp.getConstraint(N+col-1); // trust me (I don't but ok)
        ArrayList<Integer> dom = var.getDomain();
        //Collections.sort(dom);
        for(int i=0; i<dom.size(); i++) {
            int val = dom.get(i);
            //System.out.println("Val: "+val);
            if(c1.holds(val) && (c2.holds(val))) {
                // consistent
                var.setValue(val);

                boolean isAllowed = infer2(val, row, col);


                if(!isAllowed) {
                    var.removeValue();
                    addBack2(val, row, col);
                    vh.addVariable(var);
                    backtracks++;
                    return false;
                }
                boolean result = forwardCheckingSolve();
                if(result) return true; // success
                if(var.isValued()) {
                    var.removeValue();
                    addBack(val, row, col);
                }


            }
            //var.removeValue();
            //addBack(val, row, col);
        }
        vh.addVariable(var);
        backtracks++;
        return false;
    }




    void showCSPSolved() {
        //ArrayList<Variable> varList = csp.getVariablesList();
        for(int i=1; i<=N; i++) {
            for(int j=1; j<=N; j++) {
                Variable var = csp.getVariable(i, j);
                if(var == null) System.out.print(sudoku[i][j]+" ");
                else System.out.print(var.getValue()+" ");
            }
            System.out.println();
        }
        System.out.println("Nodes: "+ nodes);
        System.out.println("Backtracks: " + backtracks);
        System.out.println("==============================");
    }
}
