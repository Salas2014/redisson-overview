package lecture;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

public class PubSub11Test extends BaseTest {

    @Test
    public void subscriber1() {
        RTopicReactive topic = this.client.getTopic("telegram-chanel", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void subscriber2() {
        RPatternTopicReactive patternTopic = this.client.getPatternTopic("telegram-chanel*", StringCodec.INSTANCE);

        patternTopic.addListener(String.class,
                (pattern, channel, msg)
                        -> System.out.println("pattent " + pattern + ",channel " + channel + ",msg " + msg))
                        .subscribe();


        sleep(600_000);
    }
}
