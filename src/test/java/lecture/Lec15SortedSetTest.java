package lecture;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLexSortedSetReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    void sortedSet() {
        RScoredSortedSetReactive<Object> scoredSortedSet = this.client.getScoredSortedSet("sorted:set", StringCodec.INSTANCE);

        Mono<Void> mono = scoredSortedSet.add(2.2, "Vladyslav")
                .then(scoredSortedSet.addScore("Nikitos", 3.4)
                        .then(scoredSortedSet.addScore("Gaga", 5.5)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        scoredSortedSet.entryRange(0, 2)
                .flatMapIterable(Function.identity())
                .map(it -> it.getScore() + " " + it.getValue())
                .doOnNext(System.out::println)
                .subscribe();

        sleep(60000);
    }
}
