/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.host.model;

import java.time.LocalDate;

/**
 *
 * @author victo
 */
public class Convidado {
    private String cpf;
    private String nome;
    private LocalDate idade;
    private boolean flComida;
    private boolean flBebida;
    private boolean flBebidaAlcoolica;
    private boolean flOutros;
    private int evento; // Alterado para int
    private boolean flPago;

    public Convidado(){}
    
    public Convidado( String cpf, String nome, LocalDate idade, boolean flComida, boolean flBebida, boolean flBebidaAlcoolica, boolean flOutros, int evento, boolean flPago) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
        this.flComida = flComida;
        this.flBebida = flBebida;
        this.flBebidaAlcoolica = flBebidaAlcoolica;
        this.flOutros = flOutros;
        this.evento = evento;
        this.flPago = flPago;
    }

    public boolean isFlPago() {
        return flPago;
    }

    public void setFlPago(boolean flPago) {
        this.flPago = flPago;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    // Getters e Setters aqui

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

    public LocalDate getIdade() {
        return idade;
    }

    public void setIdade(LocalDate idade) {
        this.idade = idade;
    }

    public boolean isFlComida() {
        return flComida;
    }

    public void setFlComida(boolean flComida) {
        this.flComida = flComida;
    }

    public boolean isFlBebida() {
        return flBebida;
    }

    public void setFlBebida(boolean flBebida) {
        this.flBebida = flBebida;
    }

    public boolean isFlBebidaAlcoolica() {
        return flBebidaAlcoolica;
    }

    public void setFlBebidaAlcoolica(boolean flBebidaAlcoolica) {
        this.flBebidaAlcoolica = flBebidaAlcoolica;
    }

    public boolean isFlOutros() {
        return flOutros;
    }

    public void setFlOutros(boolean flOutros) {
        this.flOutros = flOutros;
    }

}
