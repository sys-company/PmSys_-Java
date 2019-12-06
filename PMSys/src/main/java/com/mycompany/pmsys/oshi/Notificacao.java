
package com.mycompany.pmsys.oshi;

import com.mycompany.pmsys.ConnectURL;
import com.mycompany.pmsys.Slack;
import java.util.Date;
import java.util.List;
import java.util.Map;
import log.GerarLog;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Alex Gusmão
 */
public class Notificacao {
    
    private static ConnectURL dadosConexao = new ConnectURL();
    private static JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
    private static Date dataCapturada;
    private static String identificador = "";
    private static String nome = "";
    
    private static void buscaIdentificador(String mensagem, Integer maquina){
        String stringSQL = "SELECT nomeFuncionario, identificador from tblFuncionario where fkMaquina = ?";
        
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(stringSQL, maquina);
        
        for(Map row : rows){
            identificador = row.get("identificador").toString();
            nome = row.get("nomeFuncionario").toString();
        }
        
        notificarSlack(mensagem, identificador, nome);
       
    }
    
    public static void notificacaoCPU(Double totalUsado, int idMaquina){
        
        if(totalUsado > 75.00){
            String mensagem = "A taxa de uso da CPU(" + formataValor(totalUsado) + ") está acima de 75%!";
            String queryIdFuncionario = "(select idFuncionario from tblFuncionario where fkMaquina = " + idMaquina + ")";
            String queryIdSquad = "(select fkSquad from tblFuncionario where fkMaquina = " + idMaquina + ")";
            try{
                
                dataCapturada = new Date();
                jdbcTemplate.update("INSERT INTO tblNotificacao values (?, ?, "+ queryIdFuncionario + ", ?, " + queryIdSquad + ")", mensagem, dataCapturada, 3);
                
                buscaIdentificador(mensagem, idMaquina);
                
                GerarLog.escreverLog(mensagem, "B", idMaquina);
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B", idMaquina);
            }
        }
        
    }
    
    public static void notificacaoRAM(Double totalRAM, double totalUsado, int idMaquina){

        String queryIdFuncionario = "(select idFuncionario from tblFuncionario where fkMaquina = " + idMaquina + ")";
        String queryIdSquad = "(select fkSquad from tblFuncionario where fkMaquina = " + idMaquina + ")";
        
        double primeiroQ = (totalRAM * 25) / 100;
        double terceiroQ = (totalRAM * 75) / 100;  
        
        String mensagem = "A taxa de uso de RAM( " + formataValor(totalUsado) + " ) está acima do esperado( " + formataValor(terceiroQ) + " )!";
        //buscaIdentificador(mensagem, idMaquina);
        System.out.println(mensagem);
        if(totalUsado > terceiroQ){
            try{
                dataCapturada = new Date();
                jdbcTemplate.update("INSERT INTO tblNotificacao values (?, ?, "+ queryIdFuncionario + ", ?, " + queryIdSquad + ")", mensagem, dataCapturada, 3);

                
                
                GerarLog.escreverLog(mensagem, "B", idMaquina);
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B", idMaquina);
            }
        }
    }
    
    public static void notificacaoHD(Double espacoTotal, double totalDisponivel, int idMaquina){
        
        String queryIdFuncionario = "(select idFuncionario from tblFuncionario where fkMaquina = " + idMaquina + ")";
        String queryIdSquad = "(select fkSquad from tblFuncionario where fkMaquina = " + idMaquina + ")";
        
        double primeiroQ = (espacoTotal * 25) / 100;
        double terceiroQ = (espacoTotal * 75) / 100;        
        
        String mensagem = "Espaço em HD disponivel( " + formataValor(totalDisponivel) + ") é menor que o mínimo( " + formataValor(espacoTotal - terceiroQ) + ") indicado!";
        
        if(totalDisponivel > terceiroQ){
            try{
                dataCapturada = new Date();
                jdbcTemplate.update("INSERT INTO tblNotificacao values (?, ?, "+ queryIdFuncionario + ", ?, " + queryIdSquad + ")", mensagem, dataCapturada, 3);

                buscaIdentificador(mensagem, idMaquina);
                
                GerarLog.escreverLog(mensagem, "B", idMaquina);
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B", idMaquina);
            }
        }
    }
    
    public static void notificacaoProcesso(Long horas, String processo,int idMaquina){
        String mensagem = "O processo " + processo + " está hà" + horas + "horas em uso!";
        String queryIdFuncionario = "(select idFuncionario from tblFuncionario where fkMaquina = " + idMaquina + ")";
        String queryIdSquad = "(select fkSquad from tblFuncionario where fkMaquina = " + idMaquina + ")";
        
        if(horas > 8){
            try{
                dataCapturada = new Date();
                jdbcTemplate.update("INSERT INTO tblNotificacao values (?, ?, "+ queryIdFuncionario + ", ?, " + queryIdSquad + ")", mensagem, dataCapturada, 3);
                   
                buscaIdentificador(mensagem, idMaquina);
                
                GerarLog.escreverLog(mensagem, "B", idMaquina);
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B", idMaquina);
            }
        }
    }
    
    private static String formataValor(Double v1){
        String aux = String.valueOf(v1);
        String valor1 = aux.substring(0,1);
        String valor2 = aux.substring(1,2);
        return valor1 + "." + valor2;
    }
    
    private static void notificarSlack(String mensagem, String identificador, String nome){
        
        Slack s = new Slack();
        s.enviarMensagem(mensagem, identificador, nome);
        
    }
    
    public static void main(String[] args) {
    //    notificacaoCPU(76.00, 1000);
    //    notificacaoRAM(8000.0, 6680.0, 1000);
    //   notificacaoHD(916.00, 600, 1006);
    //    notificacaoProcesso(Long.parseLong("5"), "The Forest", 1004);
    
    //   System.out.println(buscaIdentificador(1000));
    }
    
}
