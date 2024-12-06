package lecture;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

public class Batch12Test extends BaseTest {

    @Test
    public void butchTest() {
        RBatchReactive batch = this.client.createBatch();
        RListReactive<Long> list1 = batch.getList("list1", LongCodec.INSTANCE);
        RSetReactive<Long> set1 = batch.getSet("set1", LongCodec.INSTANCE);

        for (long i = 0; i < 1_000_000; i++) {
            list1.add(i);
            set1.add(i);
        }

        StepVerifier.create(batch.execute().then())
                .verifyComplete();
    }
}
