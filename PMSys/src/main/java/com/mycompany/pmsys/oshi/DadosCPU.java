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
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

/**
 *
 * @author Alex
 */
public class DadosCPU {
    
    //Variaveis de CPU
    private double user;
    private double system;
    private double iowait;
    private String cpuName;
    private Double totalUsadoCPU;
    private Date dataHora;
    private int idMaquina;
    
    public DadosCPU(int s){
        this.idMaquina = s;
    }
    
    private final HardwareAbstractionLayer dados = new SystemInfo().getHardware();
    private final CentralProcessor cpu = dados.getProcessor();
    
    private void usoCPU(){
        
        cpuName = cpu.getName();
        
        long[] cpuTicks;
        long[] prevCpuTicks;
        prevCpuTicks = cpu.getSystemCpuLoadTicks();
        Util.sleep(5000);
        
        cpuTicks = cpu.getSystemCpuLoadTicks();
        
        long user = (cpuTicks[CentralProcessor.TickType.USER.getIndex()] - prevCpuTicks[CentralProcessor.TickType.USER.getIndex()]);
        long sys = (cpuTicks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevCpuTicks[CentralProcessor.TickType.SYSTEM.getIndex()]);
        long iowait = (cpuTicks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IOWAIT.getIndex()]);
        
        long nice = (cpuTicks[CentralProcessor.TickType.NICE.getIndex()] - prevCpuTicks[CentralProcessor.TickType.NICE.getIndex()]);
        long idle = (cpuTicks[CentralProcessor.TickType.IDLE.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IDLE.getIndex()]);      
        long irq = (cpuTicks[CentralProcessor.TickType.IRQ.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IRQ.getIndex()]);
        long softirq = (cpuTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevCpuTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()]);
        long steal = (cpuTicks[CentralProcessor.TickType.STEAL.getIndex()] - prevCpuTicks[CentralProcessor.TickType.STEAL.getIndex()]);
        
        long totalcpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        
        this.user = (100d * user) / totalcpu;
        this.system = (100d * sys) / totalcpu;
        this.iowait = (100d * iowait) / totalcpu;
        
        totalUsadoCPU = (100d * (user + sys + iowait)) / totalcpu;
        
        dataHora = new Date();
        
    }
    
    public void insereDadosCPU(){
        
        usoCPU();
        
        ConnectURL dadosConexao = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
       
        try{
            jdbcTemplate.update("INSERT INTO tblInfoCPU values (?, ?, ?, ?, ?, ?)", this.cpuName, this.user, this.system, this.totalUsadoCPU, this.idMaquina, this.dataHora);
            
            Notificacao.notificacaoCPU(this.totalUsadoCPU, this.idMaquina);
            
            GerarLog.escreverLog("Dados de CPU inseridos com sucesso!", "B");
        }
        catch (Exception e){
            GerarLog.escreverLog("Erro ao inserir Dados da CPU: " + e.getMessage(), "B");
        }
//      
    }   
}
