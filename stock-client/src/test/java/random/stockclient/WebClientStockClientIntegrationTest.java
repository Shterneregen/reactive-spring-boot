package random.stockclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

class WebClientStockClientIntegrationTest {

	private WebClient build = WebClient.builder().build();

	@Test
	void shouldRetrieveStockPricesFromTheService() {
		WebClientStockClient webClientStockClient = new WebClientStockClient(build);
		Flux<StockPrice> prices = webClientStockClient.pricesFor("SYMBOL");

		Assertions.assertNotNull(prices);
		Flux<StockPrice> fivePrices = prices.take(5);
		Assertions.assertEquals(5, fivePrices.count().block());
		Assertions.assertEquals("SYMBOL", fivePrices.blockFirst().getSymbol());
	}
}
