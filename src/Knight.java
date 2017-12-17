

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




	public class Knight extends Piece {


		// Encapsulate state
		public String piece;
		BufferedImage png;
		
		public Knight(int player, int row, int col) {
			super(player, row, col);
			this.piece = "N";

			String initRowStr = Integer.toString(this.initRow);
			String initColStr = String.valueOf(chars[this.initCol]);
			String initPos =  initColStr + initRowStr;
			String p = this.player + "";
			String pn = this.piece;
			this.pieceID = p + "-" + pn + initPos; // ie 0-Bc0

			String imageURL = "files/" + player + "-" + pn + ".png";

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


			Set<Piece> moveSet = new HashSet<Piece>();

			// Get pawn moves for each player
			if (p > 0) {

				// Add Right side
				Piece moveRNN = chessboard[pr + 1][pc + 2]; 
				Piece moveRSS = chessboard[pr + 1][pc - 2]; 
				Piece moveRNE = chessboard[pr + 2][pc + 1]; 
				Piece moveRSE = chessboard[pr + 2][pc - 1];

				if (moveRNN != null && moveRNN.player != p) {
					pieceMoves.add(moveRNN);
				}
				if (moveRSS != null && moveRSS.player != p) {
					pieceMoves.add(moveRSS);
				}
				if (moveRNE != null && moveRNE.player != p) {
					pieceMoves.add(moveRNE);
				}
				if (moveRSE != null && moveRSE.player != p) {
					pieceMoves.add(moveRSE);
				}

				// Add Left side
				Piece moveLNN = chessboard[pr - 1][pc + 2]; 
				Piece moveLSS = chessboard[pr - 1][pc - 2]; 
				Piece moveLNE = chessboard[pr - 2][pc + 1]; 
				Piece moveLSE = chessboard[pr - 2][pc - 1];

				if (moveLNN != null && moveLNN.player != p) {
					pieceMoves.add(moveLNN);
				}
				if (moveLSS != null && moveLSS.player != p) {
					pieceMoves.add(moveLSS);
				}
				if (moveLNE != null && moveLNE.player != p) {
					pieceMoves.add(moveLNE);
				}
				if (moveLSE != null && moveLSE.player != p) {
					pieceMoves.add(moveLSE);
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
