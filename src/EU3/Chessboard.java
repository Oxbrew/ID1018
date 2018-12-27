package EU3;

public class Chessboard {
    public static class Field {
        private char row;
        private byte column;
        private Chesspiece piece = null;
        private boolean marked = false;

        public Field (char row, byte column){
            this.row = row;
            this.column = column;
        }

        public  void put (Chesspiece piece){
            this.piece = piece;
        }

        public Chesspiece take () {
            Chesspiece temp = this.piece;
            this.piece = null;
            return temp;
        }

        public void mark (){
            this.marked = true;
        }

        public void unmark (){
            this.marked = false;
        }

        public String toString () {
            String s = (marked)? "xx" : "--";
            return (piece == null)? s: piece.toString();
        }

    }
    public static final int NUMBER_OF_ROWS = 8;
    public static final int NUMBER_OF_COLUMNS = 8;
    public static final int FIRST_ROW = 'a';
    public static final int FIRST_COLUMN = 1;
    private Field [][] fields;

    public Chessboard() {
        fields = new Field[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        char row = 0;
        byte column = 0;
        for (int r = 0; r < NUMBER_OF_ROWS; r++){
            row = (char) (FIRST_ROW + r);
            column = FIRST_COLUMN;
            for (int c = 0; c < NUMBER_OF_COLUMNS; c++){
                fields[r][c] = new Field(row, column);
                column++;
            }
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NUMBER_OF_COLUMNS; i++){
            sb.append(" ");
            sb.append((i + 1));
        }
        for (int i = 0; i < NUMBER_OF_ROWS; i++){
            char rows = (char)(FIRST_ROW + i);
            sb.append(rows + " ");
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++){
                sb.append(fields[i][j].toString() + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public boolean isValidField (char row, byte column){
        return row >= 'a' && row <= 'h' && column > 0 && column <= NUMBER_OF_COLUMNS;
    }

    public abstract class Chesspiece {
        private char color; //w - white, b - black
        private char name; //K, Q, R, B, N, P: King, Queen, Rook, Bishop, Knight, Pawn
        protected char row = 0;
        protected byte column = -1;
        protected Chesspiece (char color, char name){}
        public String toString(){
            return "" + color + name;
        }
        public boolean isOnBoard () {
            return Chessboard.this.isValidField(row,column);
        }
        public void moveTo (char row, byte column)throws NotValidFieldException { //Exception found in separate file
            if (!Chessboard.this.isValidField(row,column)) throw new NotValidFieldException ("bad field: " + row + column);
            this.row = row;
            this.column = column;
            int r = row - FIRST_ROW;
            int c = column - FIRST_COLUMN;
            Chessboard.this.fields[r][c].put(this);
        }

        public void  moveOut(){
            fields[this.row-FIRST_ROW][this.column].take();
        }
        public abstract void markReachableFields();
        public abstract void unmarkReachableFields();
    }
    public class Pawn extends Chesspiece {
        public Pawn (char color, char name) {
            super (color, name);
        }
        public void markReachableFields () {
            byte col = (byte) (column + 1);
            if (Chessboard.this.isValidField(row,col)) {
                int r = row - FIRST_ROW;
                int c = col - FIRST_COLUMN;
                Chessboard.this.fields[r][c].mark();
            }
        }
        public void unmarkReachableFields () {
            byte col = (byte) (column + 1);
            if (Chessboard.this.isValidField(row,col)) {
                int r = row - FIRST_ROW;
                int c = col - FIRST_COLUMN;
                Chessboard.this.fields[r][c].unmark();
            }
        }
    }
    public class Rook extends Chesspiece {
        public Rook (char color, char name){
            super(color, name);
        }
        public void markReachableFields() {
            int r = row - FIRST_ROW;
            int c = column - FIRST_COLUMN;
            for (int i = 0; i < r; i++){
                Chessboard.this.fields[i][c].mark();
            }
            for (int i = 0; i < c; i++){
                Chessboard.this.fields[r][i].mark();
            }
            for (int i = r + 1; i < NUMBER_OF_ROWS; i++){
                Chessboard.this.fields[i][c].mark();
            }
            for (int i = c + 1; i < NUMBER_OF_COLUMNS; i++){
                Chessboard.this.fields[r][i].mark();
            }
        }
        public void unmarkReachableFields() {
            int r = row - FIRST_ROW;
            int c = column - FIRST_COLUMN;
            for (int i = 0; i < r; i++){
                Chessboard.this.fields[i][c].unmark();
            }
            for (int i = 0; i < c; i++){
                Chessboard.this.fields[r][i].unmark();
            }
            for (int i = r + 1; i < NUMBER_OF_ROWS; i++){
                Chessboard.this.fields[i][c].unmark();
            }
            for (int i = c + 1; i < NUMBER_OF_COLUMNS; i++){
                Chessboard.this.fields[r][i].unmark();
            }
        }
    }
    public class Knight extends Chesspiece {}
    public class Bishop extends Chesspiece {}
    public class Queen extends Chesspiece {}
    public class King extends Chesspiece {}
}
