public class State
{
    public char[] alphabet = { 'a', 'b', 'c', 'd','e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    public String turn;
    public int turns;
    public int game;
    public int connect;
    public char[][] gameBoard;

    public State(int row, int col, int board)
    {
        game = board;
        turns = 0;
        connect = convertGame(game);
        turn = "RED";
        gameBoard = new char[row][col];

        //fills the board with empty spaces
        for(int r = 0; r < gameBoard.length; r++)
        {
            for(int c = 0; c < gameBoard[0].length; c++)
            {
                gameBoard[r][c] = ' ';
            }
        }
    }

    public void switchTurn()
    {
        if(turn.equals("RED"))
        {
            turn = "YELLOW";
        }
        else if(turn.equals("YELLOW"))
        {
            turn = "RED";
        }
    }

    public String getTurn()
    {
        return turn;
    }

    public int getTurns()
    {
        return turns;
    }

    public char[][] getBoard()
    {
        return gameBoard;
    }

    public boolean checkWinner(String player)
    {
        boolean winning = false;
        int counter = 0;
        int count = 0;
        int step = 0;

        //horizontal check
        for(int row = 0; row < gameBoard.length; row++)
        {
            if(step >= 1 && game == 1) { break; }
            else if((step > 2) && (game == 2)) { break; }
            else if((step > 3) && (game == 3)) { break; }
            for(int col = 0 + step; col < connect + step; col++)
            {
                if(gameBoard[row][col] == convertPlayer(player))
                {
                    winning = true;
                }
                else
                {
                    winning = false;
                    break;
                }
            }
            if(winning == true) { return true; }
            counter++;
            if(counter >= gameBoard.length)
            {
                step++;
                counter = 0;
                row = -1;
            }
        }
        step = 0;
        counter = 0;
        count = 0;
        winning = false;
        
        //vertical check   
        for(int col = 0; col < gameBoard[0].length; col++)
        {
            if((step >= 1) && ((game == 1) || (game == 2))) { break; }
            else if((step > 2) && (game == 3)) { break; }
            for(int row = gameBoard.length - 1 - step; row > gameBoard.length - 1 - connect - step; row--)
            {
                if(gameBoard[row][col] == convertPlayer(player))
                {
                    winning = true;
                }
                else
                {
                    winning = false;
                    break;
                }
            }
            if(winning == true) { return true; }
            counter++;
            if(counter >= gameBoard[0].length)
            {
                step++;
                counter = 0;
                col = -1;
            }
        }
        step = 0;
        counter = 0;
        count = 0;
        winning = false;

        //diagonal down check
        for(int col = 0; col < gameBoard[0].length - connect + 1; col++)
        {
            int simpConnect = connect;

            if(game == 1 || game == 2) { simpConnect--; }
            if(step >= 1 && (game == 1 || game == 2)) { break; }
            else if((step > 2) && (game == 3)) { break; }
            for(int row = 0 + step; row <= ((gameBoard.length - simpConnect + 1) + step); row++)
            {
                if(gameBoard[row][col] == convertPlayer(player))
                {
                    winning = true;
                    count++;
                    col++;
                }
                else
                {
                    winning = false;
                    col -= count;
                    count = 0;
                    break;
                }
            }
            if(winning == true) { return true; }
            counter++;
            if(counter >= gameBoard[0].length - connect + 1)
            {
                step++;
                counter = 0;
                col = -1;
            }
        }
        step = 0;
        counter = 0;
        count = 0;
        winning = false;

        //diagonal up check
        for(int col = 0; col < gameBoard[0].length - connect + 1; col++)
        {
            int simpConnect = connect;

            if(game == 1 || game == 2 || game == 3) { simpConnect--; }
            if(step >= 1 && (game == 1 || game == 2)) { break; }
            else if((step > 2) && (game == 3)) { break; }
            for(int row = ((gameBoard.length - 1) - step); row >= ((gameBoard.length - simpConnect - 1) - step); row--)
            {
                if(gameBoard[row][col] == convertPlayer(player))
                {
                    winning = true;
                    count++;
                    col++;
                }
                else
                {
                    winning = false;
                    col -= count;
                    count = 0;
                    break;
                }
            }
            if(winning == true) { return true; }
            counter++;
            if(counter >= gameBoard[0].length - connect + 1)
            {
                step++;
                counter = 0;
                col = -1;
            }
        }

        return false;
    }

    public int convertGame(int game)
    {
        if(game == 1 || game == 2)
        {
            return 3;
        }
        else if(game == 3)
        {
            return 4;
        }
        return -1;
    }

    public int convertMove(char move)
    {
        for(int i = 0; i < alphabet.length; i++)
        {
            if(alphabet[i] == move)
            {
                return i;
            }
        }
        return -1;
    }

    public char convertPlayer(String player)
    {
        switch(player)
        {
            case "RED":
            return 'X';
            case "YELLOW":
            return 'O';
            default:
            return '$';
        }
    }

    public void printBoard()
    {
        System.out.print("  ");
        for(int i = 0; i < gameBoard[0].length; i++)
        {
            System.out.print(alphabet[i] + " ");
        }
        System.out.println();
        for(int row = 0; row < gameBoard.length; row++)
        {
            System.out.print(row + 1 + "|");
            for(int col = 0; col < gameBoard[0].length; col++)
            {
                System.out.print(gameBoard[row][col] + "|");
            }
            System.out.println();
        }
    }    

    public int threeFinder(String player)
    {
        int threeCount = 0;

        //vertical
        for(int row = 0; row < gameBoard.length - 2; row++)
        {
            for(int col = 0; col < gameBoard[0].length; col++)
            {
                int row1 = row + 1;
                int row2 = row + 2;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row1][col] == convertPlayer(player) && gameBoard[row2][col] == convertPlayer(player))
                {
                    threeCount++;
                }
            }
        }

