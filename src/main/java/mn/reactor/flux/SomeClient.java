package mn.reactor.flux;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;

@Client(id = "some")
public interface SomeClient {

    @Get
    Flux<SomeDto> getSome();
}
