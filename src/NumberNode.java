/**
 * @author Zander Labuschagne 23585137
 * Number Node class to be used as nodes in the puzzle game(Game Engine's Pistons)
 */

public class NumberNode
{
    //Instance Variables
    /**
     * integer containing the value of this node
     */
    private int info;
    /**
     * integer specifying the x coordinate of this node(latitude)
     */
    private int x;
    /**
     * integer specifying the y coordinate of this node(longitude)
     */
    private int y;
    /**
     * Reference to the node left adjacent of this node
     */
    private NumberNode left;
    /**
     * Reference to the node right adjacent to this node
     * can be null
     */
    private NumberNode right;
    /**
     * Reference to the node upwards adjacent to this node
     * can be null
     */
    private NumberNode up;
    /**
     * Reference to the node downwards adjacent to this node
     * can be null
     */
    private NumberNode down;

    /**
     * Default COnstructor
     */
    public NumberNode()
    {
        this(0, 0, 0);
    }

    /**
     * Overloaded Constructor #1
     * @param info value of this node
     * @param x x coordinate of this node(latitude)
     * @param y y coordinate of this node(longitude)
     */
    public NumberNode(int info, int x, int y)
    {
        this(info, x, y, null, null, null, null);
    }

    /**
     * Overloaded Constructor #2
     * @param info value of this node
     * @param x x coordinate of this node(latitude)
     * @param y y coordinate of this node(longitude)
     * @param left Reference to the node left adjacent to this node
     * @param right Reference to the node right adjacent of this node
     * @param up Reference to the node upwards adjacent to this node
     * @param down Reference to the node downwards adjacent to this node
     */
    public NumberNode(int info, int x, int y, NumberNode left, NumberNode right, NumberNode up, NumberNode down)
    {
        setInfo(info);
        setX(x);
        setY(y);
        setLeft(left);
        setRight(right);
        setUp(up);
        setDown(down);
    }

    //Mutators
    /**
     * sets the value of this node
     * @param info value of this node
     */
    public void setInfo(int info)
    {
        this.info = info;
    }

    /**
     * sets the x coordinate of this node
     * latitude
     * @param x x coordinate of this node
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * sets the y coordinate of this node
     * longitude
     * @param y y coordinate of this node
     */
    public void setY(int y)
    {
        this.y = y;
    }

    /**
     * sets the reference to the node left adjacent to this node
     * @param left reference to the left adjacent node
     */
    public void setLeft(NumberNode left)
    {
        this.left = left;
    }

    /**
     * sets the reference to the node right adjacent to this node
     * @param right reference to the right adjacent node
     */
    public void setRight(NumberNode right)
    {
        this.right = right;
    }

    /**
     * sets the reference to the node upwards adjacent to this node
     * @param up reference to the upwards adjacent node
     */
    public void setUp(NumberNode up)
    {
        this.up = up;
    }

    /**
     * sets the reference to the node downwards adjacent to this node
     * @param down reference to the downwards adjacent node
     */
    public void setDown(NumberNode down)
    {
        this.down = down;
    }

    //Accessors
    /**
     * get the value of this node
     * @return integer value of this node
     */
    public int getInfo()
    {
        return info;
    }

    /**
     * get the x coordinate of this node
     * latitude
     * @return x coordinate of this node
     */
    public int getX()
    {
        return x;
    }

    /**
     * get the y coordinate of this node
     * longitude
     * @return y coordinate of this node
     */
    public int getY()
    {
        return y;
    }

    /**
     * get the reference to the node left adjacent to this node
     * @return reference to the node left adjacent to this node
     */
    public NumberNode getLeft()
    {
        return left;
    }

    /**
     * get the reference to the node right adjacent to this node
     * @return reference to the node right adjacent to this node
     */
    public NumberNode getRight()
    {
        return right;
    }

    /**
     * get the reference to the node upwards adjacent to this node
     * @return reference to the node upwards adjacent to this node
     */
    public NumberNode getUp()
    {
        return up;
    }

    /**
     * get the reference to the node downwards adjacent to this node
     * @return reference to the node downwards adjacent to this node
     */
    public NumberNode getDown()
    {
        return down;
    }

    /**
     * Determine if this node is the origin node
     * @return true if this node is the origin node
     */
    public boolean isOrigin()
    {
        return (x == 0 && y == 0);
    }

    /**
     * Determine if this node is the null node
     * null node is the node occupying the empty block
     * @return true if this node is the null node
     */
    public boolean isNullNode()
    {
        return getInfo() == 0;
    }
}