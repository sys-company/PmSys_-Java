/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys.oshi;

import com.mycompany.pmsys.ConnectURL;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import log.GerarLog;
import org.springframework.jdbc.core.JdbcTemplate;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

/**
 *
 * @author Alex
 */
public class DadosProcessos {
    
    //Variaveis ainda a serem criadas
    
    private SystemInfo si = new SystemInfo();
    private OperatingSystem os = si.getOperatingSystem();
    private FileSystem fs = os.getFileSystem();
    
    private String nomeProcesso;
    private String tempoDeUso;
    private Date dataCapturada;
    private int idMaquina;
    private long horas;
    
    public DadosProcessos(int i){
        this.idMaquina = i;
    }
    
    private int contador = 1;
    
    public void processosAtuais(){
        //Pegar os 10 primeiros dados de processos de acordo com a memoria
        List<OSProcess> procs = Arrays.asList(os.getProcesses(0, OperatingSystem.ProcessSort.MEMORY));
        
        for(int i = 0; i < procs.size(); i++){
            OSProcess p = procs.get(i);
            
            
            if(this.contador > 1){
                break;
            }else{
                analisaProcessosValidos(p);
            }
            
            
        }
        
    }
    
    private void analisaProcessosValidos(OSProcess p){
        
        try {
            BufferedReader buffRead = new BufferedReader(new FileReader("src/main/java/com/mycompany/pmsys/systemProccess.txt"));
            String linha = "";
            boolean processoUser = false;
            
            while(true){
                if(linha != null){
                    if(p.getUser().equals("") || p.getName().equals(linha)){
                        processoUser = false;
                        break;
                    }else{
                        processoUser = true;
                        
                    }
                }else
                    break;
                
                linha = buffRead.readLine();
            }
            
            if(this.contador <= 10){
                if(processoUser){
//                  System.out.print("User: " + p.getUser() + " | ");
//                  System.out.format("Process ID: %s, Process Name: %s, Tempo de Uso: %.2f\n", p.getParentProcessID(), p.getName(), (((1d * p.getUpTime())/1000)/60)/60);
                    this.nomeProcesso = p.getName();

                    long minutos = (p.getUpTime() / 60000) % 60;
                    this.horas = (p.getUpTime() / 3600000);
                    String upTime = String.format("%3d:%02d", this.horas, minutos);

                    this.tempoDeUso = upTime;
                    this.dataCapturada = new Date();
                    
                    this.contador++;
                    
                    insereDadosProcessos();
                }
            }
            
        } catch (FileNotFoundException e) {
            GerarLog.escreverLog("Erro ao encontrar arquivo de ignore dos processos: " + e.getMessage(), "B");
        } catch(IOException e){
            GerarLog.escreverLog("Erro ao ler arquivo de ignore dos processos: " + e.getMessage(), "B");
        }
    }
    
    private void insereDadosProcessos(){
        
        ConnectURL conn = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(conn.getDataSource());
        
        try{
            jdbcTemplate.update("INSERT INTO tblInfoProcessos values (?, ?, ?, ?)", this.nomeProcesso, this.tempoDeUso, this.dataCapturada, this.idMaquina);
            
            Notificacao.notificacaoProcesso(this.horas, this.nomeProcesso, this.idMaquina);
            
            GerarLog.escreverLog("Dados de processos inseridos com sucesso!", "B");
        }catch (Exception e){
            GerarLog.escreverLog("Erro ao inserir Dados dos Processos: " + e.getMessage(), "B");
        }
        
    }
    
}
