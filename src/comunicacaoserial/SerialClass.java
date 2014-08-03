
package comunicacaoserial;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

public class SerialClass implements SerialPortEventListener{

    //teste
    public SerialPort serialPort;
    
    private static final String PORT_NAMES[] = {
    "/dev/tty.usbserial-A9007UX1", // Mac OS X
    "/dev/ttyUSB0", // Linux
    "COM3", // Windows}
    };
    
    public static BufferedReader input;
    public static OutputStream output;
    
    //Tempo em milegesundos para aguardar enquanto a porta é aberta
    public static final int TIME_OUT = 2000;
    
    //Taxa de transfêrencia de bits por segundo para a porta COM
    public static final int DATA_RATE = 9600;
    
    public void initialize()
    {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        
        //Encontra uma instância da porta serial definida em PORT_NAMES
        while(portEnum.hasMoreElements())
        {
            CommPortIdentifier currentPortId = (CommPortIdentifier) portEnum.nextElement();
            for(String portName : PORT_NAMES)
            {
                if(currentPortId.getName().equals(portName))
                {
                    portId = currentPortId;
                    break;
                }
            }
        }
        if (portId == null)
        {
            System.out.println("Não foi possível encontrar uma porta COM");
            return;
        }
        
        try{
            //abre a porta serial e usa o nome da classe appName
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
            
            //define os parâmetros da porta
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            //abre a transferência
            input = new BufferedReader (new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            char ch = 1;
            output.write(ch);
            
            //adiciona ouvintes
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);           
        }
        catch(Exception ex)
        {
            System.err.println(ex.toString());
        }
    }   
    
    public synchronized void close()
    {
        if(serialPort != null)
        {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    
    public synchronized void serialEvent(SerialPortEvent oEvent)
    {
        if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try{
                String inputLine = input.readLine();
                
                System.out.println(inputLine);
            }
            catch(Exception e)
            {
                //O input não recebeu nehum byte
            }
        }
    }
    
    public static synchronized void writeData(String data)
    {
        System.out.println("Sent: " + data);
        try
        {
            output.write(data.getBytes());
        }
        catch(Exception e)
        {
            System.out.println("Não pode escrever para na porta");
        }
    }
    
    public SerialClass()
    {    
        this.initialize();
        
        Thread t = new Thread(){
            public void run()
            {
                try
                {
                    Thread.sleep(1000);
                    //writeData("1");
                }
                catch(InterruptedException ie)
                {
                    System.out.println(ie.getMessage());
                }
            }
        };
       
        //System.out.println("Iniciado");  
        t.start();
    }
}
