package lecture;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class EventListener05 extends BaseTest {

    @Test
    public void expiredEvent(){
        RBucketReactive<Object> bucket = this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Vladyslav", 2, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> mono = bucket.addListener(new ExpiredObjectListener() {
            @Override
            public void onExpired(String name) {
                System.out.println("Expired" + name);
            }
        }).then();

        StepVerifier.create(set.concatWith(get).concatWith(mono))
                .verifyComplete();

        sleep(3000);
    }

    @Test
    public void deletedEvent(){
        RBucketReactive<Object> bucket = this.redissonReactiveClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Vladyslav");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> mono = bucket.addListener((DeletedObjectListener) name -> {
            System.out.println("Deleted " + name);
        }).then();

        StepVerifier.create(set.concatWith(get).concatWith(mono))
                .verifyComplete();

        sleep(33000);
    }
}
