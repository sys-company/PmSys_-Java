
package log;

import com.mycompany.pmsys.ConnectURL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Alex Gusmão
 */
public class GerarLog {
    
    private static void verificaExistencia(File arquivo){
        File diretorio = new File("C:/temp");
        boolean drExiste = diretorio.exists();
        boolean existe = arquivo.exists();
        
        try{
            if(!drExiste){
                diretorio.mkdir();
            }
            
            if(!existe){
                arquivo.createNewFile();
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Erro ao criar arquivo!\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public static void escreverLog(String mensagem, String opcao, Integer idMaquina){
        try{
            File arquivo = new File("");
            
            switch(opcao){
                case "A":
                    arquivo = new File("C:/temp/app_logs.txt");
                    break;
                
                case "B":
                    arquivo = new File("C:/temp/logs_insercao.txt");
                    break;
            }
            
            
            Format formatador = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dt = new Date();
            String dataLog = formatador.format(dt);
            
            verificaExistencia(arquivo);
            
            insereBanco(mensagem, idMaquina, dataLog);
            
            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter writer = new BufferedWriter(fw);
            
            writer.write("Log gerado em: " + dataLog + " | " + mensagem);
            writer.newLine();
            
            writer.close();
            fw.close();
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Erro ao criar arquivo!\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private static void insereBanco(String mensagem, Integer idMaquina, String dataLog){
        ConnectURL dadosConexao = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
        
        try{
            jdbcTemplate.update("INSERT INTO tblOshiLogs values (?, ?, ?)", mensagem, dataLog, idMaquina);
            
        }
        catch (Exception e){
            
        }
    }
    
}
