/**
 * @author Zander Labuschagne 23585137
 * Class to manage file I/O
 */

//I/O
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class IO //implements Runnable
{
    //Instance Variables
    /**
     * Name of file to be used, including directory/path
     */
    private String fileName;
    /**
     * object of File
     */
    private File file;
    /**
     * object of PrintWriter to write to files
     */
    private PrintWriter writer;

    /**
     * Default Constructor
     */
    public IO() throws FileNotFoundException
    {
        this("");
    }

    /**
     * Overloaded Constructor #1
     * @param fileName name of file to be used
     */
    public IO(String fileName) throws FileNotFoundException
    {
        if(!fileName.equals(""))
        {
            setFileName(fileName);
            setFile(new File(getFileName()));
        }
    }

    //Mutators
    /**
     * set the file name
     * @param fileName name of file
     */
    private void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * set the PrintWriter object
     * @param writer object of PrntWriter
     */
    private void setWriter(PrintWriter writer)
    {
        this.writer = writer;
    }

    /**
     * set the File object
     * @param file file to be used
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    //Accessors
    /**
     * get the file name
     * @return the file name
     */
    private String getFileName()
    {
        return fileName;
    }

    /**
     * get the PrintWriter object
     * @return PrintWriter object
     */
    private PrintWriter getWriter()
    {
        return writer;
    }

    /**
     * get the File object
     * @return the File object
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Create a file and overwrite any existing file with identical file name followed by opening of the file
     */
    public void createFile()
    {
        try
        {
            file = new File(getFileName());
            if (file.exists())
            {
                file.delete();
            }
            setWriter(new PrintWriter(file));
        }
        catch(FileNotFoundException fnfex)
        {
            //Exception handled somewhere else
        }
        catch(Exception ex)
        {
            //Exception handled somewhere else
        }
    }

    public void help()
    {
        File ug;
            try
            {
                if(Desktop.isDesktopSupported())
                {
                    InputStream resource = this.getClass().getResource("Documentation.pdf").openStream();
                    File file = File.createTempFile("Documentation", "pdf");
                    OutputStream out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int len;
                    while((len = resource.read(buffer)) != -1)
                        out.write(buffer, 0, len);
                    ug = file;
                    out.close();

                    Desktop.getDesktop().open(ug);
                    resource.close();
                }
            }
            catch (IOException ioex)
            {
                final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
                JOptionPane.showMessageDialog(null, ioex.toString() + "\nPlease contact the developer immediately.", "Error: void help()", JOptionPane.ERROR_MESSAGE, icon);
            }
            catch (Exception ex)
            {
                final ImageIcon icon = new ImageIcon(this.getClass().getResource("icons/alert-red.png"));
                JOptionPane.showMessageDialog(null, ex.toString() + "\nPlease contact the developer immediately.", "Error: void help()", JOptionPane.ERROR_MESSAGE, icon);
            }

    }

    /**
     * append a text into a file
     * @param text text to append into file
     */
    public void printToFile(String text)
    {
        getWriter().append(text);
    }

    /**
     * Close the file so other processes can use it
     */
    public void close()
    {
        getWriter().close();
    }

    /**
     * Destructor to close the opened file and free memory
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        close();
    }
}
