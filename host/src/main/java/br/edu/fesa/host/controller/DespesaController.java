package br.edu.fesa.host.controller;

import br.edu.fesa.host.dao.CategoriaDespesaDAO;
import br.edu.fesa.host.dao.DespesaDAO;
import br.edu.fesa.host.model.CategoriaDespesa;
import br.edu.fesa.host.model.Despesa;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.stage.Stage;

public class DespesaController implements Initializable {

    @FXML
    private TextField txtItem;

    @FXML
    private TextField txtValor;

    @FXML
    private ComboBox<CategoriaDespesa> cboCategoria;

    @FXML
    private TableView<Despesa> tableDespesas;

    @FXML
    private TableColumn<Despesa, String> colItem;

    @FXML
    private TableColumn<Despesa, Double> colValor;

    @FXML
    private TableColumn<Despesa, String> colCategoria;

    @FXML
    private TableColumn<Despesa, Void> colAcoes;

    @FXML
    private Label lbSair;

    @FXML
    private Label lbVoltar;
    private Stage stage;
    private DespesaDAO despesaDAO;

    private ObservableList<Despesa> despesasList;

    private CategoriaDespesaDAO categoriaDespesaDAO;

    private int eventoId;

    public void setEventoId(int idEvento) {
        this.eventoId = idEvento;

        carregarDados();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        despesaDAO = new DespesaDAO();
        despesasList = FXCollections.observableArrayList(despesaDAO.listarPorEvento(eventoId));

        colItem.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colCategoria.setCellValueFactory(cellData -> {
            int codigoCategoria = cellData.getValue().getIdCategoria();
            String nomeCategoria = "";

            // Atribuir o nome da categoria com base no código da categoria
            switch (codigoCategoria) {
                case 1:
                    nomeCategoria = "Comidas";
                    break;
                case 2:
                    nomeCategoria = "Bebidas";
                    break;
                case 3:
                    nomeCategoria = "Bebidas Alcoólicas";
                    break;
                case 4:
                    nomeCategoria = "Outros";
                    break;
                default:
                    nomeCategoria = "Categoria Desconhecida";
                    break;
            }

            return new SimpleStringProperty(nomeCategoria);
        });

        // Configurar coluna de ações
        Callback<TableColumn<Despesa, Void>, TableCell<Despesa, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Despesa, Void> call(final TableColumn<Despesa, Void> param) {
                final TableCell<Despesa, Void> cell = new TableCell<>() {
                    private final Button deleteButton = new Button("Excluir");

                    {
                        deleteButton.setOnAction(event -> {
                            Despesa despesa = getTableView().getItems().get(getIndex());
                            System.out.println("Delete button clicked for: " + despesa.getNome());
                            // Implementar lógica para remover despesa
                            removerDespesa(despesa);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsBox = new HBox(deleteButton);
                            setGraphic(buttonsBox);
                        }
                    }
                };
                return cell;
            }
        };

        colAcoes.setCellFactory(cellFactory);

        tableDespesas.setItems(despesasList);

        categoriaDespesaDAO = new CategoriaDespesaDAO();
        List<CategoriaDespesa> categorias = categoriaDespesaDAO.listar();

        // Verifique se a lista de categorias não é null ou vazia
        if (categorias != null && !categorias.isEmpty()) {
            ObservableList<CategoriaDespesa> listaCategorias = FXCollections.observableArrayList(categorias);
            cboCategoria.setItems(listaCategorias);
        }

        cboCategoria.setConverter(new StringConverter<>() {
            @Override
            public String toString(CategoriaDespesa categoria) {
                return categoria != null ? categoria.getCategoria() : ""; // Retorna uma string vazia se categoria for null
            }

            @Override
            public CategoriaDespesa fromString(String string) {
                return null; // Não é necessário para exibição
            }
        });
    }

    @FXML
    private void btAddDespesa(ActionEvent event) {
        if (validarCampos()) {
            String nome = txtItem.getText();
            double valor = Double.parseDouble(txtValor.getText());
            CategoriaDespesa categoria = cboCategoria.getValue();

            Despesa despesa = new Despesa(eventoId, categoria.getIdCategoria(), nome, valor);
            despesaDAO.inserir(despesa);
            despesasList.add(despesa);

            txtItem.clear();
            txtValor.clear();
            cboCategoria.setValue(null);

            carregarDados();
        }
    }

    private boolean validarCampos() {
        String erro = "";

        if (txtItem.getText().isEmpty()) {
            erro += "O campo 'Item' é obrigatório.\n";
        }
        if (txtValor.getText().isEmpty()) {
            erro += "O campo 'Valor' é obrigatório.\n";
        } else {
            try {
                Double.parseDouble(txtValor.getText());
            } catch (NumberFormatException e) {
                erro += "O campo 'Valor' deve ser um número decimal.\n";
            }
        }
        if (cboCategoria.getValue() == null) {
            erro += "O campo 'Categoria' é obrigatório.\n";
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

    private void carregarDados() {
        List<Despesa> despesas = despesaDAO.listarPorEvento(eventoId);
        ObservableList<Despesa> data = FXCollections.observableArrayList(despesas);
        tableDespesas.setItems(data);
    }

    private void removerDespesa(Despesa despesa) {
        despesaDAO.remover(despesa);
        carregarDados();
    }

    @FXML
    private void lbSair(ActionEvent event) {
        // Lógica para sair do aplicativo
    }

    @FXML
    private void lbVoltar(ActionEvent event) {
        // Lógica para voltar à tela anterior
    }
}
