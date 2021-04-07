package Model;

public class SummingResponse extends SummingRequest {
    private final Integer result;

    public SummingResponse(Integer a, Integer b, Priority p, String requestorThreadId, Integer result) {
        super(a, b, p, requestorThreadId);
        this.result = result;
    }

    public SummingResponse(SummingRequest request, Integer result)
    {
        this(request.getA(), request.getB(), request.getPriority(), request.getRequestorThreadId(), result);
    }

    public String getDisplayName()
    {
        return String.format("P=%s | %s+%s=%s", this.getPriority(), this.getA(), this.getB(), this.result);
    }

    public Integer getResult() {
        return result;
    }
}
