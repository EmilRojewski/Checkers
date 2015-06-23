package com.epol.eroj.checkers;

import java.util.ArrayList;
import java.util.List;



public class Strike
{
	
	private int EMPTY_FIELD, P1_PAWN, P2_PAWN, P1_PAWN_SELECTED, P2_PAWN_SELECTED;
	public Coordinates pawnToRemove;
	
	public Strike()
	{
		EMPTY_FIELD = 1;
		P1_PAWN = 2;
		P2_PAWN = 3;
		P1_PAWN_SELECTED = 4;
		P2_PAWN_SELECTED = 5;
		pawnToRemove = new Coordinates(-1, -1);
	}
	
	public boolean checkForStrikes(int[][] gameTab, int turn)
	{
		int pawn = 0, enemy_pawn = 0, selected_pawn = 0;
		
		if(turn == 1)
		{
			pawn = P1_PAWN;
			selected_pawn = P1_PAWN_SELECTED;
			enemy_pawn = P2_PAWN;
		}
		else if(turn == 2)
		{
			pawn = P2_PAWN;
			selected_pawn = P2_PAWN_SELECTED;
			enemy_pawn = P1_PAWN;
		}
		
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(gameTab[i][j] == pawn || gameTab[i][j] == selected_pawn)
				{
					try{
						if(gameTab[i+1][j+1] == enemy_pawn 	&& gameTab[i+2][j+2] == EMPTY_FIELD) return true;
					}catch(ArrayIndexOutOfBoundsException e){}
					
					try{
						if(gameTab[i+1][j-1] == enemy_pawn 	&& gameTab[i+2][j-2] == EMPTY_FIELD) return true;
					}catch(ArrayIndexOutOfBoundsException e){}
					
					try{
						if(gameTab[i-1][j+1] == enemy_pawn && gameTab[i-2][j+2] == EMPTY_FIELD) return true;
					}catch(ArrayIndexOutOfBoundsException e){}
					
					try{
						if(gameTab[i-1][j-1] == enemy_pawn && gameTab[i-2][j-2] == EMPTY_FIELD) return true;
					}catch(ArrayIndexOutOfBoundsException e){}
				}
			}
		}
		
		return false;
	}
	
	public List<Coordinates> getStrikePawnsCoordinates(int[][] gameTab, int turn)
	{
		List<Coordinates> coordinates = new ArrayList<Coordinates>();
		int pawn = 0, enemy_pawn = 0, selected_pawn = 0;
		
		if(turn == 1)
		{
			pawn = P1_PAWN;
			selected_pawn = P1_PAWN_SELECTED;
			enemy_pawn = P2_PAWN;
		}
		else if(turn == 2)
		{
			pawn = P2_PAWN;
			selected_pawn = P2_PAWN_SELECTED;
			enemy_pawn = P1_PAWN;
		}
		
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(gameTab[i][j] == pawn || gameTab[i][j] == selected_pawn)
				{
					try{
						if(gameTab[i+1][j+1] == enemy_pawn && gameTab[i+2][j+2] == EMPTY_FIELD)
							coordinates.add(new Coordinates(i,j));
						
					}catch(ArrayIndexOutOfBoundsException e){}
					
					try{
						if(gameTab[i+1][j-1] == enemy_pawn && gameTab[i+2][j-2] == EMPTY_FIELD)
							coordinates.add(new Coordinates(i,j));
					}catch(ArrayIndexOutOfBoundsException e){}
					
					try{
						if(gameTab[i-1][j+1] == enemy_pawn && gameTab[i-2][j+2] == EMPTY_FIELD)
							coordinates.add(new Coordinates(i,j));
					}catch(ArrayIndexOutOfBoundsException e){}
					
					try{
						if(gameTab[i-1][j-1] == enemy_pawn && gameTab[i-2][j-2] == EMPTY_FIELD)
							coordinates.add(new Coordinates(i,j));
					}catch(ArrayIndexOutOfBoundsException e){}
				}
			}
		}
		
		return coordinates;
	}
	
	public boolean checkStrike(List<Coordinates> coords, int[][] gameTab, int x, int y, int turn)
	{
		clear();
		int i = 0, j = 0, selected_pawn = 0, pawn_kill = 0;
		
		if(turn == 1)
		{
			selected_pawn = P1_PAWN_SELECTED;
			pawn_kill = P2_PAWN;
			
		}
		else if(turn == 2)
		{
			selected_pawn = P2_PAWN_SELECTED;
			pawn_kill = P1_PAWN;
		}
		
		for(Coordinates coordinates : coords)
		{
			if(gameTab[coordinates.x][coordinates.y] == selected_pawn)
			{
				i = coordinates.x;
				j = coordinates.y;
				
				break;
			}
		}
		
		double x_subs = i - x,
			   y_subs = j - y;
		
		x_subs = Math.abs(x_subs);
		y_subs = Math.abs(y_subs);
		
		if(x_subs == 2 && y_subs == 2)
		{
			if(i > x)pawnToRemove.x = i-1;
			else if(i < x) pawnToRemove.x = i+1;
			
			if(j > y) pawnToRemove.y = j-1;
			else if(j < y) pawnToRemove.y = j+1;
		}
		
		try{
			if(gameTab[pawnToRemove.x][pawnToRemove.y] == pawn_kill)return true;
		}catch(ArrayIndexOutOfBoundsException e){}
		
		return false;
	}
	
	public boolean lookForStrikesXY(Coordinates coord, int[][] gameTab, int turn)
	{
		int enemy_pawn = 0, pawn = 0;
		
		if(turn == 1)
		{
			pawn = P1_PAWN_SELECTED;
			enemy_pawn = P2_PAWN;
		}
		else if(turn == 2)
		{
			pawn = P2_PAWN_SELECTED;
			enemy_pawn = P1_PAWN;
		}
		
		int i = coord.x, j = coord.y;
		
		if(gameTab[i][j] == pawn)
		{
		
			try{
				if(gameTab[i+1][j+1] == enemy_pawn && gameTab[i+2][j+2] == EMPTY_FIELD)
					return true;
				
			}catch(ArrayIndexOutOfBoundsException e){}
			
			try{
				if(gameTab[i+1][j-1] == enemy_pawn && gameTab[i+2][j-2] == EMPTY_FIELD)
					return true;
				
			}catch(ArrayIndexOutOfBoundsException e){}
			
			try{
				if(gameTab[i-1][j+1] == enemy_pawn && gameTab[i-2][j+2] == EMPTY_FIELD)
					return true;
				
			}catch(ArrayIndexOutOfBoundsException e){}
			
			try{
				if(gameTab[i-1][j-1] == enemy_pawn && gameTab[i-2][j-2] == EMPTY_FIELD)
					return true;
				
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		
		System.out.println("FALSE:(((((");
		
		return false;
	}
	
	public boolean makeStrikeXY(Coordinates coords, int[][] gameTab, int x, int y, int turn)
	{
		int i = coords.x, j = coords.y;
		int pawn_kill = 0;
		
		if(turn == 1)
		{
			pawn_kill = P2_PAWN;
		}
		else if(turn == 2)
		{
			pawn_kill = P1_PAWN;
		}
		
		clear();
		
		double x_subs = Math.abs(x-i),
			   y_subs = Math.abs(y-j);
		
		if(x_subs == 2 && y_subs == 2)
		{
			if(x_subs == 2 && y_subs == 2)
			{
				if(i > x)pawnToRemove.x = i-1;
				else if(i < x) pawnToRemove.x = i+1;
				
				if(j > y) pawnToRemove.y = j-1;
				else if(j < y) pawnToRemove.y = j+1;
			}
		}
		
		try{
			if(gameTab[pawnToRemove.x][pawnToRemove.y] == pawn_kill)return true;
		}catch(ArrayIndexOutOfBoundsException e){}
		
		return false;
	}
	
	public void clear()
	{
		pawnToRemove.setCoordinates(-1, -1);
	}
}
