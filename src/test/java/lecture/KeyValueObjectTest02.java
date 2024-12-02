package lecture;

import dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class KeyValueObjectTest02 extends BaseTest {

    @Test
    void keyValueObjectTest() {
        Student student = new Student("Vladyslav", 10, "Atlanta", List.of(1,2,3,4));
        RBucketReactive<Student> bucket =
                this.redissonReactiveClient.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> mono = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(mono))
                .verifyComplete();
    }
}
