class Main {
    public static void main(String args[ ]) throws InterruptedException {
        ElevatorSystem system = new ElevatorSystem(4, 10, 5, 1000);
        Thread systemThread = new Thread(system);

        QueryGenerator generator = new QueryGenerator(system, 2000, 500, 100);
        Thread generatorThread = new Thread(generator);

        systemThread.start();
        generatorThread.start();
    }
}
