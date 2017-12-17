

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;



	public class King extends Piece {

		// Encapsulate state
		public String piece;
		BufferedImage png;
		
		public King(int player, int row, int col) {
			super(player, row, col);
			this.piece = "K";
			
			String initRowStr = Integer.toString(this.initRow);
			String initColStr = String.valueOf(chars[this.initCol]);
			String initPos =  initColStr + initRowStr;
			String p = Integer.toString(player);
			String pn = this.piece;
			this.pieceID = p + "-" + pn + initPos; // ie 0-Bc0

			String imageURL = "files/" + p + "-" + pn + ".png";

			// Load chess piece icon, if applicable
			if (player > 0) {
				try {
		             if (png == null) {
		             	png = ImageIO.read(new File(imageURL));
		             }
		             
		        } catch (IOException e) {
		            System.out.println("Internal Error:" + e.getMessage());
		        }
			} 
		}

		public String getPiece() {
			String p = this.piece;
			return p;
		}

		// Return Algebraic chess notation
		// i.e. 0-Bc0-c_0
		public String getACN() {
			char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'}; 

			String rowStr = Integer.toString(this.r);
			String colStr = String.valueOf(chars[this.c]);

			String coords = this.pieceID + "-" + colStr + "_" + rowStr; 
			return coords; // ie a pawn on tile (0,1) -> a2 
		}





		// This method gets possible moves within the bounds of 
		// the board, checking whether this is legal in terms of
		// other pieces will be checked later

		public Set<Piece> getMoves(Piece[][] chessboard)  {
			pieceMoves.clear();

			int pr = this.r;
			int pc = this.c;
			int p  = this.player;
			int i = 1;

			Set<Piece> moveSet = new HashSet<Piece>();

			// Get pawn moves for each player
			if (p > 0) {

				// Corners
				Piece moveNE = chessboard[pr + i][pc + i]; 
				Piece moveSE = chessboard[pr + i][pc - i]; 
				Piece moveNW = chessboard[pr - i][pc + i]; 
				Piece moveSW = chessboard[pr - i][pc - i];

				if (moveNE != null && moveNE.player != p) {
					pieceMoves.add(moveNE);
				}
				if (moveSE != null && moveSE.player != p) {
					pieceMoves.add(moveSE);
				}
				if (moveNW != null && moveNW.player != p) {
					pieceMoves.add(moveNW);
				}
				if (moveSW != null && moveSW.player != p) {
					pieceMoves.add(moveSW);
				}

				// Sides
				Piece moveNN = chessboard[pr][pc + i]; 
				Piece moveSS = chessboard[pr][pc - i]; 
				Piece moveEE = chessboard[pr + i][pc]; 
				Piece moveWW = chessboard[pr - i][pc]; 

				if (moveNN != null && moveNN.player != p) {
					pieceMoves.add(moveNN);
				}
				if (moveSS != null && moveSS.player != p) {
					pieceMoves.add(moveSS);
				}
				if (moveEE != null && moveEE.player != p) {
					pieceMoves.add(moveEE);
				}
				if (moveWW != null && moveWW.player != p) {
					pieceMoves.add(moveWW);
				}
			} else {
				// add no moves (empty tile)
				return moveSet;
			}

			// Check bounds
			for (Piece move : pieceMoves) {
				int mr = move.r;
				int mc = move.c;
			    boolean xBounds = mr >= 0 && mr <= 7 && mr != pr; 
			    boolean yBounds = mc >= 0 && mc <= 7 && mc != pc; 

			    if (xBounds && yBounds) {
			    	moveSet.add(move);
			    } 
			}
			return moveSet;
		}

			
		


	}
