/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import log.GerarLog;

/**
 *
 * @author Ultim
 */
public class TelaIndividual extends javax.swing.JFrame {

    javax.swing.JPanel nomeJPanel;
    private Integer idMaquina;
    JLabel lbNomeCpu;
    JLabel lbCPU;
    JProgressBar barCPU;
    JLabel lbRAM;
    JProgressBar barRAM;
    JLabel lbHD;
    JProgressBar barHD;
    JTable tableProcessos;
    Format formaterHoras = new SimpleDateFormat("HH:mm:ss");
    JLabel lbHoraxtracao;

    public TelaIndividual(String nomeGerente, DadosFuncionarios func) {
        initComponents();

        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width / 2 - getWidth() /2, size.height / 2 - getHeight() /2);
        
        DadosSquads dadosSquads = new DadosSquads(1);

        lbUsuario.setText(nomeGerente);
        lbArea.setText(dadosSquads.getAreaSquad());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("img/small_logo.png"))); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("img/rsz_profileicon.png"))); // NOI18N

        criarTela(func);

        GerarLog.escreverLog("Tela de monitoramento individual do colaborador " + func.getNomeFunc() + " foi aberta", "A", idMaquina);

        t1.start();

    }

    Thread t1 = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {

                    DadosRAM ram = new DadosRAM(idMaquina);
                    DadosCPU cpu = new DadosCPU(idMaquina);
                    DadosHD hd = new DadosHD(idMaquina);
                    DadosProcessos processos = new DadosProcessos(idMaquina);

                    //Atualiza dados CPU
                    lbNomeCpu.setText(cpu.getNomeCpu());
                    lbCPU.setText(cpu.getTotalUso().toString() + "%");
                    barCPU.setValue((int) Math.round(cpu.getTotalUso()));

                    //Atualiza dados RAM
                    lbRAM.setText(formataRamUsada(ram.getTotalRamUsado()) + " GB");
                    barRAM.setValue(Integer.parseInt(ram.getTotalRamUsado().toString().substring(0, 1)));

                    //Atualiza dados HD
                    lbHD.setText(hd.getEspacoTotalDispoivel().toString() + " GB");
                    barHD.setValue(formataHDUsado(hd));

                    //Processos
                    nomeJPanel.remove(tableProcessos);

                    //Processos
                    String colunas[] = {"Nome Processo", "Tempo de Uso"};

                    JTable tableProcessos = new JTable(processos.getDados(), colunas);

                    tableProcessos.setBounds(450, 80, 300, 160);

                    t1.sleep(10000);

                    //Atualizar momento captura
                    Date dt = new Date();
                    lbHoraxtracao.setText(String.format("Dados Atualizados às: %s", formaterHoras.format(dt)));

                }
            } catch (UnsupportedOperationException e) {
                JOptionPane.showMessageDialog(null, "Não suportado \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                GerarLog.escreverLog("Não suportado: " + e.getMessage(), "A", idMaquina);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null, "Coleta interrompida! \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                GerarLog.escreverLog("Coleta interrompida: " + e.getMessage(), "A", idMaquina);
            }
        }
    });

    private String formataRamUsada(Double valor) {
        String ramAntes = valor.toString();
        String statusRamUsada = ramAntes.substring(0, 3);

        return statusRamUsada;
    }

    private Integer formataHDUsado(DadosHD hd) {
        Integer hdTotal = (int) Math.round(hd.getTotalEspaco());
        Integer hdTotalDisponivel = (int) Math.round(hd.getEspacoTotalDispoivel());
        Integer hdTotalUsado = hdTotal - hdTotalDisponivel;

        return hdTotalUsado;
    }

    private void criarTela(DadosFuncionarios func) {
        Font statusFont = new Font("Tahoma", Font.PLAIN, 40);
        Font statusFontHD = new Font("Tahoma", Font.PLAIN, 28);

        idMaquina = func.getIdMaquina();

        DadosRAM ram = new DadosRAM(func.getIdMaquina());
        DadosCPU cpu = new DadosCPU(func.getIdMaquina());
        DadosHD hd = new DadosHD(func.getIdMaquina());
        DadosProcessos processos = new DadosProcessos(func.getIdMaquina());

        nomeJPanel = new javax.swing.JPanel();
        nomeJPanel.setLayout(null);

        //Nome do processador    
        JLabel lbNomePc = new JLabel("Nome do Processador: ");
        lbNomePc.setBounds(40, 10, 200, 20);
        lbNomeCpu = new JLabel(cpu.getNomeCpu());
        lbNomeCpu.setBounds(170, 10, 2000, 20);

        //Status em porcentagem da CPU
        JLabel lbCPUStatus = new JLabel("CPU Status: ");
        lbCPUStatus.setBounds(65, 50, 200, 20);
        lbCPU = new JLabel(cpu.getTotalUso().toString() + "%");
        lbCPU.setFont(statusFontHD);
        lbCPU.setBounds(65, 70, 200, 50);

        //Barra de porcentagem CPU
        barCPU = new JProgressBar(0, 100);
        barCPU.setBounds(20, 120, 150, 20);
        barCPU.setValue((int) Math.round(cpu.getTotalUso()));

        //Status da memoria RAM
        JLabel lbRAMStatus = new JLabel("Uso atual da RAM: ");
        lbRAMStatus.setBounds(255, 50, 200, 20);
        String ramAntes = ram.getTotalRamUsado().toString();
        String statusRamUsada = ramAntes.substring(0, 3);
        lbRAM = new JLabel(statusRamUsada + " GB");
        lbRAM.setBounds(265, 70, 200, 50);
        lbRAM.setFont(statusFontHD);

        //Barra de porcentagem RAM
        String ramBarraTotalAntes = ram.getTotalRam().toString();
        barRAM = new JProgressBar(0, Integer.parseInt(ramBarraTotalAntes.substring(0, 1)));
        barRAM.setBounds(235, 120, 150, 20);
        String ramBarraStatus = ram.getTotalRamUsado().toString();
        barRAM.setValue(Integer.parseInt(ramBarraStatus.substring(0, 1)));

        //Status do HD
        JLabel lbHDStatus = new JLabel("Espaço dísponivel do HD: ");
        lbHDStatus.setBounds(130, 170, 200, 20);
        lbHD = new JLabel(hd.getEspacoTotalDispoivel().toString() + " GB");
        lbHD.setBounds(138, 190, 200, 50);
        lbHD.setFont(statusFontHD);

        //Barra de porcentagem HD
        Integer hdTotal = (int) Math.round(hd.getTotalEspaco());
        barHD = new JProgressBar(0, hdTotal);
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

        tableProcessos = new JTable(processos.getDados(), colunas);

        tableProcessos.setBounds(450, 80, 300, 160);

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

        //Botão para cessar TeamViewer
        JButton btnTeamViewer = new JButton("Ver Tela do Usuário");
        btnTeamViewer.setBounds(625, 270, 150, 30);

        btnTeamViewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TeamViewer tv = new TeamViewer();
                tv.abrirTeamViewer();
            }
        });

        //Botão para cessar TeamViewer
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(545, 310, 100, 30);

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GerarLog.escreverLog("Tela de monitoramento individual do colaborador " + func.getNomeFunc() + " foi fechada", "A", idMaquina);

                dispose();
            }
        });

        //Label dizendo a hora que trouxe os dados
        Date dt = new Date();
        lbHoraxtracao = new JLabel(String.format("Dados Atualizados às: %s", formaterHoras.format(dt)));
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
        nomeJPanel.add(btnVoltar);
        nomeJPanel.add(lbHoraxtracao);

        jTabbedPane1.addTab(func.getNomeFunc(), nomeJPanel);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbNomeSquad = new javax.swing.JLabel();
        lbUsuario = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel2 = new javax.swing.JLabel();
        lbArea = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(java.awt.SystemColor.activeCaption);

        lbNomeSquad.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbNomeSquad.setForeground(new java.awt.Color(255, 255, 255));
        lbNomeSquad.setText("Squad Alfa");

        lbUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbUsuario.setText("Eduardo Menezes,");

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
                .addContainerGap(411, Short.MAX_VALUE))
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
                                .addComponent(lbUsuario))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(lbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
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

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TelaIndividual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TelaIndividual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TelaIndividual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TelaIndividual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TelaIndividual("", null).setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbArea;
    private javax.swing.JLabel lbNomeSquad;
    private javax.swing.JLabel lbUsuario;
    // End of variables declaration//GEN-END:variables
}
