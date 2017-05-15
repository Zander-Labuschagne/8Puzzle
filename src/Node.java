/*
* @author Zander Labuschagne 23585137
* Node
*/

public class Node< T >
{
    private T info;
    private Node< T > next;

    public Node()
    {
        this(null,null);
    }
    public Node(T data)
    {
        this(data,null);
    }
    public Node(T data, Node< T > ptr)
    {
        setInfo(data);
        setNext(ptr);
    }

    public void setInfo(T info)
    {
        this.info = info;
    }

    public void setNext(Node< T > next)
    {
        this.next = next;
    }

    public T getInfo()
    {
        return info;
    }

    public Node< T > getNext()
    {
        return next;
    }
}