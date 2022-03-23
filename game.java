import java.util.*;

public class game
{
    public static int realPlayer = -1;
    public static int depth = 0;
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        State gameBoard = null;
        boolean gameOver = false;
        int move = -1;

        System.out.println("Connect-Four by Varen Maniktala & Varun Kumar");
        System.out.println("Choose your game:");
        System.out.println("1. Tiny 3x3x3 Connect-Three");
        System.out.println("2. Wider 5x3x3 Connect-Three");
        System.out.println("3. Standard 7x6x4 Connect-Four");
        System.out.print("Your choice? ");
        int game = scan.nextInt();
        System.out.println();

        System.out.println("Choose your opponent:");
        System.out.println("1. An agent that plays randomly");
        System.out.println("2. An agent that uses MINIMAX (extremely slow on 6x7 board");
        System.out.println("3. An agent that uses MINIMAX with alpha-beta pruning (extremely slow on 6x7 board");
        System.out.println("4. An agent that uses H-MINIMAX with a fixed depth cutoff");
        System.out.println("5. An agent that uses H-MINIMAX with a fixed depth cutoff and alpha-beta pruning");
        System.out.print("Your choice? ");
        int AI = scan.nextInt();
        System.out.println();

        int depth = 0;
        if(AI == 4 || AI == 5)
        {  
            System.out.println("NOTE: with depths past 6, the AI's first move gets progessively slower...");
            System.out.print("Depth limit? ");
            depth = scan.nextInt();
            System.out.println();
        }

        System.out.print("Do you want to play RED (1) or YELLOW (2) (RED goes first)? ");
        realPlayer = scan.nextInt();
        System.out.println();
        
        //builds the board to correct size depending on user choice
        if(game == 1) { gameBoard = new State(3, 3, game); }
        else if(game == 2) { gameBoard = new State(3, 5, game); }
        else if(game == 3) { gameBoard = new State(6, 7, game); }

        String player = gameBoard.getTurn();

        gameBoard.printBoard();

        //runs the game till the game is over via win/loss/draw
        while(!gameOver && (gameBoard.getTurns() < (gameBoard.getBoard().length * gameBoard.getBoard()[0].length)))
        {
            System.out.println();

            player = gameBoard.getTurn();

            System.out.println("Next to play: " + player + "/" + gameBoard.convertPlayer(player));
            if(realPlayer == 1 && player.equals("RED"))
            {
                do
                {
                    System.out.print("Your move [column]? ");
                    char moveCHAR = scan.next().charAt(0);
                    move = gameBoard.convertMove(moveCHAR);
                    System.out.println();
    
                } while(validMove(move, gameBoard.getBoard(), 1) == false);
            }
            else
            {
                if((realPlayer == 1 && player.equals("YELLOW")) || (realPlayer == 2 && player.equals("RED")))
                {
                    //random AI
                    if(AI == 1) { move = randomCol(gameBoard.getBoard()); }
                    //minimax AI
                    else if(AI == 2)
                    {
                        move = minimax_decision("max", gameBoard);
                    }
                    //minimax AI w/ alpha beta pruning
                    else if(AI == 3)
                    {
                        move = ABminimax_decision("max", gameBoard);
                    }
                    //minimax AI w/ hueristic and fixed depth cutoff
                    else if(AI == 4)
                    {
                        move = Hminimax_decision("max", depth, gameBoard);
                    }
                    //minimax AI w/ heuristic, fixed depth cutoff, and alpha beta pruning
                    else if(AI == 5)
                    {
                        move = HABminimax_decision("max", depth, gameBoard);
                    }
                }
                else if(realPlayer == 2 && player.equals("YELLOW"))
                {
                    do
                    {
                        System.out.print("Your move [column]? ");
                        char moveCHAR = scan.next().charAt(0);
                        move = gameBoard.convertMove(moveCHAR);
                        System.out.println();
        
                    } while(validMove(move, gameBoard.getBoard(), 1) == false);
                }
            }   

            gameBoard = result(gameBoard, move, 1);

            gameBoard.printBoard();

            gameOver = gameBoard.checkWinner(player);

            System.out.println("turn: " + gameBoard.getTurns() + "/" + (gameBoard.getBoard().length * gameBoard.getBoard()[0].length));
        }

