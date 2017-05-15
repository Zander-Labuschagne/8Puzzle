/**
 * @author Zander Labuschagne 23585137
 * Class to manage Number Nodes(Game Engine)
 * This software was designed to be independent of GUI
 */

import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.util.Random;
import java.util.Scanner;
//GUI
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
//Audio
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NumberMap
{
    //Instance Variables
    /**
     * the starting state of the game presented as a matrix
     */
    private int[][] stateIn;
    /**
     * the current state of the game
     * should be updated after every move
     */
    private int[][] activeState;
    /**
     * node which is presented as the origin node
     * see mutator for more details
     */
    private NumberNode origin;
    /**
     * node which is presented as the empty space
     */
    private NumberNode nullNode;
    /**
     * integer which contains the amount of moves made
     * updated after every move
     */
    protected int countMoves;
    /**
     * Matrix to be used to determine end game
     */
    private int[][] goalState;
    /**
     * object of IO class to handle all file io
     */
    private IO gameTracker;
    /**
     * string containing last move vector(left;right;up;down)
     */
    private String lastMoveVector;
    /**
     * int containing last number moved
     */
    private int lastMove;
    /**
     * Number of nodes/blocks contained in the x-axis
     */
    private int y_axisNodes;
    /**
     * Number of nodes/blocks contained in the y-axis
     */
    private int x_axisNodes;

    public boolean pc;

    /**
     * Default Constructor
     */
    public NumberMap()
    {
        this(null, null);
    }

    /**
     * Overloaded Constructor
     * @param stateIn The state in which the game should start, presented in matrix format
     */
    public NumberMap(int[][] stateIn)
    {
        this(stateIn, null);
    }

    /**
     * Overloaded Constructor
     * @param stateIn The state in which the game should start, presented in matrix format
     * @param winState The state in which the game is won, presented in matrix format
     */
    public NumberMap(int[][] stateIn, int[][] winState)
    {
        setNullNode(null);
        setOrigin(null);
        setY_axis(3);
        setX_axis(3);

        if(winState == null)
            setWinState(new int[][]{{7, 6, 5}, {8, 0, 4}, {1, 2, 3}});
        else
            setWinState(winState);

        if(stateIn == null)
        {
            setRandomState();
        }
        else
            setStateIn(stateIn);

        countMoves = 0;
        pc = false;
    }

    //Mutators
    /**
     * set the origin node
     * Multi Dimensional Doubly Linked List Data Structure Presented in the form of a Cartesian Pane
     * @param origin the node object which should be set/considered as the origin node
     */
    public void setOrigin(NumberNode origin)
    {
        this.origin = origin;
    }

    /**
     * sets the null node
     * The null node is the node occupying the empty block
     * @param nullNode the node object which should be set/considered as the empty block/node
     */
    public void setNullNode(NumberNode nullNode)
    {
        this.nullNode = nullNode;
    }

    private void setX_axis(int x)
    {
        x_axisNodes = x;
    }

    private void setY_axis(int y)
    {
        y_axisNodes = y;
    }

    /**
     * Awesome Goed Gebeur
     * Builds the Multi Dimensional Doubly Linked List Data Structure
     * Configure the starting game state
     * @param stateIn 2 Dimensional Array that represents the starting state of the game
     */
    public void setStateIn(int[][] stateIn)
    {
        try
        {
            this.stateIn = stateIn;
            //Create Data Structure
            NumberNode node = new NumberNode(getStateIn()[0][0], 0, getY_axis() - 1);
            NumberNode prev;
            for (int x = 0; x < getX_axis(); x++)
            {
                int y = 0;

                if (x != 0)
                {
                    prev = getNodeAt(x - 1, getY_axis() - 1);
                    prev.setRight(new NumberNode(getStateIn()[y][x], x, (getY_axis() - 1 - y)));
                    prev.getRight().setLeft(prev);
                    node = prev.getRight();
                }
                if (getStateIn()[y][x] == 0)
                    setNullNode(node);

                for (y = getY_axis() - 2; y >= 0; y--)
                {
                    node.setDown(new NumberNode(getStateIn()[getY_axis() - 1 - y][x], x, y));
                    node.getDown().setUp(node);
                    node = node.getDown();
                    if (getStateIn()[getY_axis() - 1 - y][x] == 0)
                        setNullNode(node);

                    if (x > 0)
                    {
                        NumberNode ptr = getNodeAt(x - 1, y);
                        node.setLeft(ptr);
                        node.getLeft().setRight(node);
                    }

                    if (x == 0 && y == 0)
                        setOrigin(node);
                }
            }
            setActiveState();

            gameTracker = new IO("Game_Tracker.txt");
            gameTracker.createFile();
            gameTracker.printToFile("Initial State:\n" + toString() + "\n");
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void setStateIn(int[][])", JOptionPane.ERROR_MESSAGE, icon);
            ex.printStackTrace();
        }
    }

    /**
     * sets a randomized state to be built as a data structure
     * used as initial game state
     */
    public void setRandomState()
    {
        try
        {
            Random ra = new Random();
            int[][] a = new int[getY_axis()][getX_axis()];
            int random_number = ra.nextInt(getY_axis() * getX_axis() + 1);
            int iii = 0;

            for (int i = 0; i < getX_axis(); i++)
                for (int ii = 0; ii < getY_axis(); ii++)
                {
                    while (contains(a, random_number) && iii < (getX_axis() * getY_axis()))
                    {
                        random_number = ra.nextInt(getY_axis() * getX_axis() + 1);
                    }

                    a[ii][i] = random_number;
                    iii++;
                }

            for (int i = 0; i < getX_axis(); i++)
                for (int ii = 0; ii < getY_axis(); ii++)
                    if (a[ii][i] == getY_axis() * getX_axis())
                        a[ii][i] = 0;

            setActiveState(a);
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void setRandomState()", JOptionPane.ERROR_MESSAGE, icon);
        }
    }

    /**
     * Updates the matrix to be used as a reference throughout the game
     * Should be called after every move
     */
    public void setActiveState()
    {
        int[][] a = new int[getY_axis()][getX_axis()];
        for(int i = 0; i < getY_axis(); i++)
            for(int ii = 0; ii < getX_axis(); ii++)
                a[i][ii] = getNodeAt(ii, i).getInfo();

        activeState = a;

        stateIn = a;
    }

    /**
     * sets the current state of the game after move or initialization
     * @param activeState the matrix to configure or update the data structure
     */
    public void setActiveState(int[][] activeState)
    {
        setStateIn(activeState);
    }

    /**
     * sets the winning state of the game to determine end game
     * @param goalState the matrix to be used to compare with the active game state to determine end game
     */
    public void setWinState(int[][] goalState)
    {
        this.goalState = goalState;
    }

    /**
     * sets the lastmove
     * @param lastMove the last move
     */
    public void setLastMoveVector(String lastMove)
    {
        this.lastMoveVector = lastMove;
    }

    /**
     * sets the node that made the last move
     * used by GUI
     * @param move
     */
    public void setLastMove(int move)
    {
        lastMove = move;
    }

    //Accessors
    /**
     * get the amount of moves made
     * @return number of moves made
     */
    public int getCountMoves()
    {
        return countMoves;
    }

    /**
     * get the origin node reference to find or reference other nodes
     * @return the origin node reference
     */
    public NumberNode getOrigin()
    {
        return origin;
    }

    /**
     * get the empty space node
     * @return the empty space node reference
     */
    public NumberNode getNullNode()
    {
        return nullNode;
    }

    /**
     * Request a node by value
     * No node should ever hold the same value
     * @param value the value that the requested node sshould hold
     * @return node reference congaing the requested value
     */
    public NumberNode getNode(int value)
    {
        NumberNode ptr = getOrigin();

        try
        {
            for(int x = 0; x < getX_axis() && ptr.getInfo() != value; x++)
                for(int y = 0; y < getY_axis() && ptr.getInfo() != value; y++)
                    ptr = getNodeAt(x, y);

            if(ptr == null)
                throw new NullPointerException("The node with the queried value does not exist.\ngetNode(int value) returned null");
        }
        catch(NullPointerException npex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, npex.getMessage() + "\nPlease contact the developer immediately.", "Non existent node value", JOptionPane.ERROR_MESSAGE, icon);
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.getMessage() + "\nPlease contact the developer immediately.", "Error: NumberNode getNode(int value)", JOptionPane.ERROR_MESSAGE, icon);
        }

        return ptr;
    }

    /**
     * Request a node by coordinates
     * No node should ever hold the same coordinates
     * @param x latitude(x-axis) coordinate to find requested node
     * @param y longitude(y-axis) coordinate to find requested node
     * @return reference to the requested node
     */
    public NumberNode getNodeAt(int x, int y)
    {
        NumberNode ptr = getOrigin();

        try
        {
            if(x == 0 && y == 0)
                return ptr;

            if(x > (getX_axis() - 1) || y > (getY_axis() - 1) || x < 0 || y < 0)
                throw new NullPointerException("The node at the queried coordinates does not exist in the given range.\nNumberNode getNodeAt(int x, int y) returned null");

            int lat = 0, longi = 0;
            while(x != lat || y != longi)
            {
                if(lat < x)
                {
                    ptr = ptr.getRight();
                    lat++;
                }
                if(longi < y)
                {
                    ptr = ptr.getUp();
                    longi++;
                }
            }
        }
        catch(NullPointerException npex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, npex.getMessage() + "\nPlease contact the developer immediately.", "Node coordinates out of range", JOptionPane.ERROR_MESSAGE, icon);
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.getMessage() + "\nPlease contact the developer immediately.", "Error: NumberNode getNodeAt(int x, int y)", JOptionPane.ERROR_MESSAGE, icon);
        }

        return ptr;

    }

    /**
     * get the winning state
     * @return matrix representing the end game
     */
    public int[][] getGoalState()
    {
        return goalState;
    }

    /**
     * get the initial/start state of the game
     * @return matrix which represent the initial state
     */
    public int[][] getStateIn()
    {
        return stateIn;
    }

    /**
     * get the current game state
     * @return matrix which represent the current game state
     */
    public int[][] getActiveState()
    {
        return activeState;
    }

    /**
     * get the last move made's vector
     * @return string containing last move vector
     */
    public String getLastMoveVector()
    {
        return lastMoveVector;
    }

    /**
     * get the last move made's number
     * @return ineger containing the last move's number
     */
    public int getLastMove()
    {
        return lastMove;
    }

    /**
     * get the x-axis nodes
     * @return
     */
    protected int getX_axis()
    {
        return x_axisNodes;
    }

    /**
     * get the y-axis nodes
     * @return
     */
    protected int getY_axis()
    {
        return y_axisNodes;
    }

    /**
     * Matrix representation of the initial game state in string format
     * @return string formatted matrix representation of the initial game
     */
    public String toStringInitial()
    {
        String state = "";
        for(int i = 0; i < getY_axis(); i++)
        {
            for(int ii = 0; ii < getX_axis(); ii++)
                state += (getStateIn()[getY_axis() - 1 - i][ii] == 0 ? "B " : String.format("%d ", getStateIn()[getY_axis() - 1 - i][ii]));
            state += "\n";
        }

        return state;
    }

    /**
     * Matrix representation of the current game state in string format
     * @return string formatted matrix representation of the game
     */
    public String toString()
    {
        String state = "";
        for(int i = 0; i < getY_axis(); i++)
        {
            for(int ii = 0; ii < getX_axis(); ii++)
                state += (getActiveState()[getY_axis() - 1 - i][ii] == 0 ? "B " : String.format("%d ", getActiveState()[getY_axis() - 1 - i][ii]));
            state += "\n";
        }

        return state;
    }

    /**
     * Comma separated value formatted representation of the current game state to be used to write/output to .csv file
     * @return string formatted comma separated value representation of current game state
     */
    public String toCSV()
    {
        String state = "";
        for(int i = 0; i < getY_axis(); i++)
        {
            for(int ii = 0; ii < getX_axis(); ii++)
                state += (getActiveState()[getY_axis() - 1 - i][ii] == 0 ? 'B' : String.format("%d", getActiveState()[getY_axis() - 1 - i][ii])) + (i == (getY_axis() - 1) && ii == (getX_axis() - 1) ? "" : ", ");
        }

        return state;
    }

    /**
     * Comma separated value formatted representation of the end game state to be used to write/output to .csv file
     * @return string formatted comma separated value representation of end game state
     */
    public String toCSVGoal()
    {
        String state = "";
        for(int i = 0; i < getY_axis(); i++)
        {
            for(int ii = 0; ii < getX_axis(); ii++)
                state += (getGoalState()[getY_axis() - 1 - i][ii] == 0 ? 'B' : String.format("%d", getGoalState()[getY_axis() - 1 - i][ii])) + (i == (getY_axis() - 1) && ii == (getX_axis() - 1) ? "" : ", ");
        }

        return state;
    }

    /**
     * Print the current game state to console in matrix format
     */
    public void printActiveState()
    {
        System.out.print(toString() + '\n');
    }

    /**
     * Determine if game won
     * @return true if game won
     * play win sound in new thread
     */
    public boolean gameStatus()
    {
        for(int i = 0; i < getY_axis(); i++)
            for(int ii = 0; ii < getX_axis(); ii++)
                if(getActiveState()[i][ii] != getGoalState()[i][ii])
                    return false;

            new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/win2.wav"));
                        clip.open(inputStream);
                        clip.start();
                        inputStream.close();
                    }
                    catch(Exception ex)
                    {
                        final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
                        JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: boolean gameStatus()", JOptionPane.ERROR_MESSAGE, icon);
                    }
                }
            }).start();
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/info-green.png"));
            if(pc)
                JOptionPane.showMessageDialog(null, String.format("Wow!\nYour computer have completed the puzzle in %d moves.\nThe machines are catching onto us...", getCountMoves()), "Puzzle Completed!", JOptionPane.INFORMATION_MESSAGE, icon);
            else
                JOptionPane.showMessageDialog(null, String.format("Congratulations!\nYou have completed the puzzle in %d moves.\nTime for some coffee...", getCountMoves()), "Puzzle Completed!", JOptionPane.INFORMATION_MESSAGE, icon);
            if(!pc)
                gameTracker.printToFile(String.format("GAME WON! in %d moves\n\n\n", getCountMoves()));
            countMoves = 0;
            setLastMove(0);
            setLastMoveVector("None");
            return true;
    }


    /**
     * Calls the correct function that moves the node holding the number
     * Only a node adjacent to the null node can be moved into the null node's position, by swapping this node with the null node
     * @param number value of the node to be moved
     */
    public void move(int number)
    {
        NumberNode node = getNode(number);

        if(node.getLeft() != null && node.getLeft().isNullNode())
            moveLeft(node);
        else if(node.getRight() != null && node.getRight().isNullNode())
            moveRight(node);
        else if(node.getUp() != null && node.getUp().isNullNode())
            moveUp(node);
        else if(node.getDown() != null && node.getDown().isNullNode())
            moveDown(node);
    }

    /**
     * Move the node a block left, by swapping this node with the null node
     * Will only activate if the null node is left adjacent to this node
     * @param node node which is requested to move
     */
    protected void moveLeft(NumberNode node)
    {
        try
        {
            if (node.getUp() != null)
            {
                node.setUp(node.getUp().getLeft());
                node.getUp().setDown(node);

                node.getUp().getRight().setDown(getNullNode());
                getNullNode().setUp(node.getUp().getRight());
            }
            if (node.getDown() != null)
            {
                node.setDown(node.getDown().getLeft());
                node.getDown().setUp(node);

                node.getDown().getRight().setUp(getNullNode());
                getNullNode().setDown(node.getDown().getRight());
            }
            if (node.getLeft() != null && node.getLeft().getLeft() != null)//Kyk of node in derde kolom is
            {
                node.setLeft(node.getLeft().getLeft());
                node.getLeft().setRight(node);
            }
            if (node.getRight() != null)
            {
                node.getRight().setLeft(getNullNode());
                getNullNode().setRight(node.getRight());
            }
            if (node.getRight() == null)
                getNullNode().setRight(null);
            if (getNullNode().getLeft() == null)
                node.setLeft(null);

            node.setRight(getNullNode());
            getNullNode().setLeft(node);

            if (getOrigin().isNullNode())
                setOrigin(node);

            node.setX(node.getX() - 1);
            getNullNode().setX(getNullNode().getX() + 1);

            setActiveState();

            countMoves++;
            addMove(node.getInfo(), "Left");
            setLastMoveVector("left");
            setLastMove(node.getInfo());
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void moveLeft(NumberNode node)", JOptionPane.ERROR_MESSAGE, icon);
        }
    }

    /**
     * Move the node a block right, by swapping it with the null node
     * Will only activate if the null node is right adjacent to this node
     * @param node node which is requested to move
     */
    protected void moveRight(NumberNode node)
    {
        try
        {
            if (getOrigin().getInfo() == node.getInfo())
            {
                setOrigin(getNullNode());
                getNullNode().setX(0);
                getNullNode().setY(0);
            }

            if (node.getUp() != null)
            {
                node.setUp(node.getUp().getRight());
                node.getUp().setDown(node);

                node.getUp().getLeft().setDown(getNullNode());
                getNullNode().setUp(node.getUp().getLeft());
            }
            if (node.getDown() != null)
            {
                node.setDown(node.getDown().getRight());
                node.getDown().setUp(node);

                node.getDown().getLeft().setUp(getNullNode());
                getNullNode().setDown(node.getDown().getLeft());
            }
            if (node.getRight() != null && node.getRight().getRight() != null)//Kyk of node in derde kolom is
            {
                node.setRight(node.getRight().getRight());
                node.getRight().setLeft(node);
            }
            if (node.getLeft() != null)
            {
                node.getLeft().setRight(getNullNode());
                getNullNode().setLeft(node.getLeft());
            }
            if (node.getLeft() == null)
                getNullNode().setLeft(null);
            if (getNullNode().getRight() == null)
                node.setRight(null);

            node.setLeft(getNullNode());
            getNullNode().setRight(node);

            node.setX(node.getX() + 1);
            getNullNode().setX(getNullNode().getX() - 1);

            setActiveState();
            countMoves++;
            addMove(node.getInfo(), "Right");
            setLastMoveVector("right");
            setLastMove(node.getInfo());
           // if(!pc)
             //   gameStatus();
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void moveRight(NumberNode node)", JOptionPane.ERROR_MESSAGE, icon);
        }
    }

    /**
     * Move the node a block up, by swapping it with the null node
     * Will only activate if the null node is upwards adjacent to this node
     * @param node node which is requested to move
     */
    protected void moveUp(NumberNode node)
    {
        try
        {
            if (node.getLeft() != null)
            {
                node.setLeft(node.getLeft().getUp());
                node.getLeft().setRight(node);

                node.getLeft().getDown().setRight(getNullNode());
                getNullNode().setLeft(node.getLeft().getDown());
            }
            if (node.getRight() != null)
            {
                node.setRight(node.getRight().getUp());
                node.getRight().setLeft(node);

                node.getRight().getDown().setLeft(getNullNode());
                getNullNode().setRight(node.getRight().getDown());
            }
            if (node.getUp() != null && node.getUp().getUp() != null)//Kyk of node in derde kolom is
            {
                node.setUp(node.getUp().getUp());
                node.getUp().setDown(node);
            }
            if (node.getDown() != null)
            {
                node.getDown().setUp(getNullNode());
                getNullNode().setDown(node.getDown());
            }
            if (node.getDown() == null)
                getNullNode().setDown(null);
            if (getNullNode().getUp() == null)
                node.setUp(null);

            node.setDown(getNullNode());
            getNullNode().setUp(node);

            node.setY(node.getY() + 1);
            getNullNode().setY(getNullNode().getY() - 1);

            if (getOrigin().getInfo() == node.getInfo())
            {
                setOrigin(getNullNode());
                getNullNode().setX(0);
                getNullNode().setY(0);
            }

            setActiveState();
            countMoves++;
            addMove(node.getInfo(), "Up");
            setLastMoveVector("up");
            setLastMove(node.getInfo());
            //if(!pc)
              //  gameStatus();
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void moveUp(NumberNode node)", JOptionPane.ERROR_MESSAGE, icon);
        }
    }

    /**
     * Move the node a block down, by swapping it with the null node
     * Will only activate if the null node is downwards adjacent to this node
     * @param node node which is requested to move
     */
    protected void moveDown(NumberNode node)
    {
        try
        {
            if (node.getLeft() != null)
            {
                node.setLeft(node.getLeft().getDown());
                node.getLeft().setRight(node);

                node.getLeft().getUp().setRight(getNullNode());
                getNullNode().setLeft(node.getLeft().getUp());
            }
            if (node.getRight() != null)
            {
                node.setRight(node.getRight().getDown());
                node.getRight().setLeft(node);

                node.getRight().getUp().setLeft(getNullNode());
                getNullNode().setRight(node.getRight().getUp());
            }
            if (node.getDown() != null && node.getDown().getDown() != null)//Kyk of node in derde kolom is
            {
                node.setDown(node.getDown().getDown());
                node.getDown().setUp(node);
            }
            if (node.getUp() != null)
            {
                node.getUp().setDown(getNullNode());
                getNullNode().setUp(node.getUp());
            }
            if (node.getUp() == null)
                getNullNode().setUp(null);
            if (getNullNode().getDown() == null)
                node.setDown(null);

            node.setUp(getNullNode());
            getNullNode().setDown(node);

            if (getOrigin().isNullNode())
                setOrigin(node);

            node.setY(node.getY() - 1);
            getNullNode().setY(getNullNode().getY() + 1);

            setActiveState();
            countMoves++;
            addMove(node.getInfo(), "Down");
            setLastMoveVector("down");
            setLastMove(node.getInfo());
            //if(!pc)
              //  gameStatus();
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void moveDown(NumberNode node)", JOptionPane.ERROR_MESSAGE, icon);
        }
    }

    /**
     * Adds the move to the current game history
     * Will only record successful moves
     * Writes the move details to a text file
     * @param number move number
     * @param direction move direction
     */
    public void addMove(int number, String direction)
    {
        if(!pc)
        {
            gameTracker.printToFile(String.format("Move %d: %d Moved %s\n", getCountMoves(), number, direction));
            gameTracker.printToFile(toString() + "\n");
        }
    }

    /**
     * Saves the current game state if requested
     * Writes the current game state in comma separated value format into a .csv file to be loaded in future
     * Prompts user to enter the directory in which the .csv will be saved
     * @param fileName file name to save game as
     */
    protected void saveStateCode(String fileName)
    {
        try
        {
            if (!(fileName.contains(".")) || (!fileName.substring(fileName.lastIndexOf('.'), fileName.length()).equals(".csv")))
                throw new Exception("Please add the extension \".csv\" after the filename");
            IO save = new IO(fileName);
            save.createFile();
            save.printToFile(toCSV());
            save.printToFile("\n");
            save.printToFile(toCSVGoal());
            save.printToFile("\n");
            save.printToFile(String.format("%d\n", getCountMoves()));
            save.close();
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/info-blue.png"));
            JOptionPane.showMessageDialog(null, "Game saved at  " + fileName, "Game Save Successful", JOptionPane.INFORMATION_MESSAGE, icon);
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/error-red.png"));
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error: Game Save Failed", JOptionPane.ERROR_MESSAGE, icon);
        }
    }
    /**
     * Saves the current game state if requested
     * Writes the current game state in comma separated value format into a .csv file to be loaded in future
     * Prompts user to enter the directory in which the .csv will be saved
     * @param fileName file name to save game as
     */
    public void saveState(String fileName)
    {
        saveStateCode(fileName);
    }

    /**
     * Loads a requested game state from a .csv in comma separated value format
     * Reads the comma separated values from .csv file into current game state
     * Prompts user to enter the directory of the .csv file to load
     * if no state is loaded a randomized state will be generated
     * @param fileName name of file to save game as
     */
    protected void loadStateCode(String fileName)
    {
        try
        {
            IO io = new IO(fileName);
            Scanner input = new Scanner(io.getFile());
            String sNumbers = input.nextLine();
            String sWinNumbers = input.nextLine();
            String optional = null;
            countMoves = 0;
            if(input.hasNextLine())
            {
                optional = input.nextLine();
                countMoves = Integer.parseInt(optional);
            }

            String[] arrsNumbers;
            String[] arrsWinNumbers;

            CharSequence cs1 = ", ";
            CharSequence cs2 = ",";
            if(sNumbers.contains(cs1))
                arrsNumbers = sNumbers.split(", ");
            else if(sNumbers.contains(cs2))
                arrsNumbers = sNumbers.split(",");
            else
                throw new FileSystemException("The format of the .csv file is incorrect\nPlease use as follows:\n1, 2, 3, 4, 5, 6, 7, 8, B\n1,2,3,4,5,6,7,8,B\nWhere B denote the Blank block.");

            if(sWinNumbers.contains(cs1))
                arrsWinNumbers = sWinNumbers.split(", ");
            else if(sWinNumbers.contains(cs2))
                arrsWinNumbers = sWinNumbers.split(",");
            else
                throw new FileSystemException("The format of the .csv file is incorrect\nPlease use as follows:\n1, 2, 3, 4, 5, 6, 7, 8, B\n1,2,3,4,5,6,7,8,B\nWhere B denote the Blank block.");

            for(int i = 0; i < arrsNumbers.length; i++)
                if(arrsNumbers[i].equals("B"))
                    arrsNumbers[i] = "0";

            for(int i = 0; i < arrsWinNumbers.length; i++)
                if(arrsWinNumbers[i].equals("B"))
                    arrsWinNumbers[i] = "0";

            int[][] arrIn = new int[getY_axis()][getX_axis()];
            int iii = 0;
            for(int i = 0; i < getY_axis(); i++)
                for(int ii = 0; ii < getX_axis(); ii++)
                    arrIn[i][ii] = Integer.parseInt(arrsNumbers[iii++]);

            int[][] arrWinIn = new int[getY_axis()][getX_axis()];
            int iv = 0;
            for(int i = getY_axis() - 1; i >= 0; i--)
                for(int ii = 0; ii < getX_axis(); ii++)
                    arrWinIn[i][ii] = Integer.parseInt(arrsWinNumbers[iv++]);

            input.close();
            setActiveState(arrIn);
            setWinState(arrWinIn);
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/info-blue.png"));
            JOptionPane.showMessageDialog(null, "Game Loaded From " + fileName + "\nEnjoy and don't ever give up on anything.", "Game Load Successful", JOptionPane.INFORMATION_MESSAGE, icon);
        }
        catch(FileNotFoundException fnfex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/error-red.png"));
            JOptionPane.showMessageDialog(null, fnfex.getMessage() + "\nTo load a game state from .csv file, have a input.csv file in the game root directory.", "File Not Found", JOptionPane.ERROR_MESSAGE, icon);
        }
        catch(FileSystemException fsex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/error-red.png"));
            JOptionPane.showMessageDialog(null, fsex.getMessage(), "Incorrect .csv format", JOptionPane.ERROR_MESSAGE, icon);
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/error-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease make sure the .csv file is in the correct format.\nThe file should contain two lines of comma separated values,\none line for the starting game state and a second for the winning state.", "Error: Game Load Failed", JOptionPane.ERROR_MESSAGE, icon);
        }
    }

    /**
     * Loads a requested game state from a .csv in comma separated value format
     * Reads the comma separated values from .csv file into current game state
     * Prompts user to enter the directory of the .csv file to load
     * if no state is loaded a randomized state will be generated
     * @param fileName name of file to save game as
     */
    public void loadState(String fileName)
    {
        loadStateCode(fileName);
    }

    /**
     * Function only used by setRandomState function
     * This function should never be used in conjunction with anything else
     * Only the original author determines the usage of this function
     * Include and consult the original author before using this function for optimal software reliability, safety and functionality
     * @param a consult the original author
     * @param b consult the original author
     * @return consult the original author
     */
    private boolean contains(int[][] a, int b)
    {
        for(int i = 0; i < a.length; i++)
            for(int ii : a[i])
                if(ii == b)
                    return true;

        return false;
    }

    /**
     * Act as a destructor for cleaning up possible malfunctions and cleaning up memory after game has terminated
     * @throws Throwable
     */
    @Override
    protected void finalize()
    {
        try
        {
            super.finalize();
            gameTracker.close();
        }
        catch(Throwable ex)
        {
            ex.printStackTrace();
        }
    }
}