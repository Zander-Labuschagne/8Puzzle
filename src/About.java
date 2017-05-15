/**
 * @author Zander Labuschagne
 * About Form Class
 */

//GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
//I/O
import java.io.IOException;


public class About extends JDialog
{
    public About() throws IOException
    {
        initComponents();
    }

    /**
     * Overloaded Constructor
     * Generated
     * @param owner
     * @throws IOException
     */
    public About(Frame owner)
    {
        super(owner);
        initComponents();
    }

    /**
     * OK Button closes dialog
     * @param e
     */
    private void okButtonActionPerformed(ActionEvent e)
    {
        dispose();
    }

    private void thisWindowGainedFocus(WindowEvent e) {
        // TODO add your code here
    }

    /**
     * Initialize the GUI
     * Generated
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Zander Labuschagne
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        label3 = new JLabel();
        label2 = new JLabel();
        label6 = new JLabel();
        panel2 = new JPanel();
        label4 = new JLabel();
        label5 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setAlwaysOnTop(true);
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                thisWindowGainedFocus(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("9dlu, 9dlu, 9dlu, 9dlu"));

            // JFormDesigner evaluation mark
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "4*(default, $lcgap), 149dlu, 4*($lcgap, default)",
                    "9*(default, $lgap), default"));

                //---- label1 ----
                label1.setText("8 Puzzle");
                label1.setFont(new Font("Chalkboard", Font.BOLD, 20));
                contentPanel.add(label1, CC.xywh(1, 3, 17, 1, CC.CENTER, CC.DEFAULT));

                //---- label3 ----
                label3.setText("Version 1.1");
                contentPanel.add(label3, CC.xy(9, 7, CC.CENTER, CC.DEFAULT));

                //---- label2 ----
                label2.setText("Developed By:");
                contentPanel.add(label2, CC.xywh(1, 9, 17, 1, CC.CENTER, CC.DEFAULT));

                //---- label6 ----
                label6.setText("Zander Labuschagne");
                contentPanel.add(label6, CC.xywh(1, 11, 17, 1, CC.CENTER, CC.DEFAULT));

                //======== panel2 ========
                {
                    panel2.setLayout(new FormLayout(
                        "73dlu",
                        "default"));
                }
                contentPanel.add(panel2, CC.xy(9, 11));

                //---- label4 ----
                label4.setText("North-West University: Potchefstroom");
                contentPanel.add(label4, CC.xywh(1, 15, 17, 1, CC.CENTER, CC.DEFAULT));

                //---- label5 ----
                label5.setText("E-Mail: 23585137@PROTONMAIL.CH");
                contentPanel.add(label5, CC.xywh(1, 17, 17, 1, CC.CENTER, CC.DEFAULT));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("4dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                    "$glue, $button",
                    "pref"));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, CC.xywh(1, 1, 2, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Zander Labuschagne
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JLabel label3;
    private JLabel label2;
    private JLabel label6;
    private JPanel panel2;
    private JLabel label4;
    private JLabel label5;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
