package br.edu.fesa.host.controller;

import br.edu.fesa.host.dao.CategoriaDespesaDAO;
import br.edu.fesa.host.dao.ConvidadoDAO;
import br.edu.fesa.host.dao.DespesaDAO;
import br.edu.fesa.host.model.CategoriaDespesa;
import br.edu.fesa.host.model.Convidado;
import br.edu.fesa.host.model.Despesa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DashBoardController {

    @FXML
    private BarChart<String, Number> despesasPorCategoriaChart;

    @FXML
    private BarChart<String, Number> convidadosPagantesChart;

    @FXML
    private CategoryAxis xAxisDespesasPorCategoria;

    @FXML
    private NumberAxis yAxisDespesasPorCategoria;

    @FXML
    private CategoryAxis xAxisConvidadosPagantes;

    @FXML
    private NumberAxis yAxisConvidadosPagantes;

    private int eventoId;

    private DespesaDAO despesaDAO;
    private CategoriaDespesaDAO categoriaDAO;
    private ConvidadoDAO convidadoDAO;

    public void setEventoId(int idEvento) {
        this.eventoId = idEvento;
        
        initialize();
        // Use o ID do evento conforme necessário
    }

    public void initialize() {
        despesaDAO = new DespesaDAO();
        categoriaDAO = new CategoriaDespesaDAO();
        convidadoDAO = new ConvidadoDAO();

        // Gráfico de despesas por categoria
        ObservableList<Despesa> despesas = FXCollections.observableArrayList(despesaDAO.listarPorEvento(eventoId));
        ObservableList<XYChart.Data<String, Number>> dataDespesasPorCategoria = FXCollections.observableArrayList();
        for (CategoriaDespesa categoria : categoriaDAO.listar()) {
            double valorTotal = despesas.stream()
                    .filter(despesa -> despesa.getIdCategoria() == categoria.getIdCategoria())
                    .mapToDouble(Despesa::getValor)
                    .sum();
            dataDespesasPorCategoria.add(new XYChart.Data<>(categoria.getCategoria(), valorTotal));
        }
        xAxisDespesasPorCategoria.setLabel("Categoria");
        yAxisDespesasPorCategoria.setLabel("Valor Total");
        despesasPorCategoriaChart.setTitle("Valor Total das Despesas por Categoria");
        XYChart.Series<String, Number> seriesDespesasPorCategoria = new XYChart.Series<>();
        seriesDespesasPorCategoria.setData(dataDespesasPorCategoria);
        despesasPorCategoriaChart.getData().add(seriesDespesasPorCategoria);
        // Remove a legenda
        despesasPorCategoriaChart.setLegendVisible(false);

        // Gráfico de convidados pagantes e não pagantes
        ObservableList<Convidado> convidados = FXCollections.observableArrayList(convidadoDAO.listarPorEvento(eventoId));
        long convidadosPagantes = convidados.stream().filter(Convidado::isFlPago).count();
        long convidadosNaoPagantes = convidados.stream().filter(convidado -> !convidado.isFlPago()).count();

        ObservableList<XYChart.Data<String, Number>> dataConvidadosPagantes = FXCollections.observableArrayList(
                new XYChart.Data<>("Pagantes", convidadosPagantes),
                new XYChart.Data<>("Não Pagantes", convidadosNaoPagantes)
        );
        xAxisConvidadosPagantes.setLabel("Tipo de Convidado");
        yAxisConvidadosPagantes.setLabel("Quantidade");
        convidadosPagantesChart.setTitle("Convidados Pagantes vs Não Pagantes");
        XYChart.Series<String, Number> seriesConvidadosPagantes = new XYChart.Series<>();
        seriesConvidadosPagantes.setData(dataConvidadosPagantes);
        convidadosPagantesChart.getData().add(seriesConvidadosPagantes);
        // Remove a legenda
        convidadosPagantesChart.setLegendVisible(false);
    }
}
