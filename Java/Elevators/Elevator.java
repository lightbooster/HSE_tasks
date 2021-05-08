import java.security.AccessControlException;
import java.util.Vector;


public class Elevator implements Runnable{
    /**
     * <h2>Operate queries received from <code>ElevatorSystem</code></h2>
     * <h3>Optimization:</h3>
     * 1. ElevatorSystem intellectually distributes the queries among
     *    the Elevators and add the query as open.<p>
     * 2. Elevator handle open queries by itself.<p>
     * 3. The open queries are processing in a queue<p>
     * 4. First in, last out<p>
     * 5. Taken queries are in priority<p>
     * 6. Take open query along the way to target<p>
     * 7. Drop off taken query along the way target
     */
    private final int maxFloor;
    private final int maxCapacity;
    private final long speed;

    private int curFloor = 0;
    private int curCapacity = 0;
    private int targetFloor = 0;
    private boolean isTargeted = false;
    private boolean isMoving = false;

    private Vector<PersonQuery> openQueries;
    private Vector<PersonQuery> takenQueries;

    public Elevator(int maxFloor, int maxCapacity, long speed) {
        this.maxFloor = maxFloor;
        this.maxCapacity = maxCapacity;
        this.speed = speed;

        openQueries = new Vector<>(maxCapacity);
        takenQueries = new Vector<>(maxCapacity);
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "curFloor=" + curFloor +
                ", curCapacity=" + curCapacity +
                ", targetFloor=" + targetFloor +
                ", isTargeted=" + isTargeted +
                ", isMoving=" + isMoving +
                ", OpenQ's=" + openQueries.size() +
                ", TakenQ's=" + takenQueries.size() +
                '}';
    }

    public int getCurFloor() {
        return curFloor;
    }

    public int getCurCapacity() {
        return curCapacity;
    }

    public boolean getIsTargeted() {
        return isTargeted;
    }

    public boolean getIsMoving() {
        return isMoving;
    }

    public Direction getDirection() {
        if (curFloor > targetFloor){
            return Direction.DOWN;
        } else if (curFloor < targetFloor) {
            return Direction.UP;
        } else {
            return Direction.IDLE;
        }
    }

    public int getOpenQueriesSize() {
        return openQueries.size();
    }

    public synchronized void addOpenQuery(PersonQuery query) {
        openQueries.add(query);
    }

    private void takeOpenQuery(int index) {
        PersonQuery query = openQueries.get(index);
        query.enterElevator();
        if (query.getFloorEnd() != curFloor && query.getFloorEnd() <= maxFloor){
            takenQueries.add(query);
            ++curCapacity;
        }
    }

    private void takeOpenQuery(PersonQuery query) {
        query.enterElevator();
        if (query.getFloorEnd() != curFloor && query.getFloorEnd() <= maxFloor){
            takenQueries.add(query);
            ++curCapacity;
        }
    }

    private void removeTakenQuery(int index) {
        takenQueries.remove(index);
        --curCapacity;
    }

    private void removeTakenQuery(Integer[] indexes) {
        Vector<PersonQuery> tmp = new Vector<>(takenQueries.size() - indexes.length);
        for(int i = 0; i < takenQueries.size(); i++){
            boolean removeCondition = false;
            for(int j = 0; j < indexes.length; j++){
                if (i == indexes[j]){
                    removeCondition = true;
                    break;
                }
            }
            if (!removeCondition){
                tmp.add(takenQueries.get(i));
            }
        }
        int prevlength = takenQueries.size();
        takenQueries = tmp;
        curCapacity -= prevlength - takenQueries.size();
    }

    private void removeTakenQuery(PersonQuery query) {
        takenQueries.remove(query);
        --curCapacity;
    }

    private void removeOpenQuery(int index){
        openQueries.remove(index);
    }

    private void removeOpenQuery(Integer[] indexes) {
        Vector<PersonQuery> tmp = new Vector<>(openQueries.size() - indexes.length);
        for(int i = 0; i < openQueries.size(); i++){
            boolean removeCondition = false;
            for(int j = 0; j < indexes.length; j++){
                if (i == indexes[j]){
                    removeCondition = true;
                    break;
                }
            }
            if (!removeCondition){
                tmp.add(openQueries.get(i));
            }
        }
        openQueries = tmp;
    }

    private void removeOpenQuery(PersonQuery query){
        openQueries.remove(query);
    }

    private void dropOffQueries() {
        Vector<Integer> candidates = new Vector<>(curCapacity);
        for (int index = 0; index < takenQueries.size(); index++){
            int queryFloorEnd = -1;
            try {
                queryFloorEnd = takenQueries.get(index).getFloorEnd();
            } catch (AccessControlException e){
                System.out.print("*ERROR*: " + e.getMessage());
                continue;
            }
            if (curFloor == queryFloorEnd){
                candidates.add(index);
            }
        }
//        System.out.println("candidates for DropOff: " + candidates);
        removeTakenQuery((Integer[]) candidates.toArray(new Integer[0]));

    }

    private void takeQueries() {
        Vector<Integer> candidates = new Vector<>(openQueries.size());
        for(int index = 0; index < openQueries.size(); index++){
            int queryFloorStart = openQueries.get(index).getFloorStart();
            if (curFloor == queryFloorStart){
                candidates.add(index);
            }
        }
        Vector<Integer> removeCandidates = new Vector<>(candidates.size());
        for (int index : candidates) {
            if (curCapacity >= maxCapacity){
                break;
            }
            takeOpenQuery(index);
            removeCandidates.add(index);
        }
        removeOpenQuery((Integer[]) removeCandidates.toArray(new Integer[0]));
    }

    public void run(){
        while (true){
            if (openQueries.isEmpty() && takenQueries.isEmpty()){
                targetFloor = curFloor;
                isTargeted = false;
                continue;
            }

            dropOffQueries();
            takeQueries();

            if (targetFloor == curFloor){
                isTargeted = false;
            }

            if (openQueries.isEmpty() && takenQueries.isEmpty()){
                targetFloor = curFloor;
                isTargeted = false;
                continue;
            }

            // Choose new target
            if (!isTargeted){
                if (!takenQueries.isEmpty()){
                    // Drop off a taken query
                    try {
                        targetFloor = takenQueries.get(0).getFloorEnd();
                        isTargeted = true;
                    } catch (AccessControlException e){
                        System.out.print("*ERROR*: " + e.getMessage());
                        System.out.print("*Elevator Terminated*");
                        return;
                    }
                } else {
                    // Take an open query
                    targetFloor = openQueries.get(0).getFloorStart();
                    isTargeted = true;
                }
            }

            // Move to target
            isMoving = true;
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                System.out.print("*ERROR*: " + e.getMessage());
            }
            curFloor += (curFloor - targetFloor) > 0 ? -1 : 1;
            isMoving = false;
        }
    }

}

