/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys.oshi;

import com.mycompany.pmsys.ConnectURL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import log.GerarLog;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Aluno
 */
public class Executer {

    private static int idFuncionario = 1004;
    private static int lastID;

    public static void main(String[] args) throws Exception {

        logou();

        JobDetail job = JobBuilder.newJob(OshiDados.class).build();													  //("0 0 10 1/1 * ? *") Produção

        Trigger tarefa = (Trigger) TriggerBuilder.newTrigger()
                .withIdentity("CronTrigger")
                .withSchedule(CronScheduleBuilder
                        .cronSchedule("0 0/1 * 1/1 * ? *")).build();

        Scheduler sc = StdSchedulerFactory.getDefaultScheduler();
        sc.start();
        sc.scheduleJob(job, (org.quartz.Trigger) tarefa);
        System.out.println("Iniciando envio de dados ao Banco");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                deslogou();
            }
        });

    }

    private static void logou() {
        ConnectURL conn = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(conn.getDataSource());

        try {
            jdbcTemplate.update("INSERT INTO tblStatusFuncionario values (?, ?, ?)", new Date(), null, idFuncionario);
            
            System.out.println("Executando select identity");
            List<Map<String, Object>> id = jdbcTemplate.queryForList("select idStatusFuncionario from tblStatusFuncionario where idStatusFuncionario = @@identity");
            
            for(Map row : id){
                lastID = Integer.parseInt(row.get("idStatusFuncionario").toString());
            }

            GerarLog.escreverLog("Dados de status logado inseridos", "B", 0);
        } catch (Exception e) {
            GerarLog.escreverLog("Erro ao inserir dados de logon: " + e.getMessage(), "B", 0);
        }
    }
    private static void deslogou() {
        ConnectURL conn = new ConnectURL();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(conn.getDataSource());

        try {
            jdbcTemplate.update("UPDATE tblStatusFuncionario SET horaSaiu = ? where idStatusFuncionario = ?", new Date(), lastID);

            GerarLog.escreverLog("Usuário deslogou inseridos", "B", 0);
        } catch (Exception e) {
            GerarLog.escreverLog("Erro ao inserir dados de deslog: " + e.getMessage(), "B", 0);
        }
    }
}
