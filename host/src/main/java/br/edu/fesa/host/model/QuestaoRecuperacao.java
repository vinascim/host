/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.host.model;

/**
 *
 * @author victo
 */
public class QuestaoRecuperacao {
    private int idQuestao;
    private String pergunta;

    public QuestaoRecuperacao() {}

    public QuestaoRecuperacao(int idQuestao, String pergunta) {
        this.idQuestao = idQuestao;
        this.pergunta = pergunta;
    }

    public int getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(int idQuestao) {
        this.idQuestao = idQuestao;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }
}
