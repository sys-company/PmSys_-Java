
package com.mycompany.pmsys;


import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
/**
 *
 * @author Aluno
 */
public class DadosCPU {
    
    private String nomeCpu = "";
    private Double byUser = 0.0;
    private Double bySystem = 0.0;
    private Double totalUso = 0.0;
    
    
    public DadosCPU(Integer maquina){
        
        ConnectURL dadosConexao = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
        
        List<Map<String, Object>> selectCPU = jdbcTemplate.queryForList("select top 1 * from tblInfoCPU where fkMaquina = ? order by idInfoCPU desc", maquina);
        
        for (Map row: selectCPU){
            nomeCpu = row.get("nomeCPU").toString();
            byUser = Double.parseDouble(row.get("byUser").toString());
            bySystem = Double.parseDouble(row.get("bySystem").toString());
            totalUso = Double.parseDouble(row.get("totalUso").toString());
        }
      
    }

    public String getNomeCpu() {
        return nomeCpu;
    }

    public Double getByUser() {
        return byUser;
    }

    public Double getBySystem() {
        return bySystem;
    }

    public Double getTotalUso() {
        return totalUso;
    }
}