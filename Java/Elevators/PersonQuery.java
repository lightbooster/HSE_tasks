import java.util.concurrent.ThreadLocalRandom;
import java.security.AccessControlException;

// Describe a request to the ElevatorSystem

public class PersonQuery {
    /**
     * <h2>Contain information about query to <code>ElevatorSystem<code/> </h2>
     * p.s. <code>this.id</code> field is useful for working with data structure and additional uniqueness
     */
    private final int id;
    private final int floorStart;
    private final int floorEnd;
    private boolean inElevator = false;

    public PersonQuery(int maxFloor) {
        id = ThreadLocalRandom.current().nextInt();
        floorStart = ThreadLocalRandom.current().nextInt(maxFloor + 1);
        floorEnd = ThreadLocalRandom.current().nextInt(maxFloor + 1);
    }

    @Override
    public String toString() {
        return "PersonQuery{" +
                "id=" + id +
                ", floorStart=" + floorStart +
                ", floorEnd=" + floorEnd +
                ", inElevator=" + inElevator +
                '}';
    }

    public int getFloorStart(){
        return floorStart;
    }
    public int getFloorEnd () throws AccessControlException {
        if (!inElevator){
            throw new AccessControlException("End floor can be got only after entering the elevator");
        } else {
            return floorEnd;
        }
    }
    public Direction getDirection() {
        if (floorStart > floorEnd){
            return Direction.DOWN;
        } else if (floorStart < floorEnd) {
            return Direction.UP;
        } else {
            return Direction.IDLE;
        }
    }

    public void enterElevator() {
        inElevator = true;
    }

    public void exitElevator() {
        inElevator = false;
    }

}
