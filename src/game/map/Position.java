package game.map;

public class Position {
    private int row, col;
    public Position(int r,int c){
        this.row = r;
        this.col = c;
    }

    public int distanceTo(Position otherPos){
        // TODO calculate manheten distance to other point
        return 1;
    }

    public boolean equals(Position otherPos){
        return otherPos.row == row && otherPos.col == col;
    }
}
