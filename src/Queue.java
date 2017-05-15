/*
* @author Zander Labuschagne 23585137
* Queue[front -> -> -> -> -> back]
*/

public class Queue< T >
{
    private Node< T > front;
    private Node< T > back;

    //Default Constructor
    public Queue()
    {
        front = back = null;
    }

    //Empty List
    public boolean isEmpty()
    {
        return front == null;
    }

    public void clear()
    {
        front = null;
    }


    //data = el
    //	    push(T data)
    public void enqueue(T data)
    {
        if(isEmpty())
            front = back = new Node< T >(data);

        else if(front == back)
        {
            front.setNext(new Node< T >(data));
            back = front.getNext();
        }

        else
        {
            back.setNext(new Node< T >(data));
            back = back.getNext();
        }
    }
    //	pop()
    public T dequeue()
    {
        if (isEmpty())
            return null;

        T data = front.getInfo();
        if(front == back)
            front = back = null;
        else
            front = front.getNext();
        return data;
    }
    //firstEl()
    public T peek()
    {
        return front.getInfo();
    }



}
