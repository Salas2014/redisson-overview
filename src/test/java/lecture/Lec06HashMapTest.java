package lecture;

import dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class Lec06HashMapTest extends BaseTest {

    @Test
    public void hashmap() {
        RMapReactive<String, String> map = this.client.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "Vladyslav");
        Mono<String> age = map.put("age", "20");
        Mono<String> city = map.put("city", "Madrid");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    public void hashmap2() {
        RMapReactive<String, String> map = this.client.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> mapValues = Map.of(
                "name", "Vladyslav",
                "age", "20",
                "city", "Madrid"
        );

        Mono<Void> mono = map.putAll(mapValues).then();

        StepVerifier.create(map.putAll(mapValues).then())
                .verifyComplete();
    }

    @Test
    public void hashmap3() {
        TypedJsonJacksonCodec typedJsonJacksonCodec = new TypedJsonJacksonCodec(Integer.class, Student.class);

        RMapReactive<Integer, Student> users = this.client.getMap("users", typedJsonJacksonCodec);
        Mono<Student> mono1 = users.put(1, new Student("vlad", 33, "London", List.of(1, 23, 4)));
        Mono<Student> mono2 = users.put(2, new Student("Sofa", 30, "London", List.of(1, 23, 4)));

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();
    }
}
