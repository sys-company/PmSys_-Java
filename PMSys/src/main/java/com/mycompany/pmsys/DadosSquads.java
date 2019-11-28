package com.mycompany.pmsys;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Alex Gusmão
 */
public class DadosSquads {

    private Integer idSquad;
    private String apelidoSquad;
    private String areaSquad;
    private String fkConta;
    
    
    private final ConnectURL dadosConexao = new ConnectURL();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());

    public DadosSquads(Integer squad) {
        
        List<Map<String, Object>> selectSquad = jdbcTemplate.queryForList("select * from tblSquad where idSquad = ?", squad);
        //System.out.println(selectFuncionario);

        for(Map row : selectSquad){
            idSquad = Integer.valueOf(row.get("idSquad").toString());
            apelidoSquad = row.get("apelidoSquad").toString();
            areaSquad = row.get("areaSquad").toString();
            fkConta = row.get("fkConta").toString();
        }

    }
    
    public Integer getIdSquad() {
        return idSquad;
    }

    public String getApelidoSquad() {
        return apelidoSquad;
    }

    public String getAreaSquad() {
        return areaSquad;
    }

    public String getFkConta() {
        return fkConta;
    }
    
    
}
