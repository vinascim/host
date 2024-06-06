package br.edu.fesa.host.controller;

import br.edu.fesa.host.App;
import br.edu.fesa.host.dao.EventoDAO;
import br.edu.fesa.host.model.Evento;
import br.edu.fesa.host.model.Usuario;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.Date;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private TableView<Evento> tableEvento;

    @FXML
    private TableColumn<Evento, String> eventosColumn;

    @FXML
    private TableColumn<Evento, Date> inicioColumn;

    @FXML
    private TableColumn<Evento, Date> fimColumn;

    @FXML
    private TableColumn<Evento, Void> acoesColumn;

    private EventoDAO eventoDAO = new EventoDAO();
    //private String usuarioCpf;

//    public void initialize(String cpf) {
//        this.usuarioCpf = cpf;
//    }

    @FXML
    public void initialize() {
        // Configurar colunas
        eventosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

        // Configurar formato das colunas de data
        inicioColumn.setCellValueFactory(cellData -> {
            Evento evento = cellData.getValue();
            ObjectProperty<Date> property = new SimpleObjectProperty<>(evento.getInicio());
            return property;
        });

        fimColumn.setCellValueFactory(cellData -> {
            Evento evento = cellData.getValue();
            ObjectProperty<Date> property = new SimpleObjectProperty<>(evento.getFim());
            return property;
        });

        // Configurar coluna de ações
        Callback<TableColumn<Evento, Void>, TableCell<Evento, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Evento, Void> call(final TableColumn<Evento, Void> param) {
                final TableCell<Evento, Void> cell = new TableCell<>() {

                    private final Button editButton = new Button("Editar");
                    private final Button deleteButton = new Button("Excluir");

                    {
                        editButton.setOnAction(event -> {
                            Evento evento = getTableView().getItems().get(getIndex());
                            System.out.println("Edit button clicked for: " + evento.getNome());
                            // Implementar lógica para editar evento
                            editarEvento(evento);
                        });
                        deleteButton.setOnAction(event -> {
                            Evento evento = getTableView().getItems().get(getIndex());
                            System.out.println("Delete button clicked for: " + evento.getNome());
                            // Implementar lógica para remover evento
                            removerEvento(evento);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsBox = new HBox(editButton, deleteButton);
                            setGraphic(buttonsBox);
                        }
                    }
                };
                return cell;
            }
        };

        acoesColumn.setCellFactory(cellFactory);

        // Carregar dados na tabela
        carregarDados();
    }

    private void carregarDados() {
        List<Evento> eventos = eventoDAO.listar();
        ObservableList<Evento> data = FXCollections.observableArrayList(eventos);
        tableEvento.setItems(data);
    }

    @FXML
    private void editarEvento(Evento evento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/fesa/host/criaEvento.fxml"));
            Parent root = loader.load();

            EventoController controller = loader.getController();
  
            controller.setEvento(evento); // Passa o evento selecionado para preencher os campos

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removerEvento(Evento evento) {
        eventoDAO.remover(evento);
        carregarDados();
    }

    @FXML
    private void btAddEvento() throws IOException {
        EventoController controller = new EventoController();
        Usuario usuario = Usuario.getInstance();
        String usuarioCpf = usuario.getUsuarioCpf();
        App.setRoot("criaEvento");
    }

    @FXML
    private void btAcessaHistorico() {
        // Implementar lógica para acessar histórico
        System.out.println("Botão acessar histórico clicado");
    }
}
