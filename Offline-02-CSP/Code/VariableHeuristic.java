import java.util.ArrayList;

public class VariableHeuristic {

    private ArrayList<Variable> variables1;
    private static int num, num1;

    VariableHeuristic() { variables1 = new ArrayList<>(); }

    void addVariable(Variable var) { variables1.add(var); num++; }


    private Variable getVAH1() {
        if(variables1.isEmpty()) {
            //System.out.println(num);
            //System.out.println(num1);
            return null;
        }
        int minDom = variables1.get(0).getDomainSize();
        Variable var = variables1.get(0);
        for(int i=1; i<variables1.size(); i++) {
            if(variables1.get(i).getDomainSize() < minDom) {
                var = variables1.get(i);
                minDom = variables1.get(i).getDomainSize();
            }
        }
        variables1.remove(var);
        //num1++;
        return var;
    }

    private int getDegree(Variable var) {
        int row = var.getRow();
        int col = var.getCol();
        int count = 0;
        for(Variable v:variables1) {
            if(!(v.getRow() == row && v.getCol() == col) && (v.getRow() == row || v.getCol() == col)) {
                if(!v.isValued()) count++;
            }
        }
        System.out.println(count);
        return count;
    }


    public Variable getVAH2() {
        if(variables1.isEmpty()) {
            return null;
        }
        int max = getDegree(variables1.get(0));
        Variable var = variables1.get(0);
        for(int i=1; i<variables1.size(); i++) {
            if(getDegree(variables1.get(i)) > max) {
                var = variables1.get(i);
                max = getDegree(variables1.get(i));
            }
        }
        variables1.remove(var);
        return var;
    }

    private Variable getVAH3() {
        if(variables1.isEmpty()) return null;
        int minDom = variables1.get(0).getDomainSize();
        int minDeg = getDegree(variables1.get(0));
        Variable var = variables1.get(0);
        for(int i=1; i<variables1.size(); i++) {
            if(variables1.get(i).getDomainSize() < minDom) {
                var = variables1.get(i);
                minDom = variables1.get(i).getDomainSize();
                minDeg = getDegree(var);
            }
            else if(variables1.get(i).getDomainSize() == minDom) {
                if(getDegree(variables1.get(i))>minDeg) {
                    var = variables1.get(i);
                    minDom = variables1.get(i).getDomainSize();
                    minDeg = getDegree(var);
                }
            }
        }
        variables1.remove(var);
        //num1++;
        return var;
    }


    private Variable getVAH4() {
        if(variables1.isEmpty()) return null;
        int minDom = variables1.get(0).getDomainSize();
        int minDeg = getDegree(variables1.get(0));
        int fraq;
        if(minDeg == 0) fraq = minDom;
        else fraq = minDom/minDeg;
        Variable var = variables1.get(0);
        for(int i=1; i<variables1.size(); i++) {
            int fraq2;
            if(getDegree(variables1.get(i)) == 0) fraq2 = variables1.get(i).getDomainSize();
            else fraq2 = variables1.get(i).getDomainSize() / getDegree(variables1.get(i));
            if(fraq2 < fraq) {
                var = variables1.get(i);
                fraq = fraq2;
            }
        }
        variables1.remove(var);
        //num1++;
        return var;
    }

    private Variable getVAH5() {
        if(variables1.isEmpty()) return null;
        int rand = (int) (Math.random() * variables1.size());
        Variable var = variables1.get(rand);;
        variables1.remove(var);
        return var;
    }


    public Variable getNextVariable(int choice) {
        /*
        if(variables1.isEmpty()) {
            System.out.println(num);
            System.out.println(num1);
            return null;
        }
        int minDom = variables1.get(0).getDomainSize();
        Variable var = variables1.get(0);
        for(int i=1; i<variables1.size(); i++) {
            if(variables1.get(i).getDomainSize() < minDom) {
                var = variables1.get(i);
                minDom = variables1.get(i).getDomainSize();
            }
        }
        variables1.remove(var);
        num1++;
        return var;

         */
        if(choice == 1) return getVAH1();
        else if(choice == 2) return getVAH2();
        else if(choice == 3) return getVAH3();
        else if(choice == 4) return getVAH4();
        else if(choice == 5) return getVAH5();
        else return null;
    }
}
