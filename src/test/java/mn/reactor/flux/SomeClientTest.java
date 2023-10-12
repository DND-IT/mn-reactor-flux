package mn.reactor.flux;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SomeClientTest implements TestPropertyProvider {

    @Inject
    SomeClient someClient;

    protected WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());

    @Override
    public Map<String, String> getProperties() {
        wireMockServer.start();
        return Map.of("client.some.url", wireMockServer.baseUrl());
    }

    @AfterAll
    void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void reproduceError() {
        // given
        wireMockServer.stubFor(WireMock.get("/")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody("""
                                [{
                                    "data": "111"
                                }, {
                                    "data": "222"
                                }, {
                                    "data": "333"
                                }, {
                                    "data": "444"
                                }]
                                """)));

        // when
        Flux<SomeDto> fluxResponse = someClient.getSome();

        // then
        List<SomeDto> someDtos = fluxResponse.collectList().block();
        assertThat(someDtos).hasSize(4);
    }
}
