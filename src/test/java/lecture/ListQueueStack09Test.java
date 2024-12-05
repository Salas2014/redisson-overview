package lecture;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ListQueueStack09Test extends BaseTest {

    @Test
    void listTest() {
        RListReactive<Long> list = this.client.getList("number-input", LongCodec.INSTANCE);

        List<Long> range = LongStream.rangeClosed(1, 10)
                .boxed()
                .collect(Collectors.toList());

        StepVerifier.create(list.addAll(range).then())
                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    void listQueue() {
        RQueueReactive<Long> queue = this.client.getQueue("number-input", LongCodec.INSTANCE);

        Mono<Void> queuePoll = queue.poll()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queuePoll)
                .verifyComplete();

        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();
    }

    // like a Stack
    @Test
    void listDeque() {
        RDequeReactive<Long> deque = this.client.getDeque("number-input", LongCodec.INSTANCE);

        Mono<Void> pollLast = deque.pollLast()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(pollLast)
                .verifyComplete();

        StepVerifier.create(deque.size())
                .expectNext(2)
                .verifyComplete();
    }
}
