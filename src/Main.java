package src;
import javax.swing.*;  
import java.lang.Exception;  



class Main
{  
    public static void main(String arg[])  
    {  
        try  
        {   
            //create main frame to add all panels to
            //add all panels to this frame and set visible/invisible when required
            JFrame mainFrame = new JFrame();

            //create a login panel
            JPanel mainPanel = new JPanel();

            //create the form frame
            Login_form form = new Login_form(mainFrame, mainPanel); 
             
        }  


        
        catch(Exception e)  
        {     
            //return any errors
            JOptionPane.showMessageDialog(null, e.getMessage());  
        }  
    }  
}