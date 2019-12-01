/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import log.GerarLog;

/**
 *
 * @author marce
 */
public class Slack {
    
    public Boolean enviarMensagem(String mensagem, String identificador, String colaborador) {
        if (mensagem.isBlank() || identificador.isBlank()) return false;
        try {
            URL url = new URL("https://hooks.slack.com/services/TMNE3H26A/"
                    + "BNSF52HCL/XaTtm8TD5p032D2AehtR1WAr");
            HttpURLConnection connection = 
                    (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            
            connection.setDoOutput(true);
            DataOutputStream post = new DataOutputStream(connection.getOutputStream());
            post.writeBytes(String.format(
                    "{'text':'<@%s>: %s'}",
                    identificador, mensagem
            ));
            post.flush();
            post.close();
            
            if (connection.getResponseCode()==200) {
                GerarLog.escreverLog("Foi enviada a mensagem: '" + mensagem + "' para o colaborador: " + colaborador, "A", 0);
                return true;
            }
            
            
            
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao enviar mensagem para o slack: " + e.getMessage());
            GerarLog.escreverLog("Erro ao enviar mensagem para o slack: " + e.getMessage(), "A", 0);
        }
        
        return false;
    }
}
