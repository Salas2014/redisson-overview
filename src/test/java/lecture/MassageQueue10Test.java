package lecture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class MassageQueue10Test extends BaseTest {

    private RBlockingQueueReactive<Long> msgQueue;

    @BeforeAll
    public void setUp() {
        msgQueue = this.client.getBlockingQueue("messqe-queue", LongCodec.INSTANCE);
    }

    @Test
    void consume1() {
        this.msgQueue.takeElements()
                .doOnNext(i -> System.out.println("Consumer 1: " + i))
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    void consume2() {
        this.msgQueue.takeElements()
                .doOnNext(i -> System.out.println("Consumer 2: " + i))
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    void produce(){
        Mono<Void> mono = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(500))
                .doOnNext(i -> System.out.println("going to add " + i))
                .flatMap(i -> this.msgQueue.add(Long.valueOf(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
