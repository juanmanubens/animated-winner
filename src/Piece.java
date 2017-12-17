

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;



	public class Piece {

		/*
		Piece Class

		<Color>-<Piece>

		<Color>:
		None: empty tile -> int 0
		W: Player 1 -> int 1
		B: Player 2 -> int 2
		<Piece>: 
		Pawn:   P
		Rook:   R (Tower)
		Knight: N (per algebraic chess notation)
		Bishop: B
		Queen:  Q
		King:   K

		Example: White Pawn -> 1-P (.png)
		TODO: print io
		Y: 1-8
		X: a,b,c,d,e,f,g
-		// Icons from: http://www.clker.com/clipart-7190.html
		*/

		// Encapsulate state
		//private String piece;
		protected Set<Piece> pieceMoves = new HashSet<Piece>();
		protected int r;
		protected int c;
        protected String piece;
		protected int initRow;
		protected int initCol;
		protected String pieceID;
		protected int player;
		protected char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'}; 
        BufferedImage png;



		public Piece(int player, int row, int col) {

			this.player = player;
			this.r = row;
			this.c = col;
			this.initRow = row;
			this.initCol = col;
		}

		public int getPlayer() {
			int p = this.player;
			return p;
		}
        
        public String getPiece() {
			String p = "";
			return p;
		}

		public int[] getCoords() {
			int pr = this.r;
			int pc = this.c;
			int[] coords = new int[]{pc, pr}; 
			return coords;
		}

		public int charTranslate(String newCol) {
			int newInt = -1;
			char toInt = newCol.charAt(0);
			for (int i = 0; i < chars.length; i ++) {
				// Compare primitive types
				if (toInt == chars[i]) {
					newInt = i;
				}
			} 
			return newInt;

		}

		public void moveTo(String newCoordsPID) {
			String toPiece = newCoordsPID.split("-")[2];
			String[] newPos =  toPiece.split("_");
			this.r = Integer.parseInt(newPos[1]);
			this.c = charTranslate(newPos[0]);
		}

		public void moveTo(int newRow, int newCol) {
			this.r = newRow;
			this.c = newCol;
		}

        // Return Algebraic chess notation
		// i.e. 0-Bc0-c_0
		public String getACN() {
			

			String rowStr = Integer.toString(this.r);
			String colStr = String.valueOf(chars[this.c]);

			String coords = this.pieceID + "-" + colStr + "_" + rowStr; 
			return coords; // ie a pawn on tile (0,1) -> a2 
		}
        
		public int getRow() {
			int pr = this.r;
			return pr;
		}

		public int getCol() {
			int pc = this.c;
			return pc;
		}

		public boolean isEmpty() {
			return this.player == 0;

		}

		public boolean isEven() {
			return ((r % 2) == 0) && ((c % 2) == 0);

		}

		// Overridden by subclasses
		public Set<Piece> getMoves(Piece[][] chessboard)  {
            pieceMoves.clear();
            Set<Piece> moveSet = new HashSet<Piece>();
            return moveSet;
    	}

		// Filter out moves that put a player's King at risk
		public Set<Piece> getLegalMoves(Set<Piece> allMoves, Piece[][] board) {
			Piece[][] temp = GameCourt.cloneFilterOne(this);
			Set<Piece> legalMoves = new HashSet<Piece>();
			int p = this.player;

			for(Piece move : allMoves) {
				int newRow = move.getRow();
				int newCol = move.getCol();
				Piece prev = temp[newRow][newCol];
				temp[newRow][newCol] = this;
				boolean isValid = !GameCourt.inCheck(p, temp);

				// Only add move if King is not at risk
				if(isValid) {
					legalMoves.add(move);
				}
				// Reset temp board and proceed to next move
				temp[newRow][newCol] = prev;
			}
			
			return legalMoves;
		}
			

	}
