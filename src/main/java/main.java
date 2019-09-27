import sim.Sim;

public class main {

    private static Thread gameThread;

    public static void main(String[] args) {
        Sim sim = new Sim();
        gameThread = new Thread(sim);
        gameThread.setName("Sim-Loop");
        gameThread.start();
    }



}
