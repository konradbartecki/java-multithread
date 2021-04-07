package Jobs;

import Domain.Main;
import Model.SummingResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ExecutorJob implements Runnable {
    private final PriorityBlockingQueue<FutureTaskOfSumming> todoQueue;
    private final PriorityBlockingQueue<SummingResponse> resultQueue;

    public ExecutorJob(PriorityBlockingQueue<FutureTaskOfSumming> todoQueue, PriorityBlockingQueue<SummingResponse> resultQueue) {
        this.todoQueue = todoQueue;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()) {
                //System.out.printf("[%s] Polling...\n", Thread.currentThread().getName());
                var job = todoQueue.peek();
                if(job == null)
                {
                    Thread.sleep(50);
                    continue;
                }
                else{
                    System.out.printf("[%s] Starting job...\n", Thread.currentThread().getName());
                    synchronized (todoQueue)
                    {
                        job = todoQueue.take();
                    }
                }
                synchronized (job)
                {
                    System.out.printf("[%s] Executing...\n", Thread.currentThread().getName());
                    job.run();
                    System.out.printf("[%s] Waiting some time before getting new task...\n", Thread.currentThread().getName());
                    Thread.sleep(Main.TIMESCALE * 5);

                    var result = job.get();
                    resultQueue.add(result);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            return;
        }
        catch (ExecutionException e){
            System.out.println(e.getMessage());
            return;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
