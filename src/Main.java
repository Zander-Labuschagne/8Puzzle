/**
 * @author Zander Labuschagne 23585137
 * Main class to be used with Windows Operating Systems
 */

import javax.swing.*;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        JFileChooser fileChooser = new JFileChooser();
        String sInput = "";
        int iInput = 0;
        NumberMap map = new NumberMap();
        System.out.println("#############################################################################################");
        System.out.println("#\tWelcome Microsoft Windows user. To start playing, enter the number you want to move.");
        System.out.println("#\tEnter any of the following at any time, or just continue playing:");
        System.out.println("#\t\t\'about\'\t - Displays About Window with information on this software.");
        System.out.println("#\t\t\'new\'\t - Generates a new random state and restart the game.");
        System.out.println("#\t\t\'load\'\t - Loads a predefined game state from .csv file.");
        System.out.println("#\t\t\'save\'\t - Saves the current game state to .csv file.");
        System.out.println("#\t\t\'solve\'\t - Solves the current state to goal state.");
        System.out.println("#\t\t\'exit\'\t - Exits the game without saving.");
        System.out.println("#\t\t\'help\'\t - Opens help document.");
        System.out.println("#############################################################################################");
        System.out.print("\n\n");
        map.printActiveState();

        try
        {

            while (true)
            {
                //Continue Game
                if (scanner.hasNextInt())
                {
                    iInput = scanner.nextInt();

                    if(iInput == 1)
                    {
                        map.move(1);
                        map.printActiveState();
                    }
                    else if(iInput == 2)
                    {
                        map.move(2);
                        map.printActiveState();
                    }
                    else if(iInput == 3)
                    {
                        map.move(3);
                        map.printActiveState();
                    }
                    else if(iInput == 4)
                    {
                        map.move(4);
                        map.printActiveState();
                    }
                    else if(iInput == 5)
                    {
                        map.move(5);
                        map.printActiveState();
                    }
                    else if(iInput == 6)
                    {
                        map.move(6);
                        map.printActiveState();
                    }
                    else if(iInput == 7)
                    {
                        map.move(7);
                        map.printActiveState();
                    }
                    else if(iInput == 8)
                    {
                        map.move(8);
                        map.printActiveState();
                    }
                    else if(iInput == 9)
                    {
                        map.move(9);
                        map.printActiveState();
                    }
                }
                //Menu
                else
                {
                    sInput = scanner.next();
                    if (sInput.equals("exit"))
                    {
                        map.finalize();
                        System.exit(0);
                    }
                    else if (sInput.equals("about"))
                    {
                        About about = new About();

                        about.pack();
                        about.setLocationRelativeTo(null);
                        about.setAlwaysOnTop(true);
                        about.setVisible(true);
                    }
                    else if (sInput.equals("new"))
                    {
                        map.finalize();
                        map = new NumberMap();
                        map.printActiveState();
                    }
                    else if (sInput.equals("load"))
                    {
                        map.finalize();
                        int result = fileChooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION)
                            map.loadState(fileChooser.getSelectedFile().getPath());
                        map.printActiveState();
                    }
                    else if (sInput.equals("save"))
                    {
                        int result = fileChooser.showSaveDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION)
                            map.saveState(fileChooser.getSelectedFile().getPath());
                        map.printActiveState();
                    }
                    else if (sInput.equals("solve"))
                    {
                        map.pc = true;

                        AI ai = new AI(map.getActiveState(), map.getGoalState());
                        int[] solutionPath = ai.findSolutionPath();
                        if (solutionPath.length == 1 && solutionPath[0] == 0)
                        {
                            final ImageIcon icon = new ImageIcon(Main.class.getResource("icons/info-orange.png"));
                            JOptionPane.showMessageDialog(null, "Oh No! It appears that there is no solution to solve this puzzle", "No Solution Found", JOptionPane.ERROR_MESSAGE, icon);
                        }
                        else
                        {
                            IO io = new IO("Game_Tracker.txt");
                            io.createFile();
                            io.printToFile("Initial State:\n" + map.toString());
                            for(int i = 0; i < solutionPath.length; i++)
                            {
                                map.move(solutionPath[i]);
                                io.printToFile(String.format("\nMove %d: %d Moved %s\n", map.getCountMoves(), solutionPath[i], map.getLastMoveVector()));
                                io.printToFile(map.toString());
                                System.out.println(String.format("Move %d: %d Moved %s", map.getCountMoves(), solutionPath[i], map.getLastMoveVector()));
                                map.printActiveState();
                                map.gameStatus();
                            }
                            io.close();
                        }
                        map.pc = false;
                    }
                    else if (sInput.equals("help"))
                    {
                        IO io = new IO();
                        io.help();
                    }
                    else
                    {
                        final ImageIcon icon = new ImageIcon(Main.class.getResource("icons/error-red.png"));
                        JOptionPane.showMessageDialog(null, "Command or number not recognized.\nPlease enter either a number to continue playing the game or a valid command from the menu.", "Command/Number not Recognized", JOptionPane.ERROR_MESSAGE, icon);
                        System.out.println("#############################################################################################");
                        System.out.println("#\tEnter any of the following at any time, or just continue playing:");
                        System.out.println("#\t\t\'about\'\t - Displays About Window with information on this software.");
                        System.out.println("#\t\t\'new\'\t - Generates a new random state and restart the game.");
                        System.out.println("#\t\t\'load\'\t - Loads a predefined game state from .csv file.");
                        System.out.println("#\t\t\'save\'\t - Saves the current game state to .csv file.");
                        System.out.println("#\t\t\'solve\'\t - Solves the current state to goal state.");
                        System.out.println("#\t\t\'exit\'\t - Exits the game without saving.");
                        System.out.println("#\t\t\'help\'\t - Opens help document.");
                        System.out.println("#############################################################################################");
                        System.out.print("\n\n");
                        map.printActiveState();
                    }
                }
            }
        }
        catch(Exception ex)
        {
            final ImageIcon icon = new ImageIcon(Main.class.getResource("icons/alert-red.png"));
            JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error in main()", JOptionPane.ERROR_MESSAGE, icon);
            ex.printStackTrace();
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }

    }
}