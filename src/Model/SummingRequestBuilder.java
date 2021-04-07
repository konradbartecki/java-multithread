package Model;

import java.util.Random;

public class SummingRequestBuilder
{
    private final Random random;

    public SummingRequestBuilder() {
        random = new Random();
    }

    public SummingRequest generateRequest()
    {
        Integer a = random.nextInt(20);
        Integer b = random.nextInt(20);
        Integer priorityInt = random.nextInt(3);
        Priority p = Priority.values()[priorityInt];
        String threadId = Thread.currentThread().getName();

        return new SummingRequest(a, b, p, threadId);
    }
}
