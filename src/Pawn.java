

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




	public class Pawn extends Piece {

		// Encapsulate state
		public String piece;
		BufferedImage png;
		
		public Pawn(int player, int row, int col) {
			super(player, row, col);
			this.piece = "P";

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

			int pfm = 1;
			int initY = 1;

			// Get pawn moves for each player
			if (p == 2) {
				pfm = -1;
				initY = 6;
			}

			if (p > 0) {
				// Add front move (TO DO: Pawn Promotion)
				Piece moveFRONT = chessboard[pr][pc + 1 * pfm]; 

				if (moveFRONT != null && moveFRONT.player == 0) {
					pieceMoves.add(moveFRONT);
				}

				// Add possible diagonal attacks
				Piece moveDR = chessboard[pr + 1][pc + 1 * pfm]; 
				Piece moveDL = chessboard[pr - 1][pc + 1 * pfm]; 

				// Can only attack diagonally
				if (moveDR != null && moveDR.player != p && moveDR.player > 0) {
					pieceMoves.add(moveDR);
				}
				if (moveDL != null && moveDL.player != p && moveDL.player > 0) {
					pieceMoves.add(moveDL);
				}

				// Pawns in their initial square can move 2 squares if empty.
				Piece moveFF = chessboard[pr][pc + 2 * pfm]; 
				boolean initPos = (pr == this.initRow) && (pc == this.initCol);
				boolean emptyTile1 = (moveFRONT != null) && (moveFRONT.player == 0);
				boolean emptyTile2 = (moveFF != null) && (moveFF.player == 0);

				if (initPos && emptyTile1 && emptyTile2 ) {
					pieceMoves.add(moveFF);
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
