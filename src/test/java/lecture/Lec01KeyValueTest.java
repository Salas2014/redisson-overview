package lecture;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Lec01KeyValueTest extends BaseTest {

    @Test
    public void keyValueAccess() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Vladyslav");

        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExpiryAccess() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Vladyslav", 100, TimeUnit.SECONDS);

        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExtendExpiry0Access() {
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Vladyslav", 10, TimeUnit.SECONDS);

        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        sleep(500);

        Mono<Boolean> mono = bucket.expire(60, TimeUnit.SECONDS);

        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();

        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(ttl)
                .verifyComplete();
    }
}
