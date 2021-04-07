package Jobs;

import Domain.Main;
import Jobs.FutureTaskOfSumming;
import Model.SummingRequestBuilder;
import Model.SummingResponse;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class RequestorJob implements Runnable
{
    private static final Boolean AWAIT_RESULT = true;
    private static final Random r = new Random();

    private final SummingRequestBuilder builder;
    private final PriorityBlockingQueue<FutureTaskOfSumming> requestsQueue;
    private SummingResponse response;

    private Boolean responseReceived = false;

    public RequestorJob(SummingRequestBuilder builder, PriorityBlockingQueue<FutureTaskOfSumming> requestsQueue)
    {
        this.builder = builder;
        this.requestsQueue = requestsQueue;
    }


    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted())
            {
                var job = builder.generateRequest();
                var futureTask = new FutureTaskOfSumming(job, this);
                responseReceived = false;
                synchronized (requestsQueue)
                {
                    requestsQueue.add(futureTask);;
                }

                if(AWAIT_RESULT)
                {
                    while (!responseReceived)
                    {
                        Thread.sleep(100);
                    }
                    System.out.printf("[%s][P=%s] Consuming response from original thread %s+%s=%s\n", Thread.currentThread().getName(), response.getPriority(), response.getA(), response.getB(), response.getResult());
                }
                //Thread.sleep(r.nextInt(r.nextInt(15) +1) * Main.TIMESCALE);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void OnResultReceived(SummingResponse response)
    {
        responseReceived = true;
        var threadName = Thread.currentThread().getName();
        this.response = response;
        System.out.printf("[%s][P=%s] Called OnResultReceived\n", threadName, response.getPriority(), response.getA(), response.getB(), response.getResult());
    }
}
