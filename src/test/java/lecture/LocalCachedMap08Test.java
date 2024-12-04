package lecture;

import config.RedissonConfig;
import dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class LocalCachedMap08Test extends BaseTest {

    RLocalCachedMap<Integer, Student> students;

    @BeforeAll
    public void setupClient() {
        RedissonConfig config = new RedissonConfig();
        RedissonClient client = config.redissonClient();

        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer, Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE);

        students = client.getLocalCachedMap(
                        "students",
                        new TypedJsonJacksonCodec(Integer.class, Student.class),
                        mapOptions);

    }

    @Test
    public void test1() {
        Student student2 = new Student("vlad", 33, "London", List.of(1, 23, 4));
        Student student1 = new Student("Sofa", 30, "London", List.of(1, 23, 4));
        students.put(1, student1);
        students.put(2, student2);

        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println(i + " ===> " + students.get(1)))
                .subscribe();

        sleep(600000);
    }

    @Test
    public void test2() {
        Student student1 = new Student("SofaUpdate", 30, "London", List.of(1, 23, 4));
        students.put(1, student1);
    }
}
