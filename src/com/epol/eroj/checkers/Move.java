package com.epol.eroj.checkers;

public class Move 
{
	Coordinates coords;
	private int EMPTY_FIELD, P1_PAWN_SELECTED, P2_PAWN_SELECTED;
	
	public Move()
	{
		coords = new Coordinates();
		EMPTY_FIELD = 1;
		P1_PAWN_SELECTED = 4;
		P2_PAWN_SELECTED = 5;
	}
	
	public boolean select(int[][] gameTab, int x, int y, int turn)
	{
		
		if(turn == 1)
		{
			try
			{
				if(gameTab[x+1][y+1] == EMPTY_FIELD)return true;
				
			}catch(ArrayIndexOutOfBoundsException e){}
			
			try{ 
				if(gameTab[x+1][y-1] == EMPTY_FIELD)return true; 	
				
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		else if(turn == 2)
		{
			try
			{
				if(gameTab[x-1][y+1] == EMPTY_FIELD)return true;
				
			}catch(ArrayIndexOutOfBoundsException e){}
			
			try{ 
				if(gameTab[x-1][y-1] == EMPTY_FIELD)return true; 	
				
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		
		return false;
	}
	

	
	public boolean checkIfSelected(int[][] gameTab, int turn)
	{
		int search_value = 0;
		
		if(turn == 1)
		{
			search_value = P1_PAWN_SELECTED;
		}
		else if(turn == 2)
		{
			search_value = P2_PAWN_SELECTED;
		}
		
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(gameTab[i][j] == search_value)return true;
			}
		}
		return false;
	}
	
	public boolean validMove(int[][] gameTab, int turn, int i, int j)
	{
		if(turn == 1)
		{
			try{
				if(gameTab[i-1][j-1] == P1_PAWN_SELECTED)
				{
					coords.setCoordinates(i-1, j-1);
					return true;
				}
			}catch(ArrayIndexOutOfBoundsException e){}
			
			try{
				if(gameTab[i-1][j+1] == P1_PAWN_SELECTED)
				{
					coords.setCoordinates(i-1, j+1);
					return true;
				}
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		else if(turn == 2)
		{
			try{
				if(gameTab[i+1][j-1] == P2_PAWN_SELECTED)
				{
					coords.setCoordinates(i+1, j-1);
					return true;
				}
			}catch(ArrayIndexOutOfBoundsException e){}
			
			try{
				if(gameTab[i+1][j+1] == P2_PAWN_SELECTED)
				{
					coords.setCoordinates(i+1, j+1);
					return true;
				}
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		return false;
	}

}
