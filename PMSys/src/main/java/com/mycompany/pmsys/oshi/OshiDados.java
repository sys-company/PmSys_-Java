/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys.oshi;

/**
 *
 * @author Ultim
 */


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import log.GerarLog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class OshiDados implements Job{


    public void execute(JobExecutionContext args0) throws JobExecutionException {
        Integer idMaquina = 1015;
        DadosCPU dCpu = new DadosCPU(idMaquina);
        DadosRAM dRam = new DadosRAM(idMaquina);
        DadosHD dHd = new DadosHD(idMaquina);
        DadosProcessos dProcessos = new DadosProcessos(idMaquina);
        Format formatador = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dt = new Date();
        String dataLog = formatador.format(dt);
        
        try{
        GerarLog.escreverLog("Inserindo dados de CPU...", "B", idMaquina);
            System.out.println(dataLog + " | Inserindo dados de CPU...");
        dCpu.insereDadosCPU();
        GerarLog.escreverLog("Inserindo dados de RAM...", "B", idMaquina);
            System.out.println(dataLog + " | Inserindo dados de RAM...");
        dRam.insereDadosRam();
        GerarLog.escreverLog("Inserindo dadosde HD...", "B", idMaquina);
            System.out.println(dataLog + " | Inserindo dadosde HD...");
        dHd.insereDadosHD();
        GerarLog.escreverLog("Enviando processos...", "B", idMaquina);
            System.out.println(dataLog + " | Enviando processos...");
        dProcessos.processosAtuais();
        }catch(Exception e){
            GerarLog.escreverLog("Erro ao inserir dados : " +e, "B", idMaquina);
        }

    }
    //Pega informações gerais do hardware do PC





}
        
        
