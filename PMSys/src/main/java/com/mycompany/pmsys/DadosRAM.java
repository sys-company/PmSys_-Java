
package com.mycompany.pmsys;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
/**
 *
 * @author Henrique Estevam
 */
public class DadosRAM {
    private Double totalRamUsado = 0.0;
    private Double totalRam = 0.0;
    
    public DadosRAM(Integer maquina){
         
        ConnectURL dadosConexao = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
        
        List<Map<String, Object>> selectRAM = jdbcTemplate.queryForList("select top 1 * from tblInfoRAM where fkMaquina = ? order by idInfoRAM desc", maquina);
        for (Map row : selectRAM){
            totalRamUsado = Double.parseDouble(row.get("totalRamUsado").toString());
            totalRam = Double.parseDouble(row.get("totalRam").toString());
        }
    }

    public Double getTotalRamUsado() {
        return totalRamUsado;
    }

    public Double getTotalRam() {
        return totalRam;
    }
    
}
