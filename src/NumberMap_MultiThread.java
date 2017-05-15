/**
 * @author Zander Labuschagne 23585137
 * Class to manage Number Nodes(Game Engine)
 * This software was designed to be independent of GUI
 * This class is the UNIX version for NumberMap(Includes MultiThreading)
 */

public class NumberMap_MultiThread extends NumberMap
{
    /**
     * Default Constructor
     */
    public NumberMap_MultiThread()
    {
        super();
    }

    /**
     * Overloaded Constructor
     * @param stateIn The state in which the game should start, presented in matrix format
     * @param winState The state in which the game is won, presented in matrix format
     */
    public NumberMap_MultiThread(int[][] stateIn, int[][] winState)
    {
        super(stateIn, winState);
    }

    /**
     * Saves the current game state if requested
     * Writes the current game state in comma separated value format into a .csv file to be loaded in future
     * Prompts user to enter the directory in which the .csv will be saved
     * @param fileName file name to save game as
     */
    @Override
    public void saveState(String fileName)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                saveStateCode(fileName);
            }
        }).start();
    }

    /**
     * Loads a requested game state from a .csv in comma separated value format
     * Reads the comma separated values from .csv file into current game state
     * Prompts user to enter the directory of the .csv file to load
     * if no state is loaded a randomized state will be generated
     * @param fileName name of file to save game as
     */
    @Override
    public void loadState(String fileName)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                loadStateCode(fileName);
            }
        }).start();
    }
}
