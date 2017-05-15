/**
 * @author Zander Labuschagne 23585137
 * Extended from NumberMap to support AI capabilities
 */

import java.util.LinkedList;

public class NumberMapNode extends NumberMap
{
    private LinkedList<NumberMapNode> next;
    private int sumDistance;
    private int f;
    private int[] validMoves;
    private int level;
    public int previousMove;
    public NumberMapNode parent;

    /**
     * Default Constructor
     */
    public NumberMapNode()
    {
        super();
        next = null;
        f = 0;
        validMoves = null;
        sumDistance = 0;
    }
    /**
     * Overloaded Constructor
     */
    public NumberMapNode(int[][] state, int level, int[][] goalState, NumberMapNode parent)
    {
        super(state, goalState);
        this.parent = parent;
        setValidMoves();
        setLevel(level);
        setF();
        previousMove = 0;
        next = new LinkedList<NumberMapNode>();
    }

    /**
     * Set up an array of valid moves
     */
    public void setValidMoves()
    {
        int[] temp = new int[8];
        int ii = 0;
        for(int i = 1; i <= 8; i++)
        {
            moveTest(i);
            if(getLastMove() == i)
                temp[ii++] = i;
            moveTest(i);
        }
        validMoves = new int[ii];
        for(int iii = 0; iii < ii; iii++)
            validMoves[iii] = temp[iii];
    }

    /**
     * Set the level of node in tree
     * @param level the level
     */
    private void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * Set the sum distance, which is the amount of blocks not in correct position
     */
    private void setSumDistance()
    {
        int[] a = new int[9];
        int[] b = new int[9];
        int iii = 0;
        for(int i = 0; i < getActiveState().length; i++)
            for(int ii : getActiveState()[i])
                a[iii++] = ii;
        iii = 0;
        for(int i = 0; i < getGoalState().length; i++)
            for(int ii : getGoalState()[i])
                b[iii++] = ii;

        for(int i = 0; i < 9; i++)
            if(a[i] != b[i])
                sumDistance++;
        if(indexOf(a, 0) != indexOf(b, 0))
            sumDistance--;
    }

    private int indexOf(int[] a, int b)
    {
        for(int i = 0; i < a.length; i++)
            if(a[i] == b)
                return i;
        return -1;
    }

    /**
     * Calculate f() the heuristic estimate
     */
    public void setF()
    {
        sumDistance = 0;
        setSumDistance();
        f = sumDistance + level;
    }

    /**
     * Set the array of next valid states or children
     */
    public void setNext(NumberMapNode element)
    {
        next.add(element);
    }

    /**
     * Delete all the children of a node
     */
    public void killAllChildren()
    {
        next = new LinkedList<NumberMapNode>();
    }

    /**
     * @return f() the heuristic estimate
     */
    public int getF()
    {
        return f;
    }

    /**
     * @return array of valid moves
     */
    public int[] getValidMoves()
    {
        return validMoves;
    }

    /**
     *
     * @return array of children nodes
     */
    public LinkedList<NumberMapNode> getNext()
    {
        return next;
    }

    /**
     * @return the level of the node
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Calls the correct function that moves the node holding the number
     * Only a node adjacent to the null node can be moved into the null node's position, by swapping this node with the null node
     * @param number value of the node to be moved
     */
    @Override
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
    public void moveTest(int number)
    {
        NumberNode node = getNode(number);

        if(node.getLeft() != null && node.getLeft().isNullNode())
        moveLeftTest(node);
        else if(node.getRight() != null && node.getRight().isNullNode())
        moveRightTest(node);
        else if(node.getUp() != null && node.getUp().isNullNode())
        moveUpTest(node);
        else if(node.getDown() != null && node.getDown().isNullNode())
        moveDownTest(node);
    }

    /**
     * Move the node a block left, by swapping this node with the null node
     * Will only activate if the null node is left adjacent to this node
     * @param node node which is requested to move
     */
    @Override
    protected void moveLeft(NumberNode node)
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

            setLastMoveVector("left");
            previousMove = node.getInfo();
    }
    /**
     * Move the node a block left, by swapping this node with the null node
     * Will only activate if the null node is left adjacent to this node
     * This method is only to test for a left move
     * @param node node which is requested to move
     */
    protected void moveLeftTest(NumberNode node)
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

        setLastMove(node.getInfo());
    }

    /**
     * Move the node a block right, by swapping it with the null node
     * Will only activate if the null node is right adjacent to this node
     * @param node node which is requested to move
     */
    @Override
    protected void moveRight(NumberNode node)
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
            setLastMoveVector("right");
        previousMove = node.getInfo();
        }

    /**
     * Move the node a block right, by swapping this node with the null node
     * Will only activate if the null node is left adjacent to this node
     * This method is only to test for a right move
     * @param node node which is requested to move
     */
    protected void moveRightTest(NumberNode node)
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
        setLastMove(node.getInfo());
    }

    /**
     * Move the node a block up, by swapping it with the null node
     * Will only activate if the null node is upwards adjacent to this node
     * @param node node which is requested to move
     */
    @Override
    protected void moveUp(NumberNode node)
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
            setLastMoveVector("up");
        previousMove = node.getInfo();
        }

    /**
     * Move the node a block up, by swapping this node with the null node
     * Will only activate if the null node is left adjacent to this node
     * This method is only to test for a up move
     * @param node node which is requested to move
     */
    protected void moveUpTest(NumberNode node)
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
        setLastMove(node.getInfo());
    }

    /**
     * Move the node a block down, by swapping it with the null node
     * Will only activate if the null node is downwards adjacent to this node
     * @param node node which is requested to move
     */
    @Override
    protected void moveDown(NumberNode node)
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
            setLastMoveVector("down");
        previousMove = node.getInfo();

        }

    /**
     * Move the node a block down, by swapping this node with the null node
     * Will only activate if the null node is left adjacent to this node
     * This method is only to test for a down move
     * @param node node which is requested to move
     */
    protected void moveDownTest(NumberNode node)
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
        setLastMove(node.getInfo());
    }

    /**
     * Determine if game won
     * @return true if game won
     * play win sound in new thread
     * changed to be used with AI
     */
    @Override
    public boolean gameStatus()
    {
        for(int i = 0; i < getY_axis(); i++)
            for(int ii = 0; ii < getX_axis(); ii++)
                if(getActiveState()[i][ii] != getGoalState()[i][ii])
                    return false;
         return true;
    }
}
