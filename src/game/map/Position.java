package game.map;
public class Position {
    public Position(int r,int c){
        this.row = r;
        this.col = c;
    }
    public int distanceTo(Position otherPos){
        int otherRow = otherPos.getRow();
        int otherCol = otherPos.getCol();
        return Math.abs(this.row - otherRow) + Math.abs(this.col - otherCol);
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public boolean equals(Position otherPos){
        return otherPos.row == row && otherPos.col == col;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position other) {
            return this.row == other.row && this.col == other.col;
        }
        return false;
    }
    @Override
    public String toString(){
        return "("+row+","+col+")";
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col);
    }

    private int row, col;
}
