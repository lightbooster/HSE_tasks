class Main {
    public static void main(String args[ ]) throws InterruptedException {
        ElevatorSystem system = new ElevatorSystem(1, 5, 5, 2000);
        Thread systemThread = new Thread(system);

        QueryGenerator generator = new QueryGenerator(system, 100, 50, 3);
        Thread generatorThread = new Thread(generator);

        systemThread.start();
        generatorThread.start();
    }
}
