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


import log.GerarLog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class OshiDados implements Job{


    public void execute(JobExecutionContext args0) throws JobExecutionException {

        DadosCPU dCpu = new DadosCPU(1000);
        DadosRAM dRam = new DadosRAM(1000);
        DadosHD dHd = new DadosHD(1000);
        DadosProcessos dProcessos = new DadosProcessos(1000);
        
        try{
        GerarLog.escreverLog("Inserindo dados de CPU...", "B");
            System.out.println("Inserindo dados de CPU...");
        dCpu.insereDadosCPU();
        GerarLog.escreverLog("Inserindo dados de RAM...", "B");
            System.out.println("Inserindo dados de RAM...");
        dRam.insereDadosRam();
        GerarLog.escreverLog("Inserindo dadosde HD...", "B");
            System.out.println("Inserindo dadosde HD...");
        dHd.insereDadosHD();
        GerarLog.escreverLog("Enviando processos...", "B");
            System.out.println("Enviando processos...");
        dProcessos.processosAtuais();
        }catch(Exception e){
            GerarLog.escreverLog("Erro ao inserir dados : " +e, "B");
        }

    }
    //Pega informações gerais do hardware do PC





}
        
        
