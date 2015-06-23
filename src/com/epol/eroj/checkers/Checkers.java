package com.epol.eroj.checkers;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class Checkers extends JFrame implements ActionListener		
{

	private static final long serialVersionUID = 1L;
	private int turn;																//number of current turn (1 or 2)
	
	private final ImageIcon 
	
	EMPTY_FIELD_IMAGE = new ImageIcon(ImageIO.read(getClass().getResource("resources/empty_field.png"))), 				//paths to images
	DEAD_FIELD_IMAGE = new ImageIcon(ImageIO.read(getClass().getResource("resources/dead_field.png"))),
	P1_PAWN_IMAGE = new ImageIcon(ImageIO.read(getClass().getResource("resources/p1_pawn.png"))), 
    P2_PAWN_IMAGE = new ImageIcon(ImageIO.read(getClass().getResource("resources/p2_pawn.png"))),		
    P1_SELECTED_IMAGE = new ImageIcon(ImageIO.read(getClass().getResource("resources/p1_pawn_selected.png"))), 
    P2_SELECTED_IMAGE = new ImageIcon(ImageIO.read(getClass().getResource("resources/p2_pawn_selected.png")));
	
	JButton[][] pawns;																//buttons tab
	private int[][] gameTab;														//game tab to check positions
	Move move;
	Strike strike;
	boolean striked;
	
	private final int DEAD_FIELD = 0,								//final values for gameTab fields
					  EMPTY_FIELD = 1, 
					  P1_PAWN = 2, 
					  P2_PAWN = 3, 
					  P1_PAWN_SELECTED = 4, 
					  P2_PAWN_SELECTED = 5; 
	
	Coordinates lastStrikeCoordinates;
	
	
	public Checkers() throws IOException									//Checkers class constructor
	{
		turn = 1;															//INITIALIZATIONS AND WINDOW/PANEL PROPERTIES
		setTitle("Checkers PLAYER "+turn);
		setResizable(false);
		
		pawns = new JButton[8][8];
		
		setSize(600, 600);
		
		JPanel panel = new JPanel();
		panel = initPanel();
		
		add(panel);
		setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		move = new Move();
		strike = new Strike();
		lastStrikeCoordinates = new Coordinates(-1,-1);
		striked = false;
	}
	
	public static void main(String[] args) throws IOException
	{
		new Checkers();
	}
	
	// GameWindow methods
	
	private JPanel initPanel() throws IOException		//game panel initialization
	{
		JPanel panel = new JPanel();
		
		gameTab = new int[][]{
				{0,2,0,2,0,2,0,2},
				{2,0,2,0,2,0,2,0},
				{0,2,0,2,0,2,0,2},
				{1,0,1,0,1,0,1,0},
				{0,1,0,1,0,1,0,1},
				{3,0,3,0,3,0,3,0},
				{0,3,0,3,0,3,0,3},
				{3,0,3,0,3,0,3,0}
		};
		
//		gameTab = new int[][]{
//		{0,2,0,2,0,2,0,2},
//		{2,0,2,0,2,0,2,0},
//		{0,2,0,2,0,2,0,2},
//		{1,0,1,0,1,0,1,0},
//		{0,3,0,1,0,1,0,1},
//		{3,0,3,0,3,0,3,0},
//		{0,3,0,1,0,1,0,3},
//		{3,0,3,0,1,0,3,0}
//};
		
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				switch(gameTab[i][j])
				{
					case DEAD_FIELD:
						pawns[i][j] = addButton(DEAD_FIELD_IMAGE, true);
						break;
					
					case EMPTY_FIELD:
						pawns[i][j] = addButton(EMPTY_FIELD_IMAGE, false);
						break;
					
					case P1_PAWN:
						pawns[i][j] = addButton(P1_PAWN_IMAGE, false);
						break;
						
					case P2_PAWN:
						pawns[i][j] = addButton(P2_PAWN_IMAGE, false);
					
				}
				
				panel.add(pawns[i][j]);
			}
		}
		
		panel.setLayout(new GridLayout(8,8));
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) 			//action on click
	{
		Object obj = e.getSource();
		int[] xy = findButton(obj);
		int x = xy[0], y = xy[1];
		
		click(x, y);
		
		printField(x,y);
		

	}
	
	private JButton addButton(ImageIcon img, boolean isDead)		//set button properties
	{
		JButton button = new JButton();
		button.setIcon(img);
		
		if(isDead) button.setEnabled(false);
		else button.addActionListener(this);
		
		return button;
	}
	
	private void printField(int i, int j)		//test function to print some fields descritions
	{
		System.out.println(gameTab[i][j]+" x: "+i+" y: "+j);
	}
	
	private int[] findButton(Object obj)	//get Button coordinates (x,y)
	{
		int[] xy = new int[2];
		boolean found = false;
		
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(obj == pawns[i][j])
				{
					xy[0] = i;
					xy[1] = j;
					found = true;
					break;
				}
			}
			
			if(found) break;
		}
		
		return xy;
	}
	
	private void click(int i, int j)
	{
		if(turn == 1)
		{
			if(!strike.checkForStrikes(gameTab, turn))
			{
				if(gameTab[i][j] == P1_PAWN)
				{
					if(move.checkIfSelected(gameTab, turn))deselect();
					if(move.select(gameTab, i, j, turn))pawns[i][j].setIcon(P1_SELECTED_IMAGE);
					gameTab[i][j] = P1_PAWN_SELECTED;
				}
				else if(gameTab[i][j] == EMPTY_FIELD)
				{
					if(move.validMove(gameTab, turn, i, j))
					{
						makeMove(i, j, P1_PAWN);
						setTitle("Checkers PLAYER "+turn);
					}
				}
			}
			else
			{
				List<Coordinates> coords = strike.getStrikePawnsCoordinates(gameTab, turn);
				
				if(gameTab[i][j] == P1_PAWN && !striked)
				{
					for(Coordinates coord : coords)
					{
						if(i == coord.x && j == coord.y)
						{
							deselect();
							gameTab[i][j] = P1_PAWN_SELECTED;
							pawns[i][j].setIcon(P1_SELECTED_IMAGE);
						}
							
					}
				}
				else if(gameTab[i][j] == EMPTY_FIELD && !striked)
				{
					if(strike.checkStrike(coords, gameTab, i, j, turn))
					{
						for(Coordinates coord : coords)
						{
							if(gameTab[coord.x][coord.y] == P1_PAWN_SELECTED)
							{
								strike(coord, i, j);
							}
						}
					}
				}
				else if(gameTab[i][j] == EMPTY_FIELD && striked)
				{
					strikeXY(lastStrikeCoordinates, i, j);
				}
			}
		}
		else if(turn == 2)
		{
			if(!strike.checkForStrikes(gameTab, turn))
			{
				if(gameTab[i][j] == P2_PAWN)
				{
					if(move.checkIfSelected(gameTab, turn))deselect();
					if(move.select(gameTab, i, j, turn))pawns[i][j].setIcon(P2_SELECTED_IMAGE);
					gameTab[i][j] = P2_PAWN_SELECTED;
				}
				else if(gameTab[i][j] == EMPTY_FIELD)
				{
					if(move.validMove(gameTab, turn, i, j))
					{
						makeMove(i, j, P2_PAWN);
					}
				}
			}
			else
			{
				List<Coordinates> coords = strike.getStrikePawnsCoordinates(gameTab, turn);
				
				if(gameTab[i][j] == P2_PAWN && !striked)
				{
					for(Coordinates coord : coords)
					{
						if(i == coord.x && j == coord.y)
						{
							deselect();
							gameTab[i][j] = P2_PAWN_SELECTED;
							pawns[i][j].setIcon(P2_SELECTED_IMAGE);
						}
							
					}
				}
				else if(gameTab[i][j] == EMPTY_FIELD && !striked)
				{
					if(strike.checkStrike(coords, gameTab, i, j, turn))
					{
						for(Coordinates coord : coords)
						{
							if(gameTab[coord.x][coord.y] == P2_PAWN_SELECTED)
							{
								strike(coord, i, j);
							}
						}
					}
				}
				else if(gameTab[i][j] == EMPTY_FIELD && striked)
				{
					strikeXY(lastStrikeCoordinates, i, j);
				}
			}
		}
	}
	
	private void deselect()
	{
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(gameTab[i][j] == P1_PAWN_SELECTED)
				{
					pawns[i][j].setIcon(P1_PAWN_IMAGE);
					gameTab[i][j] = P1_PAWN;
				}
				else if(gameTab[i][j] == P2_PAWN_SELECTED)
				{
					pawns[i][j].setIcon(P2_PAWN_IMAGE);
					gameTab[i][j] = P2_PAWN;
				}
			}
		}
	}
	
	private void makeMove(int i, int j, int new_value)
	{
		if(new_value == P1_PAWN)pawns[i][j].setIcon(P1_PAWN_IMAGE);
		else if(new_value == P2_PAWN)pawns[i][j].setIcon(P2_PAWN_IMAGE);
		int x = move.coords.x, 
		    y = move.coords.y;
		
		pawns[x][y].setIcon(EMPTY_FIELD_IMAGE);
	
		gameTab[i][j] = new_value;
		gameTab[x][y] = EMPTY_FIELD;
		changeTurn();
		
		move.coords.setCoordinates(-1, -1);
		
		colorBorders();
	}
	
	private void clearBorders()
	{
		Border border = UIManager.getBorder("Button.border");
		
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				pawns[i][j].setBorder(border);
			}
		}
	}
	
	private void strike(Coordinates coord, int i, int j)
	{
		gameTab[coord.x][coord.y] = EMPTY_FIELD;
		pawns[coord.x][coord.y].setIcon(EMPTY_FIELD_IMAGE);
		gameTab[strike.pawnToRemove.x][strike.pawnToRemove.y] = EMPTY_FIELD;
		pawns[strike.pawnToRemove.x][strike.pawnToRemove.y].setIcon(EMPTY_FIELD_IMAGE);
		if(turn == 1)
		{
			pawns[i][j].setIcon(P1_SELECTED_IMAGE);
			gameTab[i][j] = P1_PAWN_SELECTED;
		}
		else if(turn == 2)
		{
			pawns[i][j].setIcon(P2_SELECTED_IMAGE);
			gameTab[i][j] = P2_PAWN_SELECTED;
		}
		
		clearBorders();
		
		lastStrikeCoordinates.setCoordinates(i,j);
		
		if(strike.lookForStrikesXY(lastStrikeCoordinates, gameTab, turn))
		{
			striked = true;
			System.out.println("STRIKED");
			pawns[i][j].setBorder(BorderFactory.createLineBorder(Color.red));
		}
		else
		{
			System.out.println("NOT STRIKED");
			clearBorders();
			striked = false;
			if(turn ==1)
			{
				pawns[i][j].setIcon(P1_PAWN_IMAGE);
				gameTab[i][j] = P1_PAWN;
			}
			else if(turn == 2)
			{
				pawns[i][j].setIcon(P2_PAWN_IMAGE);
				gameTab[i][j] = P2_PAWN;
			}
			
			changeTurn();
			colorBorders();
		}
	}
	
	private void strikeXY(Coordinates coord, int i, int j)
	{
		int x = coord.x, y = coord.y;
		
		if(strike.makeStrikeXY(coord, gameTab, i, j, turn))
		{
			gameTab[x][y] = EMPTY_FIELD;
			pawns[x][y].setIcon(EMPTY_FIELD_IMAGE);
			gameTab[strike.pawnToRemove.x][strike.pawnToRemove.y] = EMPTY_FIELD;
			pawns[strike.pawnToRemove.x][strike.pawnToRemove.y].setIcon(EMPTY_FIELD_IMAGE);
			
			if(turn == 1)
			{
				pawns[i][j].setIcon(P1_SELECTED_IMAGE);
				gameTab[i][j] = P1_PAWN_SELECTED;
			}
			else if(turn == 2)
			{
				pawns[i][j].setIcon(P2_SELECTED_IMAGE);
				gameTab[i][j] = P2_PAWN_SELECTED;
			}
			
			lastStrikeCoordinates.setCoordinates(i, j);
			clearBorders();
			
			if(strike.lookForStrikesXY(lastStrikeCoordinates, gameTab, turn))
			{
				System.out.println("GOOD");
				striked = true;
				pawns[i][j].setBorder(BorderFactory.createLineBorder(Color.red));
			}
			else
			{
				System.out.println("NOT GOOD");
				if(turn == 1)
				{
					pawns[i][j].setIcon(P1_PAWN_IMAGE);
					gameTab[i][j] = P1_PAWN;
				}
				else if(turn == 2)
				{
					pawns[i][j].setIcon(P2_PAWN_IMAGE);
					gameTab[i][j] = P2_PAWN;
				}
				
				striked = false;
				clearBorders();
				changeTurn();
				colorBorders();											//sprawdź czy są jakieś bicia, jeśli tak, to pokoloruj
			}
		}
	}
	
	private void changeTurn()
	{
		if(turn == 1)turn = 2;
		else if(turn == 2)turn = 1;
		
		setTitle("Checkers PLAYER "+turn);
	}
	
	private void colorBorders()
	{
		if(strike.checkForStrikes(gameTab, turn))
		{
			if(turn == 1)
			{
				for(Coordinates coords : strike.getStrikePawnsCoordinates(gameTab, turn))
				{
					pawns[coords.x][coords.y].setBorder(BorderFactory.createLineBorder(Color.red));
				}
				System.out.println("Striking obligation for player 1");
			}
			else if(turn == 2)
			{
				for(Coordinates coords : strike.getStrikePawnsCoordinates(gameTab, turn))
				{
					pawns[coords.x][coords.y].setBorder(BorderFactory.createLineBorder(Color.red));
				}
				System.out.println("Striking obligation for player 2");
			}
		}
	}
}
