package random.stockui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.springframework.stereotype.Component;
import random.stockclient.StockClient;
import random.stockclient.StockPrice;

import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static javafx.collections.FXCollections.observableArrayList;

@Component
public class ChartController {

	@FXML
	public LineChart<String, Double> chart;

	private StockClient stockClient;

	public ChartController(StockClient stockClient) {
		this.stockClient = stockClient;
	}

	@FXML
	public void initialize() {
		final PriceSubscriber priceSubscriber1 = getPriceSubscriber("SYMBOL1");
		final PriceSubscriber priceSubscriber2 = getPriceSubscriber("SYMBOL2");

		ObservableList<Series<String, Double>> data = observableArrayList();
		data.add(priceSubscriber1.getSeries());
		data.add(priceSubscriber2.getSeries());
		chart.setData(data);

	}

	private PriceSubscriber getPriceSubscriber(String symbol) {
		final PriceSubscriber priceSubscriber = new PriceSubscriber(symbol);
		stockClient.pricesFor(symbol).subscribe(priceSubscriber);
		return priceSubscriber;
	}

	private static class PriceSubscriber implements Consumer<StockPrice> {

		private final ObservableList<Data<String, Double>> seriesData = observableArrayList();
		private final Series<String, Double> series;

		public PriceSubscriber(String symbol) {
			series = new Series<>(symbol, seriesData);
		}

		@Override
		public void accept(StockPrice stockPrice) {
			Platform.runLater(() -> {
				seriesData.add(new Data<>(valueOf(stockPrice.getTime().getSecond()), stockPrice.getPrice()));
			});
		}

		public Series<String, Double> getSeries() {
			return series;
		}
	}
}
