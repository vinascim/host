package br.edu.fesa.host.model;

import java.util.Date;

public class Evento {
    private int id;
    private String usuarioCpf;
    private String nome;
    private Date inicio;
    private Date fim;
    private String endereco;
    private int lotacao;
    private double valor;
    private String descricao;

    // Construtores
    public Evento() {
    }

    public Evento(int id, String usuarioCpf, String nome, Date inicio, Date fim, String endereco, int lotacao, double valor, String descricao) {
        this.id = id;
        this.usuarioCpf = usuarioCpf;
        this.nome = nome;
        this.inicio = inicio;
        this.fim = fim;
        this.endereco = endereco;
        this.lotacao = lotacao;
        this.valor = valor;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuarioCpf() {
        return usuarioCpf;
    }

    public void setUsuarioCpf(String usuarioCpf) {
        this.usuarioCpf = usuarioCpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getLotacao() {
        return lotacao;
    }

    public void setLotacao(int lotacao) {
        this.lotacao = lotacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
