
package com.mycompany.pmsys;

import java.util.Map;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Alex Gusmão
 */
public class DadosHD {
    
    private Double totalEspaco = 0.0;
    private Double espacoTotalDispoivel = 0.0;
    
    public DadosHD(Integer maquina){
        
        ConnectURL dadosConexao = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
        
        List<Map<String, Object>> selectHD = jdbcTemplate.queryForList("select top 1 * from tblInfoHD where fkMaquina = ? order by idInfoHD desc", maquina);
        
        for (Map row : selectHD){
            totalEspaco = Double.parseDouble(row.get("espacoTotal").toString());
            espacoTotalDispoivel = Double.parseDouble(row.get("espacoTotalDisponivel").toString());
        }
        
        
    }

    public Double getTotalEspaco() {
        return totalEspaco;
    }

    public Double getEspacoTotalDispoivel() {
        return espacoTotalDispoivel;
    }
    
    
}
