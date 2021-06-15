# Elevators multithread task  
## Project structure:  
* [<code>PersonQuery</code>](./PersonQuery.java) - instance of a query to an <code>ElevatorSystem</code>.  
* [<code>Elevator</code>](./Elevator.java) - instance of Elevator, has own logic to handle appointed queries.  
* [<code>ElevatorSystem</code>](./ElevatorSystem.java) - instantiates all Elevators and smartly distribute queries among instances of Elevators.  
* [<code>QueryGenerator</code>](./QueryGenerator.java) - generates queries to <code>ElevatorSystem</code>.  
## Logs:  
You can watch the example of running the program in the log files (text has been copied from the console):  
* [logExample.txt](./logExample.txt) - 1 Elevator, nicely represents own logic of Elevator   
* [logExample2.txt](./logExample2.txt) - 2 Elevators  
  
  **NOTE:** <code>ElevatorSystem</code> is fully scalable and example logs do not reflect the limits.
