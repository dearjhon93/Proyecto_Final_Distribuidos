/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIS.Chat.readUTFwriteUTF;

import SIS.Chat.Clase_MostrarMensajes;
import SIS.Chat.Logica.MensajeServidor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jhon
 */
public class Clase_Servidor_Secundario{

    public static int PUERTO_SECUNDARIO = 5050;
    
    public static void main(String[] args) {
        
        int maximoConexiones = 10; // Maximo de conexiones simultaneas
        ServerSocket servidor = null; 
        Socket socket = null;
        MensajesChat mensajes = new MensajesChat();
        
        try {
            // Se crea el serverSocket
            servidor = new ServerSocket(PUERTO_SECUNDARIO, maximoConexiones);
            
            //=================== HILO Escucha de Secundario ===================
            Thread h2 = new Thread(new Hilo_ServidorSecundario());
            h2.start();
            //=================== HILO Escucha de Secundario ===================
            
            // Bucle infinito para esperar conexiones
            while (true) {
                
                System.err.println("INICIANDO el servidor SECUNDARIO...");
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
