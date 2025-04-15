package game.map;
//finished
public class Position {
    private int row, col;
    public Position(int r,int c){
        this.row = r;
        this.col = c;
    }

    public int distanceTo(Position otherPos){
        int otherRow = otherPos.getRow();
        int otherCol = otherPos.getCol();
        int dis = Math.abs(this.row - otherRow) + Math.abs(this.col - otherCol);
        return dis;
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
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position position = (Position) obj;
            return this.row == position.row && this.col == position.col;
        }
        return false;
    }
    public String toString(){
        return "("+row+","+col+")";
    }
}
