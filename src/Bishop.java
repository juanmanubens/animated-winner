

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




	public class Bishop extends Piece {

		// Encapsulate state
		public String piece;
		BufferedImage png;

		public Bishop(int player, int row, int col) {
			super(player, row, col);
			this.piece = "B";

			String initRowStr = Integer.toString(this.initRow);
			String initColStr = String.valueOf(chars[this.initCol]);
			String initPos =  initColStr + initRowStr;
			String p = Integer.toString(player);
			String pn = this.piece;
			this.pieceID = p + "-" + pn + initPos; // ie 0-Bc0

			String imageURL = "files/" + p + "-" + pn + ".png";
            
			// Load chess piece icon, if applicable
			if (player > 0) {
                /**/
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
			int i = 0;

			Set<Piece> moveSet = new HashSet<Piece>();

			// Get Bishop moves 
			if (p > 0) {

				boolean invalid = false;

				// ************************************************
				// Add Moves - Upper Right Diagonal
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveNE = chessboard[pr + i][pc + i]; 

					if (moveNE == null) {
						invalid = true;
					}

					int piecePlayer = moveNE.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveNE);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveNE);
						i++;
					}
				}
				// Reinitialize
				invalid = false;
				i = 1;

				// ************************************************
				// Add Moves - Lower Right Diagonal
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveSE = chessboard[pr + i][pc - i]; 
					if (moveSE == null) {
						invalid = true;
					}

					int piecePlayer = moveSE.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveSE);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveSE);
						i++;
					}
				}
				// Reinitialize
				invalid = false;
				i = 1;

				// ************************************************
				// Add Moves - Upper Left Diagonal
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveNW = chessboard[pr - i][pc + i]; 
					if (moveNW == null) {
						invalid = true;
					}

					int piecePlayer = moveNW.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveNW);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveNW);
						i++;
					}
				}
				// Reinitialize
				invalid = false;
				i = 1;

				// ************************************************
				// Add Moves - Lower Left Diagonal
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveSW = chessboard[pr - i][pc - i]; 
					if (moveSW == null) {
						invalid = true;
					}

					int piecePlayer = moveSW.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveSW);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveSW);
						i++;
					}
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
