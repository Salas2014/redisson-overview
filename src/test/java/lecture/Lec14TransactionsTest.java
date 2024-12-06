package lecture;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec14TransactionsTest extends BaseTest {

    private RBucketReactive<Long> balance1;
    private RBucketReactive<Long> balance2;

    @BeforeAll
    public void setUpBalances() {
        this.balance1 = this.client.getBucket("user:1:balance", LongCodec.INSTANCE);
        this.balance2 = this.client.getBucket("user:2:balance", LongCodec.INSTANCE);

        Mono<Void> mono = balance1.set(100L)
                .then(balance2.set(0L))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @AfterAll
    public void balanceStatus() {
        Mono<Void> mono = Flux.zip(balance1.get(), balance2.get())
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void nonTransactionsTest() {

        RTransactionReactive transaction = this.client.createTransaction(TransactionOptions.defaults());
        RBucketReactive<Long> trsBalance1 = transaction.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucketReactive<Long> trsBalance2 = transaction.getBucket("user:3:balance", LongCodec.INSTANCE);
        transfer(trsBalance1, trsBalance2, 50)
                .thenReturn(0)
                .map(i -> 5 / 0)
                .then(transaction.commit())
                .doOnError(System.out::println) // some error
                .onErrorResume(ex -> transaction.rollback())
                .subscribe();

    }

    private Mono<Void> transfer(RBucketReactive<Long> fromBalance, RBucketReactive<Long> toBalance, int amount) {
        return Flux.zip(fromBalance.get(), toBalance.get())
                .filter(t -> t.getT1() >= amount)
                .flatMap(t -> fromBalance.set(t.getT1() - amount).thenReturn(t))
                .flatMap(t -> toBalance.set(t.getT2() + amount).thenReturn(t))
                .then();
    }
}
