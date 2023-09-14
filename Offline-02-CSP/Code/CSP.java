import java.util.ArrayList;

public class CSP {
    private ArrayList<Variable> variables;
    private ArrayList<Constraint> constraints;

    CSP() {
        variables = new ArrayList<>();
        constraints = new ArrayList<>();
    }

    void addVariable(Variable var) { variables.add(var); }

    Variable getVariable(int row, int col) {
        for(Variable var:variables) {
            if((var.getRow() == row) && (var.getCol() == col)) return var;
        }
        return null;
    }

    void addConstraint(Constraint con) { constraints.add(con); }

    Constraint getConstraint(int idx) { return constraints.get(idx); }

    boolean isComplete() {
        for(Variable var:variables) {
            if(!var.isValued()) return false;
        }
        return true;
    }

    ArrayList<Variable> getVariablesList() { return variables; }

}
