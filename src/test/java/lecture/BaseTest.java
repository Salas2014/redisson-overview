package lecture;

import config.RedissonConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient client;

    @BeforeAll
    public void setClient() {
        client = redissonConfig.redissonReactiveClient();
    }

    @AfterAll
    public void shutdown(){
        client.shutdown();;
    }

    @SneakyThrows
    protected void sleep(long millis) {
        Thread.sleep(millis);
    }

}
