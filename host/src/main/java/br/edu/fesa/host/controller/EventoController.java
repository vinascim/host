package br.edu.fesa.host.controller;

import br.edu.fesa.host.App;
import br.edu.fesa.host.dao.EventoDAO;
import br.edu.fesa.host.model.Evento;
import br.edu.fesa.host.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EventoController implements Initializable {

    @FXML
    private TextField txtEvento;

    @FXML
    private DatePicker txtInicio;

    @FXML
    private DatePicker txtFim;

    @FXML
    private TextField txtLocal;

    @FXML
    private TextField txtConvidados;

    @FXML
    private TextField txtAluguel;

    @FXML
    private TextField txtSobre;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btDespensas;

    @FXML
    private Button btConvidados;

    @FXML
    private Button btGrafico;

    @FXML
    private Label lbVoltar;

    @FXML
    private Label lbSair;

    private EventoDAO eventoDAO;

    private String usuarioCpf;

    private Evento eventoSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventoDAO = new EventoDAO();
        Usuario usuario = Usuario.getInstance();
        this.usuarioCpf = usuario.getUsuarioCpf();
    }

    public void setEvento(Evento evento) {
        txtEvento.setText(evento.getNome());
        txtInicio.setValue(convertToLocalDate(evento.getInicio()));
        txtFim.setValue(convertToLocalDate(evento.getFim()));
        txtLocal.setText(evento.getEndereco());
        txtConvidados.setText(String.valueOf(evento.getLotacao()));
        txtAluguel.setText(String.valueOf(evento.getValor()));
        txtSobre.setText(evento.getDescricao());

        eventoSelecionado = evento;
    }

    @FXML
    private void btSalvar() {
        if (validarCampos()) {
            String nome = txtEvento.getText();
            LocalDate inicio = txtInicio.getValue();
            LocalDate fim = txtFim.getValue();
            String endereco = txtLocal.getText();
            int lotacao = Integer.parseInt(txtConvidados.getText());
            double valor = Double.parseDouble(txtAluguel.getText());
            String descricao = txtSobre.getText();

            if (eventoSelecionado != null && eventoSelecionado.getId() > 0) {
                eventoSelecionado.setNome(nome);
                eventoSelecionado.setInicio(convertToDate(inicio));
                eventoSelecionado.setFim(convertToDate(fim));
                eventoSelecionado.setEndereco(endereco);
                eventoSelecionado.setLotacao(lotacao);
                eventoSelecionado.setValor(valor);
                eventoSelecionado.setDescricao(descricao);

                eventoDAO.alterar(eventoSelecionado);
            } else {
                Evento evento = new Evento(0, usuarioCpf, nome, convertToDate(inicio), convertToDate(fim), endereco, lotacao, valor, descricao);
                eventoDAO.inserir(evento);

                int ultimoIdEvento = eventoDAO.buscarUltimoIdEvento();

                // Definir o evento selecionado com o último evento inserido
                eventoSelecionado = new Evento();
                eventoSelecionado.setId(ultimoIdEvento);
            }

            mostrarAlerta("Sucesso", "Evento salvo com sucesso!", AlertType.INFORMATION);
        }
    }

    private boolean validarCampos() {
        String erro = "";

        if (txtEvento.getText().isEmpty()) {
            erro += "O campo 'Evento' é obrigatório.\n";
        }
        if (txtInicio.getValue() == null) {
            erro += "O campo 'Início' é obrigatório.\n";
        }
        if (txtFim.getValue() == null) {
            erro += "O campo 'Fim' é obrigatório.\n";
        }
        if (txtLocal.getText().isEmpty()) {
            erro += "O campo 'Local' é obrigatório.\n";
        }
        if (txtConvidados.getText().isEmpty()) {
            erro += "O campo 'Convidados' é obrigatório.\n";
        } else {
            try {
                Integer.parseInt(txtConvidados.getText());
            } catch (NumberFormatException e) {
                erro += "O campo 'Convidados' deve ser um número inteiro.\n";
            }
        }
        if (txtAluguel.getText().isEmpty()) {
            erro += "O campo 'Aluguel' é obrigatório.\n";
        } else {
            try {
                Double.parseDouble(txtAluguel.getText());
            } catch (NumberFormatException e) {
                erro += "O campo 'Aluguel' deve ser um número decimal.\n";
            }
        }
        if (txtSobre.getText().isEmpty()) {
            erro += "O campo 'Sobre' é obrigatório.\n";
        }

        if (!erro.isEmpty()) {
            mostrarAlerta("Erro de Validação", erro, AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private LocalDate convertToLocalDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    private Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    @FXML
    private void btDespesas() throws IOException {
        setRootDespesa("criaDespesas", eventoSelecionado.getId());
    }

    @FXML
    private void btConvidados() throws IOException {
        setRootConvidado("convidado", eventoSelecionado.getId());
    }

    @FXML
    private void btGrafico() throws IOException {
        setRootGrafico("DashBoard", eventoSelecionado.getId());
    }

    @FXML
    private void voltarParaTelaHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void lbSair() {
        // Ação para sair
    }

    public static void setRootDespesa(String fxml, int idEvento) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        DespesaController controller = loader.getController();
        controller.setEventoId(idEvento);

        stage.show();
    }

    public static void setRootConvidado(String fxml, int idEvento) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        ConvidadoController controller = loader.getController();
        controller.setEventoId(idEvento);

        stage.show();
    }

    public static void setRootGrafico(String fxml, int idEvento) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        DashBoardController controller = loader.getController();
        controller.setEventoId(idEvento);

        stage.show();
    }
}
