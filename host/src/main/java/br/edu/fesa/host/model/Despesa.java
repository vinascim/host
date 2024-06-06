package br.edu.fesa.host.model;

public class Despesa {

    private int idEvento;
    private int idCategoria;
    private String nome;
    private double valor;
    private CategoriaDespesa categoria; 

    public Despesa(int idEvento, int idCategoria, String nome, double valor) {
        this.idEvento = idEvento;
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.valor = valor;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public CategoriaDespesa getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }
}
