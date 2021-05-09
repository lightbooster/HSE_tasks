import java.util.concurrent.ThreadLocalRandom;

public class QueryGenerator implements Runnable{
    private final ElevatorSystem system;
    private final int maxFloor;
    private final int maxQueriesNum;
    private final long maxTime;
    private final long minTime;

    public QueryGenerator(ElevatorSystem system, long maxTime, long minTime, int maxQueriesNum){
        this.system = system;
        maxFloor = system.getMaxFloor();
        this.maxTime = maxTime;
        this.minTime = minTime;
        this.maxQueriesNum = maxQueriesNum;
    }

    public void run(){
        int curQueriesNum = 0;
        while(true){
            if (maxQueriesNum >= 0){
                if (curQueriesNum < maxQueriesNum){
                    ++curQueriesNum;
                } else {
                    System.out.println("*QueryGenerator Terminated*\n");
                    return;
                }
            }
            long waitTime = minTime + ThreadLocalRandom.current().nextLong(maxTime - minTime);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e){
                System.out.print("*ERROR*: " + e.getMessage());
                continue;
            }
            PersonQuery newQuery = new PersonQuery(maxFloor);
            system.addQuery(newQuery);

        }
    }

}
