

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




	public class Rook extends Piece {

		// Encapsulate state
		public String piece;
		BufferedImage png;
		
		public Rook(int player, int row, int col) {
			super(player, row, col);
			this.piece = "R";

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
			int i = 0;

			Set<Piece> moveSet = new HashSet<Piece>();

			// Get Rook moves 
			if (p > 0) {

				boolean invalid = false;

				// ************************************************
				// Add Moves - Up
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveNN = chessboard[pr + 1][pc]; 

					if (moveNN == null) {
						invalid = true;
					}

					int piecePlayer = moveNN.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveNN);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveNN);
						i++;
					}
				}
				// Reinitialize
				invalid = false;
				i = 1;

				// ************************************************
				// Add Moves - Down
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveSS = chessboard[pr - 1][pc];  
					if (moveSS == null) {
						invalid = true;
					}

					int piecePlayer = moveSS.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveSS);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveSS);
						i++;
					}
				}
				// Reinitialize
				invalid = false;
				i = 1;

				// ************************************************
				// Add Moves - East
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveEE = chessboard[pr][pc + 1]; 
					if (moveEE == null) {
						invalid = true;
					}

					int piecePlayer = moveEE.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveEE);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveEE);
						i++;
					}
				}
				// Reinitialize
				invalid = false;
				i = 1;

				// ************************************************
				// Add Moves - West
				i = 1;
				while (!invalid && i < 8) {
					// Check if piece is valid
					Piece moveWW = chessboard[pr][pc - 1]; 
					if (moveWW == null) {
						invalid = true;
					}

					int piecePlayer = moveWW.player;
					if (piecePlayer == p) {
						invalid = true;
					} else if (piecePlayer > 0) {
						// Enemy - valid move, final in sequence
						pieceMoves.add(moveWW);
						invalid = true; // break
					} else if (piecePlayer == 0) {
						// Empty spot - add move, proceed to next
						pieceMoves.add(moveWW);
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
