import java.util.ArrayList;

/* class Variable denotes every cell of the sudoku board */
public class Variable {
    private ArrayList<Integer> domain;
    private int domainSize;
    private int row;
    private int col;
    private int value;
    private boolean isValueGiven;

    Variable(int row, int col) {
        this.row = row;
        this.col = col;
        domain = new ArrayList<>();
        value = 0;
        isValueGiven = false;
        domainSize = 0;
    }

    void setValue(int value) {
        this.value = value;
        isValueGiven = true;
    }

    int getValue() { return value; }

    void removeValue() {
        this.value = 0;
        isValueGiven = false;
    }

    boolean isValued() { return isValueGiven; }

    int getRow() { return row; }

    int getCol() {return col; }

    void addToDomain(int val) {
        //System.out.println(domain.size());
        domain.add(val);
        //domainSize++;
    }

    void removeFromDomain(int val) {
        domain.remove((Integer) val);
        //domainSize--;
    }

    int getDomainSize() { return domain.size(); }

    ArrayList<Integer> getDomain() { return  domain; }
}
