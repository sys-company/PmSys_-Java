/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys.oshi;

import com.mycompany.pmsys.ConnectURL;
import java.util.Date;
import log.GerarLog;
import org.springframework.jdbc.core.JdbcTemplate;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

/**
 *
 * @author Alex
 */
public class DadosHD {
    
    //Variaveis de HD
    private double espacoTotal = 0;
    private double espacoUsavel = 0;
    private Date dataHora;
    private int idMaquina;
    
    public DadosHD(int i){
        this.idMaquina = i;
    }
    
    private final SystemInfo si = new SystemInfo();
    OperatingSystem os = si.getOperatingSystem();
    FileSystem fs = os.getFileSystem();
    
    private void usoHD(){        
        OSFileStore[] teste = fs.getFileStores();
        
        for (OSFileStore teste1 : teste) {
            espacoTotal += teste1.getTotalSpace();
            espacoUsavel += teste1.getUsableSpace();
        }
        
        espacoTotal = (((espacoTotal/1024)/1024)/1024);
        espacoUsavel = (((espacoUsavel/1024)/1024)/1024);
        
        dataHora = new Date();
        
    }
    
    public void insereDadosHD(){
        
        usoHD();
        
        ConnectURL dadosConexao = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
       
        try{
            jdbcTemplate.update("INSERT INTO tblInfoHD values (?, ?, ?, ?)", this.espacoTotal, this.espacoUsavel, this.idMaquina, this.dataHora);
            
            Notificacao.notificacaoHD(this.espacoTotal, this.espacoUsavel, idMaquina);
            
            GerarLog.escreverLog("Dados de CPU inseridos com sucesso!", "B");
        }
        catch (Exception e){
            GerarLog.escreverLog("Erro ao inserir Dados do HD: " + e.getMessage(), "B");
        }

    }
    
}