        //horizontal
        for(int row = 0; row < gameBoard.length; row++)
        {
            for(int col = 0; col < gameBoard[0].length - 2; col++)
            {
                int col1 = col + 1;
                int col2 = col + 2;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row][col1] == convertPlayer(player) && gameBoard[row][col2] == convertPlayer(player))
                {
                    threeCount++;
                }
            }
        }

        //diagonal down
        for(int row = 0; row < gameBoard.length - 2; row++)
        {
            for(int col = 0; col < gameBoard[0].length - 2; col++)
            {
                int row1 = row + 1;
                int row2 = row + 2;
                int col1 = col + 1;
                int col2 = col + 2;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row1][col1] == convertPlayer(player) && gameBoard[row2][col2] == convertPlayer(player))
                {
                    threeCount++;
                }
            }
        }

        //diagonal up
        for(int row = gameBoard.length - 1; row > 1; row--)
        {
            for(int col = 0; col < gameBoard[0].length - 2; col++)
            {
                int row1 = row - 1;
                int row2 = row - 2;
                int col1 = col + 1;
                int col2 = col + 2;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row1][col1] == convertPlayer(player) && gameBoard[row2][col2] == convertPlayer(player))
                {
                    threeCount++;
                }
            }
        }

        return threeCount;
    }

    public int twoFinder(String player)
    {
        int twoCount = 0;

        //vertical
        for(int row = 0; row < gameBoard.length - 1; row++)
        {
            for(int col = 0; col < gameBoard[0].length; col++)
            {
                int row1 = row + 1;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row1][col] == convertPlayer(player))
                {
                    twoCount++;
                }
            }
        }

        //horizontal
        for(int row = 0; row < gameBoard.length; row++)
        {
            for(int col = 0; col < gameBoard[0].length - 1; col++)
            {
                int col1 = col + 1;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row][col1] == convertPlayer(player))
                {
                    twoCount++;
                }
            }
        }

        //diagonal down
        for(int row = 0; row < gameBoard.length - 1; row++)
        {
            for(int col = 0; col < gameBoard[0].length - 1; col++)
            {
                int row1 = row + 1;
                int col1 = col + 1;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row1][col1] == convertPlayer(player))
                {
                    twoCount++;
                }
            }
        }

        //diagonal up
        for(int row = gameBoard.length - 1; row > 1; row--)
        {
            for(int col = 0; col < gameBoard[0].length - 1; col++)
            {
                int row1 = row - 1;
                int col1 = col + 1;
                if(gameBoard[row][col] == convertPlayer(player) && gameBoard[row1][col1] == convertPlayer(player))
                {
                    twoCount++;
                }
            }
        }

        return twoCount;
    }
}