        System.out.println();
        if(gameOver)
        {
            if(player == "YELLOW") { System.out.println("YELLOW won!"); }
            else if(player == "RED") { System.out.println("RED won!"); }
        }
        else { System.out.println("TIE GAME!"); }

        scan.close();
    }

    //checks if move on board is valid or not
    public static boolean validMove(int move, char[][] gameBoard, int print)
    {
        if(move == -1 || move >= gameBoard[0].length)
        {
            if(print == 1) { System.out.println("not valid column...try again"); }
            return false;
        }
        if(gameBoard[0][move] != ' ')
        {
            if(print == 1) { System.out.println("column full...try again"); }
			return false;
		}
        return true;
    }

    //returns a random column move
    public static int randomCol(char[][] gameBoard)
    {
        int[] moves = checkMoves(gameBoard);
        Random rando = new Random();
        int randoInt;

        randoInt = rando.nextInt(moves.length);
        return moves[randoInt];
    }

    //action function: returns list of available moves for a board
    public static int[] checkMoves(char[][] gameBoard)
    {
        int[] tempMoves = new int[gameBoard[0].length];
        int count = 0;
        for(int i = 0; i < gameBoard[0].length; i++)
        {
            if(validMove(i, gameBoard, 0) == true)
            {
                tempMoves[count] = i;
                count++;
            }
        }
        if(tempMoves.length == count)
        {
            return tempMoves;
        }
        int[] moves = new int[count];
        for(int i = 0; i < moves.length; i++)
        {
            moves[i] = tempMoves[i];
        }

        return moves;
    }

    //result function: returns a state after an action has been placed
    public static State result(State gameBoard, int move, int real)
    {
        if(real != 1)
        {
            State tempBoard = new State(gameBoard.getBoard().length, gameBoard.getBoard()[0].length, gameBoard.game);

            for(int row = 0; row < gameBoard.getBoard().length; row++)
            {
                for(int col = 0; col < gameBoard.getBoard()[0].length; col++)
                {
                    tempBoard.getBoard()[row][col] = gameBoard.getBoard()[row][col];
                }
            }
            tempBoard.turn = gameBoard.turn;

            for(int row = tempBoard.getBoard().length - 1; row >= 0; row--)
            {
                if(tempBoard.getBoard()[row][move] == ' ')
                {
                    tempBoard.getBoard()[row][move] = tempBoard.convertPlayer(tempBoard.getTurn());
                    break;
                }
            }
            tempBoard.switchTurn();
            tempBoard.turns++;
            return tempBoard;
        }
        else
        {
            for(int row = gameBoard.getBoard().length - 1; row >= 0; row--)
            {
                if(gameBoard.getBoard()[row][move] == ' ')
                {
                    gameBoard.getBoard()[row][move] = gameBoard.convertPlayer(gameBoard.getTurn());
                    break;
                }
            }
            gameBoard.switchTurn();
            gameBoard.turns++;
            return gameBoard;
        }
        
    }

    //given a board returns the utility of the board from AI perspective
    public static int utility(State gameBoard)
    {
        String playerColor = "";
        String opponentColor = "";
        if(realPlayer == 2)
        {
            playerColor = "RED";
            opponentColor = "YELLOW";
        }
        else if(realPlayer == 1)
        {
            playerColor = "YELLOW";
            opponentColor = "RED";
        }

        if(gameBoard.checkWinner(playerColor) == true)
        {
            return 1;
        }
        else if(gameBoard.checkWinner(opponentColor) == true)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    //herustic function for heuristic minimax
    public static int heuristicFunction(State gameBoard)
    {
        String AI = "";
        String user = "";
        if(realPlayer == 2)
        {
            AI = "RED";
            user = "YELLOW";
        }
        if(realPlayer == 1)
        {
            AI = "YELLOW";
            user = "RED";
        }

        int value = (gameBoard.threeFinder(AI) + gameBoard.twoFinder(AI)) - (gameBoard.threeFinder(user) - gameBoard.twoFinder(user));
        
        return value;
    }

    //minimax algorithm from AIMA 4th Edition
    public static int minimax_decision(String player, State gameBoard)
    {
        return max_value(player, gameBoard)[1];
    }
    public static int[] max_value(String player, State gameBoard)
    {
        int[] moveSet = new int[2];

        if(player.equals("max"))
        {
            if(realPlayer == 2)
            {
                player = "RED";
            }
            if(realPlayer == 1)
            {
                player = "YELLOW";
            }
        }

        if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
        {
            moveSet[0] = utility(gameBoard);
            return moveSet;
        }

        moveSet[0] = -1000000;
        int[] numMoves = checkMoves(gameBoard.getBoard());

        for(int i = 0; i < numMoves.length; i++)
        {
            int newVal = min_value("min", result(gameBoard, numMoves[i], 0))[0];

            if(newVal > moveSet[0])
            {
                moveSet[0] = newVal;
                moveSet[1] = numMoves[i];
            }
        }
    
        return moveSet;
    }
    public static int[] min_value(String player, State gameBoard)
    {
        int[] moveSet = new int[2];

        if(player.equals("min"))
        {
            if(realPlayer == 2)
            {
                player = "YELLOW";
            }
            if(realPlayer == 1)
            {
                player = "RED";
            }
        }

        if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
        {
            moveSet[0] = utility(gameBoard);
            return moveSet;
        }

        moveSet[0] = 1000000;
        int[] numMoves = checkMoves(gameBoard.getBoard());

        for(int i = 0; i < numMoves.length; i++)
        {
            int newVal = max_value("max", result(gameBoard, numMoves[i], 0))[0];
            if(newVal < moveSet[0])
            {
                moveSet[0] = newVal;
                moveSet[1] = numMoves[i];
            }
        }
        return moveSet;
    }

    //heuristic minimax algorithm
    public static int Hminimax_decision(String player, int depth, State gameBoard)
    {
        return Hmax_value(player, depth, gameBoard)[1];
    }
    public static int[] Hmax_value(String player, int depth, State gameBoard)
    {
        int[] moveSet = new int[2];

        if(player.equals("max"))
        {
            if(realPlayer == 2)
            {
                player = "RED";
            }
            if(realPlayer == 1)
            {
                player = "YELLOW";
            }
        }

        if(depth <= 0 || gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
        {
            if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
            {
                moveSet[0] = utility(gameBoard);
            }
            else { moveSet[0] = heuristicFunction(gameBoard); }
            return moveSet;
        }

        moveSet[0] = -1000000;
        int[] numMoves = checkMoves(gameBoard.getBoard());

        for(int i = 0; i < numMoves.length; i++)
        {
            int newDepth = depth - 1;
            int newVal = Hmin_value("min", newDepth, result(gameBoard, numMoves[i], 0))[0];

            if(newVal > moveSet[0])
            {
                moveSet[0] = newVal;
                moveSet[1] = numMoves[i];
            }
        }

        return moveSet;
    }
    public static int[] Hmin_value(String player, int depth, State gameBoard)
    {
        int[] moveSet = new int[2];

        if(player.equals("min"))
        {
            if(realPlayer == 2)
            {
                player = "YELLOW";
            }
            if(realPlayer == 1)
            {
                player = "RED";
            }
        }

        if(depth <= 0 || gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
        {
            if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
            {
                moveSet[0] = utility(gameBoard);
            }
            else { moveSet[0] = heuristicFunction(gameBoard); }
            return moveSet;
        }

        moveSet[0] = 1000000;
        int[] numMoves = checkMoves(gameBoard.getBoard());

        for(int i = 0; i < numMoves.length; i++)
        {
            int newDepth = depth - 1;
            int newVal = Hmax_value("max", newDepth, result(gameBoard, numMoves[i], 0))[0];
            if(newVal < moveSet[0])
            {
                moveSet[0] = newVal;
                moveSet[1] = numMoves[i];
            }
        }
        return moveSet;
    }

    //alpha beta pruning minimax algorithm from AIMA 4th Edition
    public static int ABminimax_decision(String player, State gameBoard)
    {
        return ABmax_value(player, gameBoard, -1000000, 1000000)[1];
    }
    public static int[] ABmax_value(String player, State gameBoard, int alpha, int beta)
    {
        int[] moveSet = new int[2];

        if(player.equals("max"))
        {
            if(realPlayer == 2)
            {
                player = "RED";
            }
            if(realPlayer == 1)
            {
                player = "YELLOW";
            }
        }

        if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
        {
            moveSet[0] = utility(gameBoard);
            return moveSet;
        }

        moveSet[0] = -1000000;
        int[] numMoves = checkMoves(gameBoard.getBoard());

        for(int i = 0; i < numMoves.length; i++)
        {
            int newVal = ABmin_value("min", result(gameBoard, numMoves[i], 0), alpha, beta)[0];
            if(newVal > moveSet[0])
            {
                moveSet[0] = newVal;
                moveSet[1] = numMoves[i];
                if(moveSet[0] > alpha) { alpha = moveSet[0]; }
            }
            if(moveSet[0] >= beta) { return moveSet; }
        }
        return moveSet;
    }
    public static int[] ABmin_value(String player, State gameBoard, int alpha, int beta)
    {
        int[] moveSet = new int[2];

        if(player.equals("max"))
        {
            if(realPlayer == 2)
            {
                player = "RED";
            }
            if(realPlayer == 1)
            {
                player = "YELLOW";
            }
        }

        if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
        {
            moveSet[0] = utility(gameBoard);
            return moveSet;
        }

        moveSet[0] = 1000000;
        int[] numMoves = checkMoves(gameBoard.getBoard());

        for(int i = 0; i < numMoves.length; i++)
        {
            int newVal = ABmax_value("max", result(gameBoard, numMoves[i], 0), alpha, beta)[0];
            if(newVal < moveSet[0])
            {
                moveSet[0] = newVal;
                moveSet[1] = numMoves[i];
                if(moveSet[0] < beta) { beta = moveSet[0]; }
            }
            if(moveSet[0] <= alpha) { return moveSet; }
        }
        return moveSet;
    }

        //alpha beta pruning heuristic minimax algorithm
        public static int HABminimax_decision(String player, int depth, State gameBoard)
        {
            return HABmax_value(player, depth, gameBoard, -1000000, 1000000)[1];
        }
        public static int[] HABmax_value(String player, int depth, State gameBoard, int alpha, int beta)
        {
            int[] moveSet = new int[2];
    
            if(player.equals("max"))
            {
                if(realPlayer == 2)
                {
                    player = "RED";
                }
                if(realPlayer == 1)
                {
                    player = "YELLOW";
                }
            }
    
            if(depth <= 0 || gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
            {
                if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
                {
                    moveSet[0] = utility(gameBoard);
                }
                else { moveSet[0] = heuristicFunction(gameBoard); }
                return moveSet;
            }
    
            moveSet[0] = -1000000;
            int[] numMoves = checkMoves(gameBoard.getBoard());
    
            for(int i = 0; i < numMoves.length; i++)
            {
                int newDepth = depth - 1;
                int newVal = HABmin_value("min", newDepth, result(gameBoard, numMoves[i], 0), alpha, beta)[0];
                if(newVal > moveSet[0])
                {
                    moveSet[0] = newVal;
                    moveSet[1] = numMoves[i];
                    if(moveSet[0] > alpha) { alpha = moveSet[0]; }
                }
                if(moveSet[0] >= beta) { return moveSet; }
            }
            return moveSet;
        }
        public static int[] HABmin_value(String player, int depth, State gameBoard, int alpha, int beta)
        {
            int[] moveSet = new int[2];
    
            if(player.equals("max"))
            {
                if(realPlayer == 2)
                {
                    player = "RED";
                }
                if(realPlayer == 1)
                {
                    player = "YELLOW";
                }
            }
    
            if(depth <= 0 || gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
            {
                if(gameBoard.checkWinner("RED") == true || gameBoard.checkWinner("YELLOW") == true || checkMoves(gameBoard.getBoard()).length == 0)
                {
                    moveSet[0] = utility(gameBoard);
                }
                else { moveSet[0] = heuristicFunction(gameBoard); }
                return moveSet;
            }
    
            moveSet[0] = 1000000;
            int[] numMoves = checkMoves(gameBoard.getBoard());
    
            for(int i = 0; i < numMoves.length; i++)
            {
                int newDepth = depth - 1;
                int newVal = HABmax_value("max", newDepth, result(gameBoard, numMoves[i], 0), alpha, beta)[0];
                if(newVal < moveSet[0])
                {
                    moveSet[0] = newVal;
                    moveSet[1] = numMoves[i];
                    if(moveSet[0] < beta) { beta = moveSet[0]; }
                }
                if(moveSet[0] <= alpha) { return moveSet; }
            }
            return moveSet;
        }
}