package br.edu.fesa.host.controller;

import br.edu.fesa.host.dao.ConvidadoDAO;
import br.edu.fesa.host.dao.DespesaDAO;
import br.edu.fesa.host.dao.EventoDAO;
import br.edu.fesa.host.model.Convidado;
import br.edu.fesa.host.model.Despesa;
import br.edu.fesa.host.model.Evento;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConvidadoController {

    @FXML
    private TableView<Convidado> tableViewConvidados;

    @FXML
    private TableColumn<Convidado, String> colNome;

    @FXML
    private TableColumn<Convidado, Double> colValorPagar;

    @FXML
    private TextField txtNomeConvidado;

    @FXML
    private DatePicker dpIdade;

    @FXML
    private TextField txtCpf;

    @FXML
    private CheckBox chkComidas;

    @FXML
    private CheckBox chkBebidaSemAlcool;

    @FXML
    private CheckBox chkBebidaAlcoolica;

    @FXML
    private CheckBox chkOutros;

    @FXML
    private CheckBox chkPago;

    @FXML
    private Label lblValorPendente;

    @FXML
    private Label lblValorTotal;

    private ConvidadoDAO convidadoDAO;
    private DespesaDAO despesaDAO;
    private EventoDAO eventoDAO;
    private int eventoId;

    public void setEventoId(int idEvento) {
        this.eventoId = idEvento;
        atualizarValoresTotais();
        atualizarValoresPendentes();
        loadConvidados();
    }

    @FXML
    private void initialize() {
        convidadoDAO = new ConvidadoDAO();
        despesaDAO = new DespesaDAO();
        eventoDAO = new EventoDAO();
        setupTableView();
        loadConvidados();
        tableViewConvidados.setOnMouseClicked(this::handleTableViewClicked);
        dpIdade.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int idade = calcularIdade(newValue);
                chkBebidaAlcoolica.setDisable(idade < 18);
                if (idade < 18 && chkBebidaAlcoolica.isSelected()) {
                    chkBebidaAlcoolica.setSelected(false);
                }
            }
        });
    }

    private void setupTableView() {
        if (colNome != null && colValorPagar != null) {
            colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colValorPagar.setCellValueFactory(cellData -> {
                Convidado convidado = cellData.getValue();
                double valorPagar = calcularValorPagar(convidado);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
                nf.setMaximumFractionDigits(2);
                String valorFormatado = nf.format(valorPagar);
                valorPagar = Double.valueOf(valorFormatado.replace(",", "."));
                return new SimpleObjectProperty<>(valorPagar);
            });
        } else {
            System.err.println("Erro: TableColumn não foi injetada corretamente pelo FXML.");
        }
    }

    private void loadConvidados() {
        List<Convidado> convidados = convidadoDAO.listarPorEvento(eventoId);
        tableViewConvidados.getItems().setAll(convidados);
        atualizarValoresTotais();
        atualizarValoresPendentes();
    }

    @FXML
    private void handleTableViewClicked(MouseEvent event) {
        Convidado convidadoSelecionado = tableViewConvidados.getSelectionModel().getSelectedItem();
        if (convidadoSelecionado != null) {
            // Buscar o convidado atualizado pelo CPF no banco de dados
            Convidado convidadoAtualizado = convidadoDAO.buscarPorCpf(convidadoSelecionado.getCpf());

            if (convidadoAtualizado != null) {
                // Preencher os campos do formulário com os dados atualizados
                txtNomeConvidado.setText(convidadoAtualizado.getNome());
                dpIdade.setValue(convidadoAtualizado.getIdade());
                txtCpf.setText(convidadoAtualizado.getCpf());
                chkComidas.setSelected(convidadoAtualizado.isFlComida());
                chkBebidaSemAlcool.setSelected(convidadoAtualizado.isFlBebida());
                chkOutros.setSelected(convidadoAtualizado.isFlOutros());
                chkPago.setSelected(convidadoAtualizado.isFlPago());
                chkBebidaAlcoolica.setSelected(convidadoAtualizado.isFlBebidaAlcoolica());
                // Verificar idade do convidado para habilitar/desabilitar o checkbox de bebida alcoólica
                if (convidadoAtualizado.getIdade() != null) {
                    if (calcularIdade(convidadoAtualizado.getIdade()) < 18) {
                        chkBebidaAlcoolica.setSelected(false);
                        chkBebidaAlcoolica.setDisable(true);
                    } else {
                        chkBebidaAlcoolica.setDisable(false);
                    }
                }
            }
        }
    }

    private int calcularIdade(LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();
        return Period.between(dataNascimento, hoje).getYears();
    }

    @FXML
    private void btAddConvidado(ActionEvent event) {
        if (validarCampos()) {
            // Verificar a quantidade máxima de locação do evento
            String cpf = txtCpf.getText();

            Evento evento = new Evento();
            evento.setId(eventoId);
            int quantidadeMaximaLocacao = eventoDAO.listarPorID(evento).getLotacao();

            if (tableViewConvidados.getItems().size() >= quantidadeMaximaLocacao) {
                mostrarAlerta("Erro", "A quantidade máxima de locação do evento foi alcançada.", Alert.AlertType.ERROR);
                return;
            }

            Convidado existenteConvidado = convidadoDAO.buscarPorCpf(cpf);

            // Se o convidado já existir, atualize seus dados
            if (existenteConvidado != null) {
                existenteConvidado.setNome(txtNomeConvidado.getText());
                existenteConvidado.setIdade(dpIdade.getValue());
                existenteConvidado.setFlComida(chkComidas.isSelected());
                existenteConvidado.setFlBebida(chkBebidaSemAlcool.isSelected());
                existenteConvidado.setFlBebidaAlcoolica(chkBebidaAlcoolica.isSelected());
                existenteConvidado.setFlOutros(chkOutros.isSelected());
                existenteConvidado.setFlPago(chkPago.isSelected());
                existenteConvidado.setEvento(eventoId);
                convidadoDAO.alterar(existenteConvidado);
            } else { // Caso contrário, adicione um novo convidado
                Convidado novoConvidado = new Convidado();
                novoConvidado.setEvento(eventoId);
                novoConvidado.setNome(txtNomeConvidado.getText());
                novoConvidado.setIdade(dpIdade.getValue());
                novoConvidado.setCpf(cpf);
                novoConvidado.setFlComida(chkComidas.isSelected());
                novoConvidado.setFlBebida(chkBebidaSemAlcool.isSelected());
                novoConvidado.setFlBebidaAlcoolica(chkBebidaAlcoolica.isSelected());
                novoConvidado.setFlOutros(chkOutros.isSelected());
                novoConvidado.setFlPago(chkPago.isSelected());
                convidadoDAO.inserir(novoConvidado);
            }

            loadConvidados();
        }
    }

    private boolean validarCampos() {
        String erro = "";

        if (txtNomeConvidado.getText().isEmpty()) {
            erro += "O campo 'Nome do Convidado' é obrigatório.\n";
        }
        if (dpIdade.getValue() == null) {
            erro += "O campo 'Idade' é obrigatório.\n";
        }
        if (txtCpf.getText().isEmpty()) {
            erro += "O campo 'CPF' é obrigatório.\n";
        }
        if (!erro.isEmpty()) {
            mostrarAlerta("Erro de Validação", erro, Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private double calcularValorPagar(Convidado convidado) {
        double valorPagar = 0.0;

        // Verificar se o convidado entra em cada categoria e calcular o valor proporcional
        if (convidado.isFlComida()) {
            valorPagar += calcularValorProporcionalPorCategoria(1);
        }
        if (convidado.isFlBebida()) {
            valorPagar += calcularValorProporcionalPorCategoria(2);
        }
        if (convidado.isFlBebidaAlcoolica()) {
            valorPagar += calcularValorProporcionalPorCategoria(3);
        }
        if (convidado.isFlOutros()) {
            valorPagar += calcularValorProporcionalPorCategoria(4);
        }

        return valorPagar;
    }

    private double calcularValorProporcionalPorCategoria(int categoria) {
        double totalDespesas = calcularTotalDespesasPorTipo(categoria);
        int totalConvidadosNaCategoria = contarConvidadosNaCategoria(categoria);

        if (totalConvidadosNaCategoria > 0) {
            return totalDespesas / totalConvidadosNaCategoria;
        }

        return 0.0;
    }

    private int contarConvidadosNaCategoria(int categoria) {
        int count = 0;

        for (Convidado convidado : tableViewConvidados.getItems()) {
            switch (categoria) {
                case 1:
                    if (convidado.isFlComida()) {
                        count++;
                    }
                    break;
                case 2:
                    if (convidado.isFlBebida()) {
                        count++;
                    }
                    break;
                case 3:
                    if (convidado.isFlBebidaAlcoolica()) {
                        count++;
                    }
                    break;
                case 4:
                    if (convidado.isFlOutros()) {
                        count++;
                    }
                    break;
            }
        }

        return count;
    }

    private double calcularTotalDespesasPorTipo(int tipo) {
        double total = 0.0;
        List<Despesa> despesas = despesaDAO.listar();
        for (Despesa despesa : despesas) {
            if (despesa.getIdEvento() == eventoId && despesa.getIdCategoria() == tipo) {
                total += despesa.getValor();
            }
        }
        return total;
    }

    private void atualizarValoresTotais() {
        double totalValorPagar = 0.0;
        for (Convidado convidado : tableViewConvidados.getItems()) {
            totalValorPagar += calcularValorPagar(convidado);
        }
        lblValorTotal.setText("Valor total " + formatarValor(totalValorPagar));
    }

    private void atualizarValoresPendentes() {
        double valorPendente = 0.0;
        for (Convidado convidado : tableViewConvidados.getItems()) {
            if (!convidado.isFlPago()) {
                valorPendente += calcularValorPagar(convidado);
            }
        }
        lblValorPendente.setText("Valor pendente " + formatarValor(valorPendente));
    }

    private String formatarValor(double valor) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return nf.format(valor);
    }

    @FXML
    private void limparCampos() {
        txtNomeConvidado.clear();
        dpIdade.setValue(null);
        txtCpf.clear();
        chkComidas.setSelected(false);
        chkBebidaSemAlcool.setSelected(false);
        chkBebidaAlcoolica.setSelected(false);
        chkOutros.setSelected(false);
        chkPago.setSelected(false);
    }

    @FXML
    private void excluirConvidado() {
        Convidado convidadoSelecionado = tableViewConvidados.getSelectionModel().getSelectedItem();
        if (convidadoSelecionado != null) {
            tableViewConvidados.getItems().remove(convidadoSelecionado);
            convidadoDAO.remover(convidadoSelecionado);
        } else {
            System.out.println("Nenhum convidado selecionado para exclusão.");
        }
    }
}
