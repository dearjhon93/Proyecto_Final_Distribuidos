/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIS.Chat.readUTFwriteUTF;

import SIS.Chat.*;
import SIS.Chat.Logica.MensajeServidor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jhon
 */
public class Hilo_ServidorPrimario extends Thread{
    
    public static Map<Object, ArrayList<Object>> mapaMensajePrincipal = new HashMap<Object, ArrayList<Object>>();
    public static Clase_MostrarMensajes mostrar = new Clase_MostrarMensajes();
    
    public static int contadorClaveMensajes = 0;
    
    @Override
    public void run(){
        System.err.println("Escucha Multicast PRINCIPAL...");
        
        MensajeServidor objMensajeMulticast = null;
        while (true) {  
            
            try {
                
                //Paso 5
                //Recibir Multicast===============================================
                objMensajeMulticast = (MensajeServidor) mostrar.toObject(recibirMensajeMulticast());
                mostrar.guardarObjetoenMap(mapaMensajePrincipal, objMensajeMulticast);
//                mostrar.mostrarMapChat(mapaMensaje);
            
                
                
            } catch (IOException ex) {
                System.out.println("ERROR: "+ex.getMessage());
            }
        }
    }
    
    public static void enviarMensajeMulticast(byte[] aux_objMensajeByte) throws IOException{
        
        MulticastSocket multiEnvia = new MulticastSocket();
        InetAddress group = InetAddress.getByName("231.0.0.1");
        byte[] vacio = new byte[0];
        
        DatagramPacket dgp = new DatagramPacket(vacio, 0, group, 10000);
        
        MensajeServidor mensajeEnvio = (MensajeServidor)mostrar.toObject(aux_objMensajeByte);

        //Objeto
//        System.out.print("\nMensaje ENVIADO de Servidor (Object): "+ mensajeEnvio);
        //Byte
//        System.out.println("\nMensaje ENVIADO de Servidor (Byte []): " + aux_objMensajeByte);

        byte[] buffer = aux_objMensajeByte;

        dgp.setData(buffer);
        dgp.setLength(buffer.length);

        multiEnvia.send(dgp);
        multiEnvia.close();
        
    }
    
    
    public static byte[] recibirMensajeMulticast() throws IOException{
        
        MulticastSocket multiRecibe = new MulticastSocket(10000);
        
        InetAddress group = InetAddress.getByName("231.0.0.1");
        
        multiRecibe.joinGroup(group);

        byte[] buffer = new byte[256];
        DatagramPacket dgp = new DatagramPacket(buffer, buffer.length);
        multiRecibe.receive(dgp);

        byte[] bufferMensaje = new byte[dgp.getLength()];
        System.arraycopy(dgp.getData(), 0, bufferMensaje, 0, dgp.getLength());

        //Byte
        System.out.println("\nMensaje RECIBIDO de Servidor (Byte []): " + bufferMensaje);
        
        multiRecibe.leaveGroup(group);
        multiRecibe.close();
        
        return bufferMensaje;
    }
    
    
}
