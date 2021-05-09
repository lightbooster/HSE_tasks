import java.util.Vector;

public class ElevatorSystem implements Runnable{
    /**
     * <h2>Operate all queries</h2>
     * Distribute queries by calculating a 'suitableness' score of each elevator
     */
    private final int elevatorsNum;
    private final int maxFloor;
    private final int maxElevatorCapacity;
    private final long systemSpeed;

    private final Elevator[] elevators;
    private final Thread[] elevatorThreads;

    ElevatorSystem(int elevatorsNum, int maxFloor, int maxElevatorCapacity, long systemSpeed){
        this.elevatorsNum = elevatorsNum;
        this.maxFloor = maxFloor;
        this.maxElevatorCapacity = maxElevatorCapacity;
        this.systemSpeed = systemSpeed;
        elevators = new Elevator[elevatorsNum];
        elevatorThreads = new Thread[elevatorsNum];
        for (int i = 0; i < elevatorsNum; i++){
            elevators[i] = new Elevator(maxFloor, maxElevatorCapacity, systemSpeed);
            elevatorThreads[i] = new Thread(elevators[i]);
        }
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public int getElevatorsNum() {
        return elevatorsNum;
    }

    public int getMaxElevatorCapacity() {
        return maxElevatorCapacity;
    }

    public synchronized void addQuery(PersonQuery query){
        float bestScore = calculateScore(query, elevators[0]);
        int bestElevator = 0;
        for (int i = 1; i < elevatorsNum; i++){
            float curScore = calculateScore(query, elevators[i]);
            if (curScore > bestScore){
                bestScore = curScore;
                bestElevator = i;
            }
        }
        elevators[bestElevator].addOpenQuery(query);
        System.out.println("*ElevatorSystem*:\n" + query + "\nto\n" + elevators[bestElevator] + '\n');
    }

    public float calculateScore(PersonQuery query, Elevator elevator){
        float score = 0;
        int distanceScore = maxFloor - Math.abs(query.getFloorStart() - elevator.getCurFloor());
        Direction elevatorDirection = elevator.getDirection();
        int directionScore = 0;
        if (elevatorDirection == Direction.UP){
            if (elevator.getCurFloor() <= query.getFloorStart()){
                directionScore = 1;
            } else {
                directionScore = -1;
            }
        } else if (elevatorDirection == Direction.DOWN){
            if (elevator.getCurFloor() >= query.getFloorStart()){
                directionScore = 1;
            } else {
                directionScore = -1;
            }
        }
        int capacityScore = maxElevatorCapacity - elevator.getCurCapacity();
        int elevatorQueueScore = -1 * elevator.getOpenQueriesSize();

        score = distanceScore + distanceScore * directionScore + elevatorQueueScore + capacityScore;
        return score;
    }

    private void startElevatorThreads() {
        for (Thread th : elevatorThreads){
            th.start();
        }
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();
        message.append("ElevatorSystem:\n");
        for (Elevator el : elevators){
            message.append(el.toString() + '\n');
        }
        return message.toString();
    }

    public void run(){
        startElevatorThreads();
        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.print("*ERROR*: " + e.getMessage());
                continue;
            }
            System.out.println(this);
            System.out.println("");
        }
    }
}
