import java.util.ArrayList;

public class Constraint {
    private ArrayList<Variable> scope;

    Constraint() {
        scope = new ArrayList<>();
    }

    void addVariable(Variable var) { scope.add(var); }

    boolean holds(int val) {
        for(Variable var:scope) {
            if(var.isValued() && (var.getValue() == val)) return false;
        }
        return true;
    }
}
