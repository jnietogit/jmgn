/*******************************************************************************************
 ***                                    WebStream.java                                   ***
 ***                                        v3.0                                         *** 
 ***                                 Created on May 2002                                 ***
 ***                                                                                     ***  
 ***       Client/Server communicaton service for LAN/WAN based on HTTP protocol         ***   
 ***       using Socket and ServerSocket JAVA implementation.                            ***
 ***                                                                                     *** 
 ***                         Developed by Jose Manuel Garcia Nieto                       *** 
 *******************************************************************************************/


import java.util.*;
import java.net.*;
import java.io.*;

public class WebStream {
    
    private Socket socket;
    private ServerSocket ss;  
	private InputStream is;
	private OutputStream os;
    private ObjectOutputStream oos;
    private ObjectInputStream  ois;
    private DataInputStream  in;
    private DataOutputStream out;
    private int port = 80;   

    /* Server constructor */
    public WebStream(int p) {               
        try{
        	port = p;
            ss = new ServerSocket(port);                                  
        }catch(IOException ie){
            ie.printStackTrace();     
        }catch(Exception e){
            e.printStackTrace();     
        }
    }
    
    /* Client Constructor */
    public WebStream(String ad, int p) {
        try{            
        	port = p;
            socket =  new Socket(ad,port); 
        }catch(UnknownHostException uhe){
            uhe.printStackTrace();    
        }catch(IOException ie){
            ie.printStackTrace();     
        }catch(Exception e){
            e.printStackTrace();     
        }
    }       
    
    // Listens for a connection to be made to this WebStream and accepts it.
    // Only for Server WebStream Constructor.
    public void accept(){        
        try{            
            socket = ss.accept();
        }catch(Exception e){
            e.printStackTrace();     
        }        
    }
    
    // Obtain the Input/Output Stream to init the communication system 
    public void init(){
        try{            
            is     =  socket.getInputStream();
            os     =  socket.getOutputStream();
            in     =  new DataInputStream(is);            
            out    =  new DataOutputStream(os);          
            oos    =  new ObjectOutputStream(os);
            ois    =  new ObjectInputStream(is);            
            
        }catch(IOException ie){
            ie.printStackTrace();     
        }catch(Exception e){
            e.printStackTrace();     
        }
    }
    
    
    /**********************************************************************
     ***                  Basic Input/Output Services                   ***
     **********************************************************************/
    public void put(Object obj) throws Exception{
        oos.writeObject(obj);           
    }
    
    public Object get() throws Exception{       
        return (ois.readObject());
    }
    
    // Put in the WebStream the file whose name is passed by parameter
    public int put_file(String f)throws Exception{
        int buffer_len = 0; 
        
        FileInputStream fis = new FileInputStream(f); 
        byte[] buffer_file  = new byte[fis.available()];          
        buffer_len = new Integer(fis.available()).intValue();
            
        out.writeInt(buffer_len);         
        fis.read(buffer_file);
        out.write(buffer_file);
        fis.close();
            
        return buffer_len;                            
             
    }
    
    //Put in the WebStrea an String.
    // "len" is the length of string 
    public int put_string(int len, String s) throws Exception{    
        out.writeInt(len);
        out.writeChars(s); 
        return len;                                
    }
    
    
    // Put in the WebStream an array of bytes 
    // "len" is the length of array
    public int put(int len, byte [] bytebuff) throws Exception {                         
                out.write(bytebuff);
				return (len);
	}
    
    // Put in the WebStream an array of integers
    // "len" is the length of array
    public int put(int len, int [] intbuff) throws Exception {       
            for (int i=0; i<len; i++)
                out.writeInt(intbuff[i]);            
            return len;                                
    }
    
    // Put in the WebStream an array of booleans
    // "len" is the length of array
    public int put(int len, boolean [] booleanbuff) throws Exception {       
            for (int i=0; i<len; i++)
                out.writeBoolean(booleanbuff[i]);            
            return len;                                
    }
    
    // Put in the WebStream an array of floats
    // "len" is the length of array
    public int put(int len, float [] floatbuff) throws Exception {       
            for (int i=0; i<len; i++)
                out.writeFloat(floatbuff[i]);            
            return len;                                
    }
    
