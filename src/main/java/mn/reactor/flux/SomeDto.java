package mn.reactor.flux;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class SomeDto {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
