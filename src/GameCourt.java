/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // Chess tiles 8x8, Dim 75, 0075, W,H 600
    public static final int TILE = 74;
    public static final int COURT_WIDTH  = TILE * 8;
    public static final int COURT_HEIGHT = TILE * 8;

    public static final String p1 = "Player 1's Move";
    public static final String p2 = "Player 2's Move";
    public static final String c1 = "CHECK - Player 1";
    public static final String c2 = "CHECK - Player 2";
    public static final String w1 = "CHECKMATE - Player 1";
    public static final String w2 = "CHECKMATE - Player 2";
    public static final String promotion = "PAWN PROMOTION - Pick a replacement" + "\n";

    public static final Color legal = new Color(160, 240, 160);
    public static final Color selected = new Color(255, 130, 130);
    
    final JLabel status; // Current status text, i.e. "Running..."

    int gameCounter;
    int player;
    
    public static final String FILE = "out.txt";
    //BufferedWriter bw = null;
    //FileWriter fw = null;

    public static Piece[][] board = new Piece[8][8];
    public Set<Piece> legalMoves = new HashSet<Piece>();
    public Piece toMove;
    public GameCourt(JLabel status) {



        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.status = status;
        this.player = 1;
        this.gameCounter = 1;

        // Set board dimensions
        Dimension boardSize = getPreferredSize();
        this.setPreferredSize(boardSize);
        /*
        try {
			fw = new FileWriter(FILE,true);
			bw = new BufferedWriter(fw);
			bw.write("ChessScriptIO");
		} catch (IOException e) {
			e.printStackTrace();
		}
        */



        // TODO: Pause, timer
        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {

                int p = player;

                int row = Math.min(Math.max(me.getY() / 75, 0), 7);
                int col = Math.min(Math.max(me.getX() / 75, 0), 7);

                Piece cp =  board[row][col];

                if(toMove == null) {
                    // First click - select piece
                    if(sameTeam(cp)) {
                        toMove = cp;
                        legalMoves.addAll(getLegalMoves(toMove, p, board));
                    }
                } else {
                    // Second click - move if valid, else repeat selection
                    if(sameTeam(cp)) {
                        toMove = cp;
                        legalMoves.addAll(getLegalMoves(toMove, p, board));
                    }

                    //move piece
                    boolean validTarget = cp.isEmpty() || !sameTeam(cp);
                    boolean validCoords = legalMoves.contains(cp);
                    if(validTarget && validCoords) {
                        
                        cp = toMove;
                        int r = toMove.getRow();
                        int c = toMove.getCol();
                        // Register move with ACN
                        /*
                        try {
                            char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'}; 
                            String rowStr = Integer.toString(r);
                            String colStr = String.valueOf(chars[c]);
                            String moveCoords = cp.getACN() + "\t" + colStr + "_" + rowStr;
                            bw.write(Integer.toString(gameCounter) + "\t" + moveCoords);
                            bw.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        */
                        boolean isPawn = toMove instanceof Pawn;
                        boolean noMovesLeft = (row == 0) || (row == 7);
                        if (isPawn && noMovesLeft) {
                            toMove = promotePawn(toMove);
                        }
                        
                        board[r][c] = new Piece(0, r, c);
                        toMove.moveTo(row, col);
                        board[row][col] = toMove;
                        
                        
                        toMove = null;
                        legalMoves.clear();
                        
                        nextMove();
                        // TO DO: IO

                    }
                }
                repaint();
            }
        });

        // Create the first initial board
        newBoard();
    }



    // TODO: ADD CHECK AND CHECKMATE
    public void playerStatus() {
        if (this.player == 1) {
            status.setText(p1);
            if (inCheckmate(2, board)) {
                status.setText(w1);
                /*
                try {
                    bw.write(w1);
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
            }
        } else if (this.player == 2) {
            status.setText(p2);
            if (inCheckmate(1, board)) {
                status.setText(w2);
                /*
                try {
                    bw.write(w2);
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
            }
        } else {
            status.setText("ERROR: playerStatus");
        }
        this.gameCounter++;
    }

    public void switchPlayers() {
        int p = this.player;
        if (p == 1) {
            this.player = 2;
        } else if (p == 2) {
            this.player = 1;
        } else {
            status.setText("ERROR: switchPlayers");
        }
    }

    public void resetCounter() {
        this.gameCounter = 1;
        this.player = 1;
        this.toMove = null;
        this.legalMoves = new HashSet<Piece>();
        playerStatus();
    }

    public void nextMove() {
        switchPlayers();
        playerStatus();
    }

    // set the initial board, with all the pieces in their correct spots
    public void newBoard() {

        // Reset counter and linked states 
        resetCounter();

        // Pawns and empty tiles
        for (int row = 0; row < 8; row++) {
            boolean emptyTiles = (row > 1 && row < 6);
            for (int col = 0; col < 8; col++) {
                if (row == 1) {
                    board[row][col] = new Pawn(1, row, col);
                } else if (row == 6) {
                    board[row][col] = new Pawn(2, row, col);
                } else if (emptyTiles) {
                    board[row][col] = new Piece(0, row, col);
                }
            }
        }

        // Rooks
        board[0][0] = new Rook(1,0,0);
        board[0][7] = new Rook(1,0,7);
        board[7][0] = new Rook(2,7,0);
        board[7][7] = new Rook(2,7,0);
        // Knights
        board[0][1] = new Knight(1,0,1);
        board[0][6] = new Knight(1,0,6);
        board[7][1] = new Knight(2,7,1);
        board[7][6] = new Knight(2,7,6);
        // Bishops
        board[0][2] = new Bishop(1,0,2);
        board[0][5] = new Bishop(1,0,5);
        board[7][2] = new Bishop(2,7,2);
        board[7][5] = new Bishop(2,7,5);
        // Kings
        board[0][3] = new King(1,0,0);
        board[7][3] = new King(2,7,0);
        // Queens
        board[0][4] = new Queen(1,0,0);
        board[7][4] = new Queen(2,7,0);

        // Paint the board
        repaint();
    }



    /// PIECE HANDLING FUNCTIONS

    public static boolean isKing(Piece cp) {
        return cp instanceof King;
    }

    public boolean sameTeam(Piece cp) {
        if (toMove != null) {
            return toMove.getPlayer() == cp.getPlayer();
        } else {
            return false;
        }

    }

    public Piece replacePawn(Piece cp, String playerPick) {

        int p = cp.getPlayer();
        int r = cp.getCoords()[1];
        int c = cp.getCoords()[0];

        Piece newQ = new Queen(p, r, c);;
        Piece newN = new Knight(p, r, c);
        Piece newR = new Rook(p, r, c);
        Piece newB = new Bishop(p, r, c);
        
        if (playerPick.equals("Knight")) {
            return newN;
        } else if (playerPick.equals("Rook")) {
            return newR;
        } else if (playerPick.equals("Bishop")) {
            return newB;
        } 
        // Return Queen if no valid pick has been chosen by default
        return newQ;
    }

    public Piece promotePawn(Piece cp) {
        Object[] options = {"Queen","Rook","Knight","Bishop"};
        String playerPick = (String) JOptionPane.showInputDialog(Game.frame, 
            promotion,
            "PAWN PROMOTION",
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            "Queen");
        Piece newPiece = replacePawn(cp, playerPick);
        return newPiece;
    }

    public Piece[][] cloneAll() {
        Piece[][] temp = new Piece[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                temp[i][j] = board[i][j];
            }
        }
        return temp;
    }

    public static Piece[][] cloneFilterOne(Piece cp) {
        int r1 = cp.getRow();
        int c1 = cp.getCol();
        Piece[][] temp = new Piece[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if (i != r1 && j != c1) {
                    temp[i][j] = board[i][j];
                }
            }
        }
        temp[r1][c1] = new Piece(0, r1, c1);
        return temp;
    }



    public static Piece getKing(int choosePlayer, Piece[][] temp) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Piece cp = temp[i][j];
                if (cp.getPlayer() == choosePlayer && isKing(cp)) {
                    Piece newPiece = cp;
                    return newPiece;
                }    
            }
        }
        // Dummy return 
        return new Piece(0,0,0);
    }

    
    public static Set<Piece> getCounterMoves(int choosePlayer, Piece[][] temp) {
        Set<Piece> counterMoves = new HashSet<Piece>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Piece cp = temp[i][j];
                int p = cp.getPlayer();
                if(p != choosePlayer && p > 0) {
                    counterMoves.addAll(cp.getMoves(temp)); 
                } 
            }
        }
        return counterMoves;
    }

    public Set<Piece> getLegalMoves(Piece cp, int choosePlayer, Piece[][] temp) {
        Set<Piece> unfiltered = new HashSet<Piece>();
        unfiltered.addAll(cp.getMoves(temp));
        Set<Piece> filtered = new HashSet<Piece>();
        filtered.addAll(cp.getLegalMoves(unfiltered, temp));
        return filtered;
    }



    public Set<Piece> getAllPieces(int choosePlayer, Piece[][] temp) {
        Set<Piece> allPieces = new HashSet<Piece>();
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece cp = temp[i][j];
                int p = cp.getPlayer();
                if(p == choosePlayer) {
                    allPieces.add(cp);
                }
            }
        }
        return allPieces;
    }


    //method to check if team is in check on a particular board
    public static boolean inCheck(int choosePlayer , Piece[][] temp) {
        Set<Piece> counterMoves = new HashSet<Piece>();
        counterMoves.addAll(getCounterMoves(choosePlayer, temp));
        return counterMoves.contains(getKing(choosePlayer, temp));
    }

    public  boolean inCheckmate(int choosePlayer , Piece[][] temp) {
        Set<Piece> allMoves = new HashSet<Piece>();
        Set<Piece> allPieces = new HashSet<Piece>();
        allPieces.addAll(getAllPieces(choosePlayer, temp));
        for (Piece cp: allPieces) {
            allMoves.addAll(getLegalMoves(cp, choosePlayer, temp));
        }
        return allMoves.size() == 0;
    }





    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int df = 0;
                Piece cp = board[i][j];
                String p = Integer.toString(cp.getPlayer());
                String t = "";
                if (cp.getPlayer() > 0) {
                    t = cp.getPiece();
                }
                boolean selectedPiece = cp == toMove;
                boolean isLegal = legalMoves.contains(cp);
                boolean isEmpty = cp.isEmpty();

                // Mark selected piece if applicable
                if (selectedPiece && !isEmpty) {
                    g.setColor(selected);
                }

                // Set board tile color
                boolean isEven = ((i + j % 2) == 0);
                if (isEven) {
                    df = 100;
                } 
                Color tileColor = new Color(155 + df, 155 + df, 155 + df);
                g.setColor(tileColor); 
                g.fillRect(TILE * j + 32, TILE * i + 32, TILE, TILE);

                // Draw Icon if Applicable
                if (!!isEmpty) {
                    //g.drawImage(cp.png, TILE * j + 32, TILE * i + 32, TILE, TILE, null);
                    // MY IMAGE IO HAS BEEN FAILING SO IM TRYING AN ALTERNATIVE
                    g.drawString(p + t, TILE * j + 32, TILE * i + 32);
                }
                

                // Mark legal moves for player
                if (isLegal) {
                    Graphics2D g2 = (Graphics2D) g;
                    Font h = new Font("Helvetica", Font.PLAIN, 18);
                    g2.setFont(h);
                    g2.setColor(legal);
                    g.fillRect(TILE * j + 32, TILE * i + 32, TILE, TILE);
                    
                }
            }
        }

        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}