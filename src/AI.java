/**
 * @author Zander Labuschagne 23585137
 * Class to perform the AI part
 */

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;

public class AI
{
    //Instance Variables
    private NumberMapNode root;
    private LinkedList<String> open2 = new LinkedList<String>();
    private LinkedList<String> closed2 = new LinkedList<String>();

    /**
     * Default Constructor
     */
    public AI()
    {
        this(null, null);
    }
    /**
     * Overloaded Constructor
     */
    public AI(int[][] startState, int[][] goalState)
    {
        root = new NumberMapNode(invert(startState), 0, goalState, null);
    }

    /**
     * Inverts the matrix on the y-axis
     * @param state to be inverted
     * @return inverted state
     */
    private int[][] invert(int[][] state)
    {
        int[][]a = new int[3][3];
        for(int i = 0 ; i < state.length; i++)
            a[i] = state[2 - i];
        return a;
    }

    /**
     * Find the solution path to goal state
     * @return array of numbers needed to move to reach goal state
     */
    public int[] findSolutionPath()
    {
        NumberMapNode ptr = root;
        int[] path;
        LinkedList<NumberMapNode> open = new LinkedList<NumberMapNode>();
        LinkedList<NumberMapNode> closed = new LinkedList<NumberMapNode>();
        open.add(root);
        open2.add(root.toString());
        int minF;
        Queue<NumberMapNode> queue = new Queue<NumberMapNode>();
        queue.enqueue(ptr);
        int[] levels = new int[1000];
        levels[0] = 1;
        ptr.parent = null;

        while(!queue.isEmpty())
        {
            minF = 1000;
            ptr = queue.dequeue();
            levels[ptr.getLevel()]--;
            if(!ptr.gameStatus())
            {
                int x = ptr.getValidMoves().length;

                //Generate Children
                for (int i = 0; i < ptr.getValidMoves().length; i++)
                {
                    NumberMapNode temp = new NumberMapNode(invert(ptr.getActiveState()), ptr.getLevel() + 1, ptr.getGoalState(), ptr);
                    temp.move(ptr.getValidMoves()[i]);
                    temp.setValidMoves();
                    temp.setF();
                    if (!open2.contains(temp.toString()) && temp.getLevel() > ptr.getLevel())
                    {
                        ptr.setNext(temp);
                        if (minF > temp.getF() || i == 0)
                            minF = temp.getF();

                    }
                    else
                        x--;
                }
                if(x != 0)
                {
                    //Trim tree to promising children
                    for (int ix = 0, iii = 0; ix < x; iii++, ix++)
                    {
                        if (ptr.getNext().get(iii).getF() == minF)
                        {
                            if(closed2.contains(ptr.getNext().get(iii).toString()))
                            {
                                int ii = closed2.indexOf(ptr.getNext().get(iii).toString());
                                if(ptr.getNext().get(iii).getLevel() > closed.get(ii).getLevel())
                                {
                                    NumberMapNode n;
                                    n = closed.get(ii);

                                    closed2.remove(ii);
                                    closed.remove(ii);

                                    queue.clear();

                                    ptr.killAllChildren();

                                    ptr = n.parent;
                                    ptr.killAllChildren();
                                    ptr.setNext(n);
                                    ix = x;

                                    open.clear();
                                    open2.clear();
                                    int iv = n.getLevel();
                                    NumberMapNode a = ptr;
                                    while (iv > 0)
                                    {
                                        open.addFirst(a);
                                        open2.addFirst(a.toString());
                                        a = a.parent;
                                        iv--;
                                    }
                                }
                            }
                            levels[ptr.getLevel() + 1]++;
                        }
                        else
                        {
                            closed2.add(ptr.getNext().get(iii).toString());
                            closed.add(ptr.getNext().get(iii));
                            ptr.getNext().remove(ptr.getNext().get(iii));
                            iii--;
                        }
                    }

                    for (int ii = 0; ii < ptr.getNext().size(); ii++)
                    {
                        queue.enqueue(ptr.getNext().get(ii));
                        open.addLast(ptr.getNext().get(ii));
                        open2.addLast(ptr.getNext().get(ii).toString());
                    }

                    if (levels[ptr.getLevel()] == 0)
                    {
                        //search for lowest f in queue
                        Queue<NumberMapNode> q = new Queue<NumberMapNode>();

                        int lowestF = queue.peek().getF();

                        while (!queue.isEmpty())
                        {
                            NumberMapNode n = queue.dequeue();
                            if (lowestF > n.getF())
                                lowestF = n.getF();
                            q.enqueue(n);
                        }
                        //Take out all high F nodes from Queue
                        boolean c = false;
                        while (!q.isEmpty())
                        {
                            NumberMapNode object = q.dequeue();
                            if (object.getF() == lowestF)
                                queue.enqueue(object);
                            else
                            {
                                int ii = open2.indexOf(object.toString());
                                open.remove(ii);
                                open2.remove(ii);
                                closed2.add(object.toString());
                                closed.add(object);
                                levels[object.getLevel()]--;
                                NumberMapNode temp = object;
                            }
                        }
                        //Add queue elements predecessors to open if not in open
                        while (c && !queue.isEmpty())
                        {
                            NumberMapNode temp = queue.dequeue();
                            q.enqueue(temp);
                            temp = temp.parent;
                            while (temp.parent != null && !open2.contains(temp.parent.toString()))
                            {
                                open.addFirst(temp.parent);
                                open2.addFirst(temp.parent.toString());
                                closed2.remove(temp.parent.toString());
                                closed.remove(temp.parent);
                                temp = temp.parent;
                            }
                        }
                        if(c)
                            queue = q;
                    }
                }
                else if(queue.isEmpty())
                {
                    return new int[]{0};
                }
            }
        }
        NumberMapNode n = ptr;
        path = new int[ptr.getLevel()];
        String out = "";

        int i = ptr.getLevel() - 1;
        while(i >= 0)
        {
            out = n.toString() + "\n" + out;
            out = String.format("Move %d: %d Moved %s\n", i + 1, n.previousMove, n.getLastMoveVector()) + out;
            path[i--] = n.previousMove;
            n = n.parent;
        }
        try
        {
            IO io = new IO("Game_Tracker.txt");
            io.createFile();
            io.printToFile("Initial State:\n" + n.toString() + "\n");
            io.printToFile(out);
            io.close();
        }
        catch(IOException ioex)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ioex.toString() + "\nPlease contact the developer immediately.", "Error: void help()", JOptionPane.ERROR_MESSAGE, icon);
        }
        return path;
    }
}
