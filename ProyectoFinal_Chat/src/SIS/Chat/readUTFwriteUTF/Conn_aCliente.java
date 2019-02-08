/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIS.Chat.readUTFwriteUTF;

import SIS.Chat.Clase_MostrarMensajes;
import SIS.Chat.Logica.MensajeServidor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observer;

/**
 *
 * @author Jhon
 */
public class Conn_aCliente extends Thread implements Observer{
    private Socket socket; 
    private MensajesChat mensajes;
    
    public static Clase_MostrarMensajes mostrar = new Clase_MostrarMensajes();
    
    private DataInputStream entradaDatos;
    private DataOutputStream salidaDatos;
    
    public Conn_aCliente (Socket socket, MensajesChat mensajes){
        this.socket = socket;
        this.mensajes = mensajes;
        
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
            
        } catch (IOException ex) {
            System.err.println("Error al crear los stream de entrada y salida : " + ex.getMessage());
        }
    }
    
    @Override
    public void run(){
        String mensajeRecibido;
        boolean conectado = true;
        // Se apunta a la lista de observadores de mensajes
        mensajes.addObserver(this);
        
        MensajeServidor objMensajeUnicast = null;
        
        while (conectado) {
            
            try {
                
                //Condicion para el Server PRINCIPAL
                if (socket.getLocalPort() == 6868) {
                    
                    System.out.println("PRINCIPAL");
                    System.out.println("SOCKET: " + socket.getLocalPort());
                    // Lee un mensaje enviado por el cliente
                    mensajeRecibido = entradaDatos.readUTF();
                    // Pone el mensaje recibido en mensajes para que se notifique 
                    // a sus observadores que hay un nuevo mensaje.

                    //=============== Uso de Multicast a Servidores ======================
                    //=============== Uso de Multicast a Servidores ======================
                    
                    mensajeRecibido = mensajeRecibido.substring(mensajeRecibido.indexOf(":") + 1, mensajeRecibido.length());
                    String usuario = mensajeRecibido.substring(0, mensajeRecibido.indexOf(":"));
                    System.out.println("Usuario: " + usuario);

                    String mensaje = mensajeRecibido.substring(mensajeRecibido.indexOf(":") + 2, mensajeRecibido.length());
                    System.out.println("Mensaje: " + mensaje);
                    
                    //Paso 1
                    //Recibe Unicast===============================================
                    objMensajeUnicast = new MensajeServidor(Hilo_ServidorPrimario.contadorClaveMensajes, usuario, mensaje);
//                  mostrar.mostrarMensajeUnicast(mostrar.toBytes(objMensaje));

                    mostrar.guardarObjetoenMap(Hilo_ServidorPrimario.mapaMensajePrincipal, objMensajeUnicast);
//                  mostrar.mostrarMapChat(mapaMensaje);

                    //Paso 2
                    //Envia Multicast===============================================
                    Hilo_ServidorPrimario.enviarMensajeMulticast(mostrar.toBytes(objMensajeUnicast));
                    
                    
                    //=============== Uso de Multicast a Servidores ======================
                    //=============== Uso de Multicast a Servidores ======================
                    
                    System.out.println("Mensaje recibido : " + mensajeRecibido + " *** de cliente con la IP " + socket.getInetAddress().getHostName());

                    mensajes.setMensaje(mensajeRecibido);
                }
                
                //Condicion para el Server SECUNDARIO
                if (socket.getLocalPort() == 5050) {
                    System.out.println("SECUNDARIO");
                    System.out.println("SOCKET: " + socket.getLocalPort());
                    // Lee un mensaje enviado por el cliente
                    mensajeRecibido = entradaDatos.readUTF();
                    // Pone el mensaje recibido en mensajes para que se notifique 
                    // a sus observadores que hay un nuevo mensaje.

                    
                    //=============== Uso de Multicast a Servidores ======================
                    //=============== Uso de Multicast a Servidores ======================
                    
                    
                    
                    
                    //=============== Uso de Multicast a Servidores ======================
                    //=============== Uso de Multicast a Servidores ======================
                    
                    
                    System.out.println("Mensaje recibido : " + mensajeRecibido + " *** de cliente con la IP " + socket.getInetAddress().getHostName());

                    mensajes.setMensaje(mensajeRecibido);
                }
                


                
                
//                System.out.println("Mensaje recibido : " + mensajeRecibido + " *** de cliente con la IP " + socket.getInetAddress().getHostName());
//                mensajes.setMensaje(mensajeRecibido);

            } catch (IOException ex) {
                System.err.println("Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.");
                conectado = false; 
                // Si se ha producido un error al recibir datos del cliente se cierra la conexion con el.
                try {
                    entradaDatos.close();
                    salidaDatos.close();
                } catch (IOException ex2) {
                    System.out.println("Error al cerrar los stream de entrada y salida :" + ex2.getMessage());
                }
            }
        }
        
        
    }
    
    
    

    @Override
    public void update(java.util.Observable o, Object o1) {
        try {
            Hilo_ServidorPrimario.contadorClaveMensajes++;
            // Envia el mensaje al CLIENTE ==================================
            salidaDatos.writeUTF(o1.toString());
        } catch (IOException ex) {
            System.err.println("Error al enviar mensaje al cliente (" + ex.getMessage() + ").");
        }
    }


}
