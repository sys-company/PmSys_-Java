/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys;

import com.mycompany.pmsys.ConnectURL;
import com.mycompany.pmsys.DadosProcessos;
import com.mycompany.pmsys.oshi.OshiDados;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.JTable;
import log.GerarLog;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Aluno
 */
public class TelaMonitoramento extends javax.swing.JFrame {

    ConnectURL dadosConexao = new ConnectURL();
    DadosSquads dadosSquads = new DadosSquads(1);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());
    private Integer idSquad = 0;
    private final String nomeGerente;

    public TelaMonitoramento(String nomeGerente) {
        initComponents();
        
        this.nomeGerente = nomeGerente;
        
        DadosSquads dadosSquads = new DadosSquads(1);

        lbUsuario.setText(nomeGerente);
        lbArea.setText(dadosSquads.getAreaSquad());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("img/small_logo.png"))); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("img/rsz_profileicon.png"))); // NOI18N

        
        Toolkit toolkit = getToolkit();
        Dimension size  = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
        
        buscaFuncionarios();
        
        GerarLog.escreverLog("Tela de monitoramento do squad do gerente " + nomeGerente + " foi aberta", "A");
        //atualizarFuncionarios();

    }    

    public void buscaFuncionarios() {

        String stringSql = "select * from tblFuncionario where fkSquad = ?";

        List<DadosFuncionarios> funcionarios = new ArrayList<>();

        this.idSquad = 1;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(stringSql, this.idSquad); //idSquad virá do site

        for (Map row : rows) {
            DadosFuncionarios func = new DadosFuncionarios(this.idSquad,
                    Integer.parseInt(row.get("fkMaquina").toString()),
                    Integer.parseInt(row.get("fkCargo").toString()),
                    row.get("nomeFuncionario").toString(),
                    row.get("identificador").toString(),
                    Integer.parseInt(row.get("idFuncionario").toString()));
            funcionarios.add(func);
        }

        definirLayout(funcionarios);
    }

    private void definirLayout(List<DadosFuncionarios> listFunc) {
        Font statusFont = new Font("Tahoma", Font.PLAIN, 40);
        Font statusFontHD = new Font("Tahoma", Font.PLAIN, 28);
        int contador = 0;

        for (DadosFuncionarios func : listFunc) {
            DadosRAM ram = new DadosRAM(func.getIdMaquina());
            DadosCPU cpu = new DadosCPU(func.getIdMaquina());
            DadosHD hd = new DadosHD(func.getIdMaquina());
            DadosProcessos processos = new DadosProcessos(func.getIdMaquina());

            contador++;

            javax.swing.JPanel nomeJPanel = new javax.swing.JPanel();
            nomeJPanel.setLayout(null);

            //Nome do processador    
            JLabel lbNomePc = new JLabel("Nome do Processador: ");
            lbNomePc.setBounds(40, 10, 200, 20);
            JLabel lbNomeCpu = new JLabel(cpu.getNomeCpu());
            lbNomeCpu.setBounds(170, 10, 2000, 20);

            //Status em porcentagem da CPU
            JLabel lbCPUStatus = new JLabel("CPU Status: ");
            lbCPUStatus.setBounds(65, 50, 200, 20);
            JLabel lbCPU = new JLabel(cpu.getTotalUso().toString() + "%");
            lbCPU.setFont(statusFontHD);
            lbCPU.setBounds(65, 70, 200, 50);

            //Barra de porcentagem CPU
            JProgressBar barCPU = new JProgressBar(0, 100);
            barCPU.setBounds(20, 120, 150, 20);
            barCPU.setValue((int) Math.round(cpu.getTotalUso()));

            //Status da memoria RAM
            JLabel lbRAMStatus = new JLabel("Uso atual da RAM: ");
            lbRAMStatus.setBounds(255, 50, 200, 20);
            String ramAntes = ram.getTotalRamUsado().toString();
            String statusRamUsada = ramAntes.substring(0, 3);
            JLabel lbRAM = new JLabel(statusRamUsada + " GB");
            lbRAM.setBounds(265, 70, 200, 50);
            lbRAM.setFont(statusFontHD);

            //Barra de porcentagem RAM
            String ramBarraTotalAntes = ram.getTotalRam().toString();
            JProgressBar barRAM = new JProgressBar(0, Integer.parseInt(ramBarraTotalAntes.substring(0, 1)));
            barRAM.setBounds(235, 120, 150, 20);
            String ramBarraStatus = ram.getTotalRamUsado().toString();
            barRAM.setValue(Integer.parseInt(ramBarraStatus.substring(0, 1)));

            //Status do HD
            JLabel lbHDStatus = new JLabel("Espaço dísponivel do HD: ");
            lbHDStatus.setBounds(130, 170, 200, 20);
            JLabel lbHD = new JLabel(hd.getEspacoTotalDispoivel().toString() + " GB");
            lbHD.setBounds(138, 190, 200, 50);
            lbHD.setFont(statusFontHD);

            //Barra de porcentagem HD
            Integer hdTotal = (int) Math.round(hd.getTotalEspaco());
            JProgressBar barHD = new JProgressBar(0, hdTotal);
            barHD.setBounds(123, 240, 150, 20);
            Integer hdTotalDisponivel = (int) Math.round(hd.getEspacoTotalDispoivel());
            Integer hdTotalUsado = hdTotal - hdTotalDisponivel;
            barHD.setValue(hdTotalUsado);

            //Processos
            JLabel lbProcessos = new JLabel("Processos:");
            JLabel lbTempoDeUso = new JLabel("Tempo de Uso:");
            lbProcessos.setBounds(450, 37, 100, 50);
            lbTempoDeUso.setBounds(600, 37, 400, 50);

            String colunas[] = {"Nome Processo", "Tempo de Uso"};

            JTable tableProcessos = new JTable(processos.getDados(), colunas);

            tableProcessos.setBounds(450, 80, 300, 160);
            tableProcessos.setEnabled(false);
            

            //Botão mandar mensagem
            JButton btnEnviarMensagem = new JButton("Enviar Mensagem");
            btnEnviarMensagem.setBounds(425, 270, 150, 30);

            btnEnviarMensagem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TelaMensagem tm = new TelaMensagem(
                            func.getIdentificador(), func.getNomeFunc());
                    tm.setVisible(true);
                }
            });

            //Botão para acessar TeamViewer
            JButton btnTeamViewer = new JButton("Ver Tela do Usuário");
            btnTeamViewer.setBounds(625, 270, 150, 30);

            btnTeamViewer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TeamViewer tv = new TeamViewer();
                    tv.abrirTeamViewer();
                }
            });
            
            //Botão para acessar ver informações do usuário em tempo real
            JButton btnTempoReal = new JButton("Visualizar em Tempo Real");
            btnTempoReal.setBounds(505, 310, 180, 30);

            btnTempoReal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TelaIndividual ti = new TelaIndividual(nomeGerente, func);
                    ti.setVisible(true);
                }
            });
            
            //Label dizendo a hora que trouxe os dados
            Date dt = new Date();
            Format formaterHoras = new SimpleDateFormat("HH:mm:ss");
            JLabel lbHoraxtracao = new JLabel(String.format("Dados Atualizados às: %s", formaterHoras.format(dt)));
            lbHoraxtracao.setBounds(5, 340, 500, 30);

            //Adicionando componentes no JPanel
            nomeJPanel.add(lbNomePc);
            nomeJPanel.add(lbNomeCpu);
            nomeJPanel.add(lbCPUStatus);
            nomeJPanel.add(lbRAMStatus);
            nomeJPanel.add(lbHDStatus);
            nomeJPanel.add(lbProcessos);
            nomeJPanel.add(lbTempoDeUso);
            nomeJPanel.add(lbCPU);
            nomeJPanel.add(lbRAM);
            nomeJPanel.add(lbHD);
            nomeJPanel.add(barCPU);
            nomeJPanel.add(barRAM);
            nomeJPanel.add(barHD);
            nomeJPanel.add(tableProcessos);
            nomeJPanel.add(btnTeamViewer);
            nomeJPanel.add(btnEnviarMensagem);
            nomeJPanel.add(btnTempoReal);
            nomeJPanel.add(lbHoraxtracao);
            
            jTabbedPane1.addTab(func.getNomeFunc(), nomeJPanel);
            
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbNomeSquad = new javax.swing.JLabel();
        lbUsuario = new javax.swing.JLabel();
        lbSair = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel2 = new javax.swing.JLabel();
        lbArea = new javax.swing.JLabel();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PMSys - Productivity Management System");
        setResizable(false);

        jPanel1.setBackground(java.awt.SystemColor.activeCaption);

        lbNomeSquad.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbNomeSquad.setForeground(new java.awt.Color(255, 255, 255));
        lbNomeSquad.setText("Squad Alfa");

        lbUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbUsuario.setText("Eduardo Menezes,");

        lbSair.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbSair.setForeground(new java.awt.Color(255, 255, 255));
        lbSair.setText("Sair");
        lbSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbSairMouseClicked(evt);
            }
        });

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(800, 400));
        jTabbedPane1.setVerifyInputWhenFocusTarget(false);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Área: ");

        lbArea.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbSair)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbNomeSquad)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbNomeSquad)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbUsuario)
                                    .addComponent(lbSair)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(lbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbSairMouseClicked
        
        GerarLog.escreverLog("Tela de monitoramento do squad do gerente " + nomeGerente + " foi fechada", "A");
        
        dispose();
        TelaLogin inicio = new TelaLogin();
        inicio.setVisible(true);
    }//GEN-LAST:event_lbSairMouseClicked

    /**
     * @param args the command line arguments
     * @throws org.quartz.SchedulerException
     */
    public static void main(String args[]){


        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaMonitoramento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaMonitoramento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaMonitoramento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaMonitoramento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaMonitoramento("").setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbArea;
    private javax.swing.JLabel lbNomeSquad;
    private javax.swing.JLabel lbSair;
    private javax.swing.JLabel lbUsuario;
    // End of variables declaration//GEN-END:variables

}
