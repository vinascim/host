/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.host.dao;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Locale;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author victo
 */
public class Conexao {
    private final ResourceBundle BUNDLE = ResourceBundle.getBundle("dao", new Locale("pt", "BR"));
    private static Conexao conexao;

    private Conexao() {

    }

    public static Conexao getConexao() {
        if (conexao == null) {
            conexao = new Conexao();
        }
        return conexao;
    }

    public Connection getConnection()  {
        Connection connection = null;
        try {
            Class.forName(BUNDLE.getString("driver"));
            connection = DriverManager.getConnection(BUNDLE.getString("url"), BUNDLE.getString("usuario"), BUNDLE.getString("senha"));
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            //throw new Exception("Erro ao carregar a conexão com o banco de dados!");
        } catch (SQLException ex) {
            //Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            //throw new Exception("Não foi possível realizar a operação no banco de dados!");
        }
         return connection;
    }
}
