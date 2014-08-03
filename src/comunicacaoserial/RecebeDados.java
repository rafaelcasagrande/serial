
package comunicacaoserial;

import java.io.BufferedReader;
import java.io.OutputStream;



public class RecebeDados {
    
    public static BufferedReader input;
    public static OutputStream output;
       
    public static void main(String[] ag)
    {
        try
        {
            SerialClass obj = new SerialClass();

            input = SerialClass.input;
            
            String inputLine = input.readLine();
            System.out.println(inputLine);
            
            obj.close();
        }
        catch(Exception e)
        {
            
        }
    }    
}
