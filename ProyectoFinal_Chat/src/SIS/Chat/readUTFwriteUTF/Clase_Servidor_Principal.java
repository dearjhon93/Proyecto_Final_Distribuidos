/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIS.Chat.readUTFwriteUTF;

import SIS.Chat.Clase_MostrarMensajes;
import SIS.Chat.Logica.MensajeServidor;
import SIS.Chat._TEST_addShutdown;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jhon
 */
public class Clase_Servidor_Principal{

    public static int PUERTO_PRINCIPAL = 6868;
    
    public static void main(String[] args) {
        
        int maximoConexiones = 10; // Maximo de conexiones simultaneas
        ServerSocket servidor = null; 
        Socket socket = null;
        MensajesChat mensajes = new MensajesChat();
        
        
        try {
            // Se crea el serverSocket
            servidor = new ServerSocket(PUERTO_PRINCIPAL, maximoConexiones);
            
            //=================== HILO Escucha de Principal ===================
            Thread h2 = new Thread(new Hilo_ServidorPrimario());
            h2.start();
            //=================== HILO Escucha de Principal ===================
            
            // Bucle infinito para esperar conexiones
            while (true) {
                
                System.err.println("INICIANDO el servidor PRINCIPAL...");
                socket = servidor.accept();
                System.out.println("Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");
                
                Thread cc = new Conn_aCliente(socket, mensajes);
                cc.start();
                
            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            try {
                socket.close();
                servidor.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar el servidor: " + ex.getMessage());
            }
        }
    }
    
}
