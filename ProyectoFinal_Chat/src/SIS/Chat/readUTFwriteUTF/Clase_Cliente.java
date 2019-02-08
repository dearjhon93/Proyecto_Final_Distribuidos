/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIS.Chat.readUTFwriteUTF;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Jhon
 */
public class Clase_Cliente extends JFrame{
    
    private final JTextField jtextfield_enviarMensaje;
    private final JScrollPane jscroll_scrollpane1;
    private final JTextArea jtextarea_areaChat;
    private final JLabel jlabel_nombreUsuario;
    
    private Socket socket;
    private int puerto;
    private String host;
    private String usuario;
    
    public Clase_Cliente() {
        super("Chat Universidad de Cuenca");
        
        jlabel_nombreUsuario = new JLabel();
        jlabel_nombreUsuario.setBounds(10, 10, 500, 20);
        jlabel_nombreUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jlabel_nombreUsuario.setHorizontalAlignment(JLabel.CENTER);
        jlabel_nombreUsuario.setVerticalAlignment(JLabel.CENTER);
        
        
        jtextarea_areaChat = new JTextArea();
        jtextarea_areaChat.setEnabled(true);
        jscroll_scrollpane1 = new JScrollPane(jtextarea_areaChat);
        jscroll_scrollpane1.setBounds(10, 40, 500, 300);
        add(jscroll_scrollpane1);
        
        jtextfield_enviarMensaje = new JTextField();
        jtextfield_enviarMensaje.setBounds(10, 350, 500, 20);
        add(jtextfield_enviarMensaje);
        
        
        // Ventana de configuracion inicial
        VentanaConfiguracion vc = new VentanaConfiguracion(this);
        host = vc.getHost();
        puerto = vc.getPuerto();
        usuario = vc.getUsuario();
        
        
        jlabel_nombreUsuario.setText("Bienvenido: "+ usuario);
        add(jlabel_nombreUsuario);
        
        setVisible(true);
        setBounds(0, 0, 540, 440);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        System.out.println("Quieres conectarte a " + host + " en el puerto " + puerto + " con el nombre de ususario: " + usuario + ".");
        
        // Se crea el socket para conectar con el Sevidor del Chat
        try {
            socket = new Socket(host, puerto);
        } catch (UnknownHostException ex) {
            System.err.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
            System.err.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        }
        
        // Accion para el boton enviar
        jtextfield_enviarMensaje.addActionListener(new Conn_aServer(socket, jtextfield_enviarMensaje, usuario));
        
    }
    
    public void recibirMensajesServidor(){
        
        DataInputStream entradaDatos = null;
        String mensaje;
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("El socket no se creo correctamente. ");
        }
        
        // Bucle infinito que recibe mensajes del servidor
        boolean conectado = true;
        while (conectado) {
            try {
                mensaje = entradaDatos.readUTF();
                jtextarea_areaChat.append(mensaje+ System.lineSeparator());
            } catch (IOException ex) {
                System.out.println("Error al leer del stream de entrada: " + ex.getMessage());
                int result = JOptionPane.showConfirmDialog(null, "MENSAJE: \n" + ex.getMessage() + ": Inicie secion de Nuevo", "Alerta!", JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) {
                    System.out.println("\n=====================\n\nERROR: "+ex+"\n=====================\n");
                    this.dispose();
                    Clase_Cliente ver = new Clase_Cliente();
                    ver.recibirMensajesServidor();
                } else {
                    System.out.println("\n=====================\n\nERROR: "+ex+"\n=====================\n");
                    this.dispose();
                }
                conectado = false;
            } catch (NullPointerException ex) {
                System.err.println("El socket no se creo correctamente. ");
                conectado = false;
            }
        }
    }

    public static void main(String[] args) {
        Clase_Cliente ver = new Clase_Cliente();
        ver.recibirMensajesServidor();
    }

}
