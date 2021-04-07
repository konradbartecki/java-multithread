package Jobs;

import Domain.Main;
import Model.SummingRequest;
import Model.SummingResponse;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.FutureTask;

public class FutureTaskOfSumming extends FutureTask<SummingResponse> implements Comparable<FutureTaskOfSumming> {

    private final RequestorJob requestorJob;
    private final SummingRequest request;

    public FutureTaskOfSumming(SummingRequest request, RequestorJob requestorJob){
        super(() -> {
            var result = request.getA() + request.getB();
            System.out.printf("[%s] Executed result %s+%s=%s\n", Thread.currentThread().getName(), request.getA(), request.getB(), result);
            return new SummingResponse(request, result);
        });
        this.request = request;
        this.requestorJob = requestorJob;
    }

    public void done() {
        SummingResponse result = null;
        try {
            result = this.get();
        } catch(Exception exc) {
            try {
                requestorJob.OnResultReceived(result);
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }
            return;
        }
        try {
            requestorJob.OnResultReceived(result);
        } catch(Exception exc) {
            System.out.println(exc.getMessage());
            return;
        }
    }

    public String getDisplayName()
    {
        return String.format("P=%s | %s+%s", request.getPriority(), request.getA(), request.getB());
    }

    @Override
    public int compareTo(FutureTaskOfSumming o) {
        return this.request.getPriority().compareTo(o.request.getPriority());
    }
}