    // Put in the WebStream an array of doubles
    // "len" is the length of array
    public int put(int len, double [] doublebuff) throws Exception {                   
            for (int i=0; i<len; i++)
                out.writeDouble(doublebuff[i]);            
            return len;                                 
    }
    
    // Put in the WebStream an array of longs
    // "len" is the length of array
    public int put(int len, long [] longbuff) throws Exception {       
            for (int i=0; i<len; i++)
                out.writeLong(longbuff[i]);            
            return len;                                
    }
    
    // Put in the WebStream an array of shorts
    // "len" is the length of array
    public int put(int len, short [] shortbuff) throws Exception {       
            for (int i=0; i<len; i++)
                out.writeShort(shortbuff[i]);            
            return len;                                
    }
    
    
    
    // Get a file of the WebStream 
    // "f" is the name of the file where it write in
    public int get_file(String f) throws Exception {       
            int              file_length      = in.readInt();            
            byte[]           buffer_file      = new byte[file_length];
            int              buffersize       = in.read(buffer_file);
            FileOutputStream fos              = new FileOutputStream(f); 
            
            fos.write(buffer_file);          
            fos.close();
            
            return file_length;       
    }   
    
    // Get a String of throws WebStream and return it
    public String get_string() throws Exception {      
            String s = "";
            int len = in.readInt();
            for (int i=0; i<len; i++)
                s += in.readChar();            
            return s;       
    }
    
    // Get an array of bytes and put it in "rec_bytebuff"
	// "len" is the length of array
    public int get(int len, byte [] rec_bytebuff) throws Exception{
        in.read(rec_bytebuff); 
		return (len);
	}    
                    
    // Get an array of integers and put it in "rec_intbuff",
    // "len" is the length of array
    public int get(int len, int [] rec_intbuff) throws Exception{
        for (int i=0; i<len; i++) rec_intbuff[i] = in.readInt(); return (len);}
    
    // Get an array of boolenas and put it in "rec_booleanbuff",
    // "len" is the length of array    
    public int get(int len, boolean [] rec_booleanbuff) throws Exception{
        for (int i=0; i<len; i++) rec_booleanbuff[i] = in.readBoolean(); return (len);}
    
    // Get an array of floats and put it in "rec_floatbuff",
    // "len" is the length of array        
    public int get(int len,float [] rec_floatbuff) throws Exception{
        for (int i=0; i<len; i++) rec_floatbuff[i] = in.readFloat(); return (len);}
    
    // Get an array of doubles and put it in "rec_doublebuff",
    // "len" is the length of array        
    public int get(int len,double [] rec_doublebuff) throws Exception{
        for (int i=0; i<len; i++) rec_doublebuff[i] = in.readDouble(); return (len);}
    
    // Get an array of longs and put it in "rec_longbuff",
    // "len" is the length of array        
    public int get(int len, long [] rec_longbuff) throws Exception{
        for (int i=0; i<len; i++) rec_longbuff[i] = in.readLong(); return (len);}
    
    // Get an array of shortss and put it in "rec_shortbuff",
    // "len" is the length of array        
    public int get(int len, short [] rec_shortbuff) throws Exception{
        for (int i=0; i<len; i++) rec_shortbuff[i] = in.readShort(); return (len);}
   
    // Check whether there are awaiting data    
    public boolean probe() throws IOException{
        return ((in.available() > 0) || (ois.available() > 0));
    }
    
    // 
	public int available() throws IOException{
		return ((in.available())+(ois.available()));
	}

    //force sending
    public void flush(){
        try{
            oos.flush();           
			out.flush();
        }catch(IOException ie){
            ie.printStackTrace();     
        }catch(Exception e){
            e.printStackTrace();     
        }
    }
    // close WebStream
    public void finalize(){
        try{
            this.flush();
            oos.close();
            ois.close();
			in.close();
			out.close();
            socket.close();
        }catch(IOException ie){
            ie.printStackTrace();     
        }catch(Exception e){
            e.printStackTrace();     
        }
    }
}