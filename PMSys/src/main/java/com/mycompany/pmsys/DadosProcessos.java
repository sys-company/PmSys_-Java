
package com.mycompany.pmsys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Alex Gusmão
 */
public class DadosProcessos {
    
    private List<String> nomeProcesso = new ArrayList<>();
    private List<String> tempoDeUso = new ArrayList<>();
    private String[][] dados = new String[10][2];
    
    public DadosProcessos(Integer maquina){
         ConnectURL dadosConexao = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
        
        List<Map<String, Object>> selectProcess = jdbcTemplate.queryForList("select top 10 * from tblInfoProcessos where fkMaquina = ? order by idInfoProcessos desc", maquina);
        
        int i = 0;
        
        for (Map row : selectProcess){
            
            for(int j = 0; j < 2; j++){
                if(j == 1){
                    dados[i][j] = row.get("tempoDeUso").toString();
                }else{
                    dados[i][j] = row.get("nomeProcesso").toString(); 
                }
            }
            
            i++;
        }
    }

    public String[][] getDados() {
        return dados;
    }
    
    
    
}
