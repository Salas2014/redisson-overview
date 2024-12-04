package lecture;

import dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CashedHashMap07Test extends BaseTest {

    @Test
    public void cashedHashmap() {
        RMapCacheReactive<Integer, Student> mapCache = this.client.getMapCache("users:cashed", new TypedJsonJacksonCodec(Integer.class, Student.class));
        Student student2 = new Student("vlad", 33, "London", List.of(1, 23, 4));
        Student student1 = new Student("Sofa", 30, "London", List.of(1, 23, 4));

        Mono<Student> mono1 = mapCache.put(1, student1, 5, TimeUnit.SECONDS);
        Mono<Student> mono2 = mapCache.put(2, student2, 7, TimeUnit.SECONDS);

        StepVerifier.create(mono1.concatWith(mono2))
                .verifyComplete();

        sleep(3000);
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        sleep(3000);
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        sleep(3000);
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
    }
}
