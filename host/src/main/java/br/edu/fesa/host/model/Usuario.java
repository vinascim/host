/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.host.model;

import java.time.LocalDate;

/**
 *
 * @author Everymind
 */
public class Usuario {
     private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String senha;
    private String recoveryQST; // Pergunta de recuperação
    private String recoveryANS; // Resposta de recuperação

    private static Usuario instance;
    private String usuarioCpf;
    
    public Usuario(){}
    
    // Construtor
    public Usuario(String cpf, String nome, LocalDate dataNascimento, String email, String senha, String recoveryQST, String recoveryANS) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;
        this.recoveryQST = recoveryQST;
        this.recoveryANS = recoveryANS;
    }

       public static Usuario getInstance() {
        if (instance == null) {
            instance = new Usuario();
        }
        return instance;
    }

    public String getUsuarioCpf() {
        return usuarioCpf;
    }

    public void setUsuarioCpf(String usuarioCpf) {
        this.usuarioCpf = usuarioCpf;
    }
    
    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRecoveryQST() {
        return recoveryQST;
    }

    public void setRecoveryQST(String recoveryQST) {
        this.recoveryQST = recoveryQST;
    }

    public String getRecoveryANS() {
        return recoveryANS;
    }

    public void setRecoveryANS(String recoveryANS) {
        this.recoveryANS = recoveryANS;
    }
}
