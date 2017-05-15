/**
 * @author Zander Labuschagne 23585137
 * Main GUI
 * This GUI interacts with game in backend
 * This GUI is designed to be used with only 8 Tiles
 * This GUI is only compatable with UNIX systems
 */

import javax.swing.Timer;
//GUI
import java.awt.*;
import java.awt.event.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import jdk.nashorn.internal.scripts.JO;
//IO
import java.io.FileNotFoundException;
import java.io.IOException;
//Audio
import javax.sound.sampled.*;
import javax.swing.*;

public class Puzzle extends JFrame
{
    /**
     * Object of the game
     */
    private NumberMap map;
    final Timer timer = new Timer(1000, null);


    /**
     * Default Constructor
     */
    public Puzzle()
    {
        initComponents();
        map = new NumberMap_MultiThread();
        setButtons();
    }

    /**
     * Plays the Click sound
     * Execute when illegal move is attempted
     * Runs in own thread
     */
    public static synchronized void playClick()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream((this.getClass().getResource("sounds/click.wav")));

                    clip.open(inputStream);
                    clip.start();
                    inputStream.close();
                }
                catch(LineUnavailableException luex)
                {
                    final ImageIcon icon = new ImageIcon((this.getClass().getResource("icons/error-red.png")));
                    JOptionPane.showMessageDialog(null, "An error occurred with rapid playback of sound, \n" +
                            "please upgrade you audio processor to avoid this.", "Error: void playClick()", JOptionPane.ERROR_MESSAGE, icon);
                }
                catch (Exception ex)
                {
                    final ImageIcon icon = new ImageIcon((this.getClass().getResource("icons/alert-red.png")));
                    JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void playClick()", JOptionPane.ERROR_MESSAGE, icon);
                }
            }
        }).start();
    }

    /**
     * Play Swipe sound
     * Executed when successful move is made
     * Runs in own thread
     */
    public static synchronized void playSwipe()
    {
        new Thread(new Runnable()
        {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run()
            {
                try
                {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream((this.getClass().getResource("sounds/whoosh.wav")));
                    clip.open(inputStream);
                    clip.start();
                    inputStream.close();
                }
                catch(LineUnavailableException luex)
                {
                    final ImageIcon icon = new ImageIcon((this.getClass().getResource("icons/error-red.png")));
                    JOptionPane.showMessageDialog(null, "An error occurred with rapid playback of sound, \nplease upgrade you audio processor to avoid this.", "Error: void playSwipe()", JOptionPane.ERROR_MESSAGE, icon);
                }
                catch(Exception ex)
                {
                    final ImageIcon icon = new ImageIcon((this.getClass().getResource("icons/alert-red.png")));
                    JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void playSwipe()", JOptionPane.ERROR_MESSAGE, icon);
                }
            }
        }).start();
    }

    /**
     * Sets the new location for moved tile
     * @param btn JButton that's moved
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setLocation(JButton btn, int x, int y)
    {
        int lat, longi;

        if(x == 0)
            lat = 0;
        else if(x == 1)
            lat = 170;
        else if(x == 2)
            lat = 340;
        else
            lat = 0;

        if(y == 0)
            longi = 230;
        else if(y == 1)
            longi = 115;
        else if(y == 2)
            longi = 0;
        else
            longi = 0;

        btn.setSize(170, 115);
        btn.setLocation(lat, longi);
    }

    /**
     * Set the JButtons on their correct positions
     */
    public void setButtons()
    {
        int x = map.getNode(1).getX();
        int y = map.getNode(1).getY();
        setLocation(btn1, x, y);
        btn1.setText("1");

        x = map.getNode(2).getX();
        y = map.getNode(2).getY();
        setLocation(btn2, x, y);
        btn2.setText("2");

        x = map.getNode(3).getX();
        y = map.getNode(3).getY();
        setLocation(btn3, x, y);
        btn3.setText("3");

        x = map.getNode(4).getX();
        y = map.getNode(4).getY();
        setLocation(btn4, x, y);
        btn4.setText("4");

        x = map.getNode(5).getX();
        y = map.getNode(5).getY();
        setLocation(btn5, x, y);
        btn5.setText("5");

        x = map.getNode(6).getX();
        y = map.getNode(6).getY();
        setLocation(btn6, x, y);
        btn6.setText("6");

        x = map.getNode(7).getX();
        y = map.getNode(7).getY();
        setLocation(btn7, x, y);
        btn7.setText("7");

        x = map.getNode(8).getX();
        y = map.getNode(8).getY();
        setLocation(btn8, x, y);
        btn8.setText("8");

        this.requestFocus();
    }

    /**
     * Move a Tile
     * @param btn JButton that is moved
     */
    public void moveButton(JButton btn)
    {
        if(String.format("%d", map.getLastMove()).equals(btn.getText()))
        {
            playSwipe();
            if (map.getLastMoveVector() == "left")
                moveButtonLeft(btn);
            else if (map.getLastMoveVector() == "right")
                moveButtonRight(btn);
            else if (map.getLastMoveVector() == "up")
                moveButtonUp(btn);
            else if (map.getLastMoveVector() == "down")
                moveButtonDown(btn);
            this.setTitle(String.valueOf("8 Puzzle - Moves Made: " + (map.getCountMoves())));
        }
        else
        {
            playClick();
            if(timer.isRunning())
                timer.stop();
        }
    }

    /**
     * Move a Tile to the Left
     * @param btn JButton to be moved
     */
    private void moveButtonLeft(JButton btn)
    {
        final Timer timer = new Timer(1, null);
        int origin = btn.getLocation().x;

        timer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Point loc = btn.getLocation();

                int x = loc.x;
                int y = loc.y;

                btn.setLocation(x -= 1, y);
                if(btn.getLocation().x == origin - 168)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x -= 1, y);
                if(btn.getLocation().x == origin - 168)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x -= 1, y);
                if(btn.getLocation().x == origin - 168)
                {
                    setButtons();
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    /**
     * Move a Tile to the Right
     * @param btn JButton to be moved
     */
    private void moveButtonRight(JButton btn)
    {
        final Timer timer = new Timer(1, null);
        int origin = btn.getLocation().x;
        timer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Point loc = btn.getLocation();

                int x = loc.x;
                int y = loc.y;

                btn.setLocation(x += 1, y);
                if(btn.getLocation().x == origin + 168)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x += 1, y);
                if(btn.getLocation().x == origin + 168)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x += 1, y);
                if(btn.getLocation().x == origin + 168)
                {
                    setButtons();
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    /**
     * Moves a Tile Upwards
     * @param btn JButton to be moved
     */
    private void moveButtonUp(JButton btn)
    {
        final Timer timer = new Timer(1, null);
        int origin = btn.getLocation().y;
        timer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Point loc = btn.getLocation();

                int x = loc.x;
                int y = loc.y;

                btn.setLocation(x, y -= 1);
                if(btn.getLocation().y == origin - 114)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x, y -= 1);
                if(btn.getLocation().y == origin - 114)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x, y -= 1);
                if(btn.getLocation().y == origin - 114)
                {
                    setButtons();
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    /**
     * Moves a Tile Downwards
     * @param btn JButton to be moved
     */
    private void moveButtonDown(JButton btn)
    {
        final Timer timer = new Timer(1, null);
        int origin = btn.getLocation().y;
        timer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Point loc = btn.getLocation();

                int x = loc.x;
                int y = loc.y;

                btn.setLocation(x, y += 1);
                if(btn.getLocation().y == origin + 114)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x, y += 1);
                if(btn.getLocation().y == origin + 114)
                {
                    setButtons();
                    timer.stop();
                }
                btn.setLocation(x, y += 1);
                if(btn.getLocation().y == origin + 114)
                {
                    setButtons();
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    /**
     * New Random Game State Menu Item
     * @param e
     * @throws Throwable
     */
    private void mnuFile_NewRandomStateActionPerformed(ActionEvent e)
    {
        if(timer.isRunning())
            timer.stop();
        map.finalize();
        map = new NumberMap_MultiThread();
        setButtons();
    }

    /**
     * Set Tiles when window is focussed
     * @param e
     */
    private void thisWindowGainedFocus(WindowEvent e)
    {
        setButtons();
    }

    /**
     * Closes the current game state and load a new state from file
     * @param e
     * @throws Throwable
     */
    private void mnuFile_LoadGameStateActionPerformed(ActionEvent e)
    {
        map.finalize();
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION)
        {
            map.loadState(fileChooser.getSelectedFile().getPath());
            setButtons();
        }
        if(timer.isRunning())
            timer.stop();
    }

    /**
     * Saves the current game state to file
     * @param e
     */
    private void mnuFile_SaveGameStateActionPerformed(ActionEvent e)
    {
        int result = fileChooser.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION)
            map.saveState(fileChooser.getSelectedFile().getPath());
    }

    /**
     * Closes the current game state and Exit
     * @param e
     * @throws Throwable
     */
    private void mnuFile_ExitActionPerformed(ActionEvent e)
    {
        map.finalize();
        System.exit(0);
    }

    /**
     * Attempt Move Tile 1
     * @param e
     * @throws InterruptedException
     */
    private void btn1ActionPerformed(ActionEvent e)
    {
        map.move(1);
        moveButton(btn1);
        map.gameStatus();
    }

    /**
     * Attempt Move Tile 2
     * @param e
     */
    private void btn2ActionPerformed(ActionEvent e)
    {
        map.move(2);
        moveButton(btn2);
        map.gameStatus();

    }

    /**
     * Attempt Move Tile 3
     * @param e
     */
    private void btn3ActionPerformed(ActionEvent e)
    {
        map.move(3);
        moveButton(btn3);
        map.gameStatus();

    }

    /**
     * Attempt Move Tile 4
     * @param e
     */
    private void btn4ActionPerformed(ActionEvent e)
    {
        map.move(4);
        moveButton(btn4);
        map.gameStatus();

    }

    /**
     * Attempt Move Tile 5
     * @param e
     */
    private void btn5ActionPerformed(ActionEvent e)
    {
        map.move(5);
        moveButton(btn5);
        map.gameStatus();

    }

    /**
     * Attempt Move Tile 6
     * @param e
     */
    private void btn6ActionPerformed(ActionEvent e)
    {
        map.move(6);
        moveButton(btn6);
        map.gameStatus();

    }

    /**
     * Attempt Move Tile 7
     * @param e
     */
    private void btn7ActionPerformed(ActionEvent e)
    {
        map.move(7);
        moveButton(btn7);
        map.gameStatus();
    }

    /**
     * Attempt Move Tile 8
     * @param e
     */
    private void btn8ActionPerformed(ActionEvent e)
    {
        map.move(8);
        moveButton(btn8);
        map.gameStatus();

    }

    /**
     * Closes current game state and Exit
     * @param e
     * @throws Throwable
     */
    private void thisWindowClosing(WindowEvent e)
    {
        map.finalize();
        System.exit(0);
    }


    /**
     * Opens About Dialog
     * @param e
     * @throws IOException
     */
    private void mnuAboutMouseClicked(MouseEvent e)
    {
        About about = new About(this);
        about.pack();
        about.setLocationRelativeTo(null);
        about.setAlwaysOnTop(true);
        about.setVisible(true);
    }

    private void mnuHelpMouseClicked(MouseEvent e)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    IO io = new IO();
                    io.help();
                }
                catch (Exception ex)
                {
                    final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
                    JOptionPane.showMessageDialog(null, ex.getMessage() + "\nPlease contact the developer immediately.", "Error: Help", JOptionPane.ERROR_MESSAGE, icon);
                }
            }
        }).start();
        if(timer.isRunning())
            timer.stop();
    }

    private void mnuAboutActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void menuItem4ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void menuItem1ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void mnuAI_AnimatedSolveActionPerformed(ActionEvent e)
    {
        map.pc = true;

        AI ai = new AI(map.getActiveState(), map.getGoalState());
        int[] solutionPath = ai.findSolutionPath();
            if (solutionPath.length == 1 && solutionPath[0] == 0)
            {
                final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/info-orange.png"));
                JOptionPane.showMessageDialog(null, "Oh No! It appears that there is no solution to solve this puzzle", "No Solution Found", JOptionPane.ERROR_MESSAGE, icon);
            } else
            {
                final int[] i = {0};
                timer.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        mnuAI_AnimatedSolve.setEnabled(false);
                        mnuAI_QuickSolve.setEnabled(false);
                        if (solutionPath[i[0]] == 1)
                            btn1.doClick();

                        else if (solutionPath[i[0]] == 2)
                            btn2.doClick();

                        else if (solutionPath[i[0]] == 3)
                            btn3.doClick();

                        else if (solutionPath[i[0]] == 4)
                            btn4.doClick();

                        else if (solutionPath[i[0]] == 5)
                            btn5.doClick();

                        else if (solutionPath[i[0]] == 6)
                            btn6.doClick();

                        else if (solutionPath[i[0]] == 7)
                            btn7.doClick();

                        else if (solutionPath[i[0]] == 8)
                            btn8.doClick();

                        map.pc = true;

                        if (++i[0] == solutionPath.length)
                        {
                            mnuAI_AnimatedSolve.setEnabled(true);
                            mnuAI_QuickSolve.setEnabled(true);
                            map.pc = false;
                            timer.stop();
                        }

                    }
                });
                timer.start();
            }

        map.pc = false;
        mnuAI_AnimatedSolve.setEnabled(true);
        mnuAI_QuickSolve.setEnabled(true);
    }

    private void mnuAI_QuickSolveActionPerformed(ActionEvent e)
    {
        map.pc = true;
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        btn5.setEnabled(false);
        btn6.setEnabled(false);
        btn7.setEnabled(false);
        btn8.setEnabled(false);


        AI ai = new AI(map.getActiveState().clone(), map.getGoalState().clone());
        int[] solutionPath = ai.findSolutionPath();
        if(solutionPath.length == 1 && solutionPath[0] == 0)
        {
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/info-orange.png"));
            JOptionPane.showMessageDialog(null, "Oh No! It appears that there is no solution to solve this puzzle", "No Solution Found", JOptionPane.ERROR_MESSAGE, icon);
        }
        else
        {
            for (int i = 0; i < solutionPath.length; i++)
                map.move(solutionPath[i]);
            setButtons();
            this.setTitle(String.valueOf("8 Puzzle - Moves Made: " + (map.getCountMoves())));
            map.gameStatus();

        }
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        btn4.setEnabled(true);
        btn5.setEnabled(true);
        btn6.setEnabled(true);
        btn7.setEnabled(true);
        btn8.setEnabled(true);
        map.pc = false;
    }

    private void thisKeyTyped(KeyEvent e)
    {
        if(e.getKeyChar() == 'n')
            mnuFile_NewRandomState.doClick();
        else if(e.getKeyChar() == 'a')
            mnuAI_AnimatedSolve.doClick();
        else if(e.getKeyChar() == 'q')
            mnuAI_QuickSolve.doClick();
        else if(e.getKeyChar() == '1')
            btn1.doClick();
        else if(e.getKeyChar() == '2')
            btn2.doClick();
        else if(e.getKeyChar() == '3')
            btn3.doClick();
        else if(e.getKeyChar() == '4')
            btn4.doClick();
        else if(e.getKeyChar() == '5')
            btn5.doClick();
        else if(e.getKeyChar() == '6')
            btn6.doClick();
        else if(e.getKeyChar() == '7')
            btn7.doClick();
        else if(e.getKeyChar() == '8')
            btn8.doClick();


    }

    /**
     * Initialize GUI
     * Generated
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Zander Labuschagne
        menuBar = new JMenuBar();
        mnuAbout = new JMenu();
        mnuFile = new JMenu();
        mnuFile_NewRandomState = new JMenuItem();
        mnuFile_LoadGameState = new JMenuItem();
        mnuFile_SaveGameState = new JMenuItem();
        separator1 = new JSeparator();
        mnuFile_Exit = new JMenuItem();
        mnuAI = new JMenu();
        mnuAI_AnimatedSolve = new JMenuItem();
        mnuAI_QuickSolve = new JMenuItem();
        mnuHelp = new JMenu();
        btn6 = new JButton();
        btn7 = new JButton();
        btn8 = new JButton();
        btn3 = new JButton();
        btn4 = new JButton();
        btn5 = new JButton();
        btn1 = new JButton();
        btn2 = new JButton();
        fileChooser = new JFileChooser();

        //======== this ========
        setTitle("8 Puzzle");
        setIconImage(new ImageIcon("/Users/ZanderMac/Downloads/WaterPlain0017_3_M.jpg").getImage());
        setResizable(false);
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                thisWindowGainedFocus(e);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                thisKeyTyped(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "88dlu, $lcgap, 85dlu, $lcgap, 92dlu",
            "70dlu, $lgap, 65dlu, $lgap, 70dlu"));

        //======== menuBar ========
        {

            //======== mnuAbout ========
            {
                mnuAbout.setText("About");
                mnuAbout.addActionListener(e -> {
			mnuAboutActionPerformed(e);
			mnuAboutActionPerformed(e);
			mnuAboutActionPerformed(e);
		});
                mnuAbout.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mnuAboutMouseClicked(e);
                    }
                });
            }
            menuBar.add(mnuAbout);

            //======== mnuFile ========
            {
                mnuFile.setText("Game");

                //---- mnuFile_NewRandomState ----
                mnuFile_NewRandomState.setText("New Random State");
                mnuFile_NewRandomState.addActionListener(e -> {
			menuItem4ActionPerformed(e);
			mnuFile_NewRandomStateActionPerformed(e);
		});
                mnuFile.add(mnuFile_NewRandomState);

                //---- mnuFile_LoadGameState ----
                mnuFile_LoadGameState.setText("Load Game State");
                mnuFile_LoadGameState.addActionListener(e -> {
			menuItem1ActionPerformed(e);
			mnuFile_LoadGameStateActionPerformed(e);
		});
                mnuFile.add(mnuFile_LoadGameState);

                //---- mnuFile_SaveGameState ----
                mnuFile_SaveGameState.setText("Save Game State");
                mnuFile_SaveGameState.addActionListener(e -> mnuFile_SaveGameStateActionPerformed(e));
                mnuFile.add(mnuFile_SaveGameState);
                mnuFile.add(separator1);

                //---- mnuFile_Exit ----
                mnuFile_Exit.setText("Exit");
                mnuFile_Exit.addActionListener(e -> mnuFile_ExitActionPerformed(e));
                mnuFile.add(mnuFile_Exit);
            }
            menuBar.add(mnuFile);

            //======== mnuAI ========
            {
                mnuAI.setText("AI");

                //---- mnuAI_AnimatedSolve ----
                mnuAI_AnimatedSolve.setText("Animated Solve");
                mnuAI_AnimatedSolve.addActionListener(e -> mnuAI_AnimatedSolveActionPerformed(e));
                mnuAI.add(mnuAI_AnimatedSolve);

                //---- mnuAI_QuickSolve ----
                mnuAI_QuickSolve.setText("Quick Solve");
                mnuAI_QuickSolve.addActionListener(e -> {
			mnuAI_QuickSolveActionPerformed(e);
			//mnuAI_QuickSolveActionPerformed(e);
		});
                mnuAI.add(mnuAI_QuickSolve);
            }
            menuBar.add(mnuAI);

            //======== mnuHelp ========
            {
                mnuHelp.setText("Help");
                mnuHelp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mnuHelpMouseClicked(e);
                       // mnuHelpMouseClicked(e);
                    }
                });
            }
            menuBar.add(mnuHelp);
        }
        setJMenuBar(menuBar);

        //---- btn6 ----
        btn6.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn6.setDefaultCapable(false);
        btn6.addActionListener(e -> btn6ActionPerformed(e));
        contentPane.add(btn6, CC.xy(1, 1, CC.FILL, CC.FILL));

        //---- btn7 ----
        btn7.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn7.setDefaultCapable(false);
        btn7.addActionListener(e -> btn7ActionPerformed(e));
        contentPane.add(btn7, CC.xy(3, 1, CC.FILL, CC.FILL));

        //---- btn8 ----
        btn8.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn8.setDefaultCapable(false);
        btn8.addActionListener(e -> btn8ActionPerformed(e));
        contentPane.add(btn8, CC.xy(5, 1, CC.FILL, CC.FILL));

        //---- btn3 ----
        btn3.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn3.setDefaultCapable(false);
        btn3.addActionListener(e -> btn3ActionPerformed(e));
        contentPane.add(btn3, CC.xy(1, 3, CC.FILL, CC.FILL));

        //---- btn4 ----
        btn4.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn4.setDefaultCapable(false);
        btn4.addActionListener(e -> btn4ActionPerformed(e));
        contentPane.add(btn4, CC.xy(3, 3, CC.FILL, CC.FILL));

        //---- btn5 ----
        btn5.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn5.setForeground(Color.black);
        btn5.setBackground(new Color(0, 204, 255));
        btn5.setOpaque(false);
        btn5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn5.setDefaultCapable(false);
        btn5.addActionListener(e -> btn5ActionPerformed(e));
        contentPane.add(btn5, CC.xy(5, 3, CC.FILL, CC.FILL));

        //---- btn1 ----
        btn1.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn1.setDefaultCapable(false);
        btn1.addActionListener(e -> btn1ActionPerformed(e));
        contentPane.add(btn1, CC.xy(1, 5, CC.FILL, CC.FILL));

        //---- btn2 ----
        btn2.setFont(new Font("Chalkduster", Font.PLAIN, 64));
        btn2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn2.setDefaultCapable(false);
        btn2.addActionListener(e -> btn2ActionPerformed(e));
        contentPane.add(btn2, CC.xy(3, 5, CC.FILL, CC.FILL));
        pack();
        setLocationRelativeTo(getOwner());

        //---- fileChooser ----
        fileChooser.setCurrentDirectory(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Zander Labuschagne
    private JMenuBar menuBar;
    private JMenu mnuAbout;
    private JMenu mnuFile;
    private JMenuItem mnuFile_NewRandomState;
    private JMenuItem mnuFile_LoadGameState;
    private JMenuItem mnuFile_SaveGameState;
    private JSeparator separator1;
    private JMenuItem mnuFile_Exit;
    private JMenu mnuAI;
    private JMenuItem mnuAI_AnimatedSolve;
    private JMenuItem mnuAI_QuickSolve;
    private JMenu mnuHelp;
    private JButton btn6;
    private JButton btn7;
    private JButton btn8;
    private JButton btn3;
    private JButton btn4;
    private JButton btn5;
    private JButton btn1;
    private JButton btn2;
    private JFileChooser fileChooser;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    /**
     * Main Method
     * @param args Arguments loaded from Command
     */
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel") ;
                } catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                } catch (InstantiationException e)
                {
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e)
                {
                    e.printStackTrace();
                }
                Puzzle GUI = new Puzzle();
                GUI.setSize(new Dimension(510, 390));
                GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                GUI.setVisible(true);
            }
        });

    }
}
