/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIS.Chat.readUTFwriteUTF;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextField;

/**
 *
 * @author Jhon
 */
public class Conn_aServer implements ActionListener {
    private Socket socket; 
    private JTextField tfMensaje;
    private String usuario;
    private DataOutputStream salidaDatos;
    
    public Conn_aServer(Socket socket, JTextField tfMensaje, String usuario) {
        this.socket = socket;
        this.tfMensaje = tfMensaje;
        this.usuario = usuario;
        try {
            this.salidaDatos = new DataOutputStream(this.socket.getOutputStream());            
        } catch (IOException ex) {
            System.out.println("Error al crear el stream de salida : " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("El socket no se creo correctamente. ");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            // Envia el mensaje al SERVIDOR ==================================
            System.out.println("Mensaje Enviado a Servidor (Usuario): "+usuario);
            System.out.println("Mensaje Enviado a Servidor (Mensaje): "+tfMensaje.getText());
            salidaDatos.writeUTF(":"+ usuario + ": " + tfMensaje.getText());
            tfMensaje.setText("");
        } catch (IOException ex) {
            System.out.println("Error al intentar enviar un mensaje: " + ex.getMessage());
        }
    }
}
