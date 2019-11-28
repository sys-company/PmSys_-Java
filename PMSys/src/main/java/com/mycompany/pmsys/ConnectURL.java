/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys;

/**
 *
 * @author Alex
 */


import org.apache.commons.dbcp2.BasicDataSource;


public class ConnectURL {
    
    private BasicDataSource dataSource;
    
    public ConnectURL(){
        
        /* Conexão Via Spring JDBC */
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://srvdotsys.database.windows.net:1433;database=bddotsys");
        
        
        dataSource.setUsername("userdotsys");
        dataSource.setPassword("#Gfgrupo6");
        
    }
    
    public BasicDataSource getDataSource() {
        return dataSource;
    }
}