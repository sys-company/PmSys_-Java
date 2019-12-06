
package com.mycompany.pmsys;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;
import log.GerarLog;

/**
 *
 * @author marcelo
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
            Writer post = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            post.write(String.format(
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
