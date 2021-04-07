package Model;

public class SummingRequest implements Comparable<SummingRequest> {
    private final Integer a;

    public Integer getA() {
        return a;
    }

    public Integer getB() {
        return b;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getRequestorThreadId() {
        return requestorThreadId;
    }

    private final Integer b;

    private final Priority priority;
    private final String requestorThreadId;

    public SummingRequest(Integer a, Integer b, Priority p, String requestorThreadId) {
        this.a = a;
        this.b = b;
        this.priority = p;
        this.requestorThreadId = requestorThreadId;
        Report("Created");
    }

    private void Report(String message, Object... params) {
        String finalMessage = String.format("[%s][p:%s] %s+%s | %s\n", this.requestorThreadId, this.priority, this.getA(), this.getB(), String.format(message, params));
        System.out.printf(finalMessage);
    }

    @Override
    public int compareTo(SummingRequest o) {
        return this.priority.compareTo(o.priority);
    }

}
