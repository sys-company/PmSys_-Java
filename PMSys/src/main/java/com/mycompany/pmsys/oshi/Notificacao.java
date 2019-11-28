
package com.mycompany.pmsys.oshi;

import com.mycompany.pmsys.ConnectURL;
import java.util.Date;
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
    
    public static void notificacaoCPU(Double totalUsado, int idMaquina){
        
        if(totalUsado > 75.00){
            String mensagem = "A taxa de uso da CPU(" + totalUsado + ") está acima de 75%!";
            String queryIdFuncionario = "(select idFuncionario from tblFuncionario where fkMaquina = " + idMaquina + ")";
            String queryIdSquad = "(select fkSquad from tblFuncionario where fkMaquina = " + idMaquina + ")";
            try{
                
                dataCapturada = new Date();
                jdbcTemplate.update("INSERT INTO tblNotificacao values (?, ?, "+ queryIdFuncionario + ", ?, " + queryIdSquad + ")", mensagem, dataCapturada, 3);
                
                GerarLog.escreverLog(mensagem, "B");
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B");
            }
        }
        
    }
    
    public static void notificacaoRAM(Double totalRAM, double totalUsado, int idMaquina){

        String queryIdFuncionario = "(select idFuncionario from tblFuncionario where fkMaquina = " + idMaquina + ")";
        String queryIdSquad = "(select fkSquad from tblFuncionario where fkMaquina = " + idMaquina + ")";
        
        double primeiroQ = (totalRAM * 25) / 100;
        double terceiroQ = (totalRAM * 75) / 100;        

        String mensagem = "A taxa de uso de RAM( " + totalUsado + " ) está acima do esperado( " + terceiroQ + " )!";
        
        if(totalUsado > terceiroQ){
            try{
                dataCapturada = new Date();
                jdbcTemplate.update("INSERT INTO tblNotificacao values (?, ?, "+ queryIdFuncionario + ", ?, " + queryIdSquad + ")", mensagem, dataCapturada, 3);

                GerarLog.escreverLog(mensagem, "B");
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B");
            }
        }
    }
    
    public static void notificacaoHD(Double espacoTotal, double totalDisponivel, int idMaquina){
        
        String queryIdFuncionario = "(select idFuncionario from tblFuncionario where fkMaquina = " + idMaquina + ")";
        String queryIdSquad = "(select fkSquad from tblFuncionario where fkMaquina = " + idMaquina + ")";
        
        double primeiroQ = (espacoTotal * 25) / 100;
        double terceiroQ = (espacoTotal * 75) / 100;        
        
        String mensagem = "Espaço em HD disponivel( " + totalDisponivel + ") é menor que o mínimo( " + (espacoTotal - terceiroQ) + ") indicado!";
        if(totalDisponivel > terceiroQ){
            try{
                dataCapturada = new Date();
                jdbcTemplate.update("INSERT INTO tblNotificacao values (?, ?, "+ queryIdFuncionario + ", ?, " + queryIdSquad + ")", mensagem, dataCapturada, 3);

                GerarLog.escreverLog(mensagem, "B");
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B");
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

                GerarLog.escreverLog(mensagem, "B");
            }catch(Exception ex){
                GerarLog.escreverLog("Erro ao gerar notificação da CPU", "B");
            }
        }
    }
    
    
    
    public static void main(String[] args) {
    //    notificacaoCPU(76.00, 1000);
    //    notificacaoRAM(900.00, 668.00, 1001);
    //    notificacaoHD(916.00, 600, 1006);
    //    notificacaoProcesso(Long.parseLong("5"), "The Forest", 1004);
    }
    
}
