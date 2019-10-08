import sim.Sim;

/**
 * Starter for the Simulation.
 *
 * @author M.Nadler
 */
public class main {
    public static void main(String[] args) {
        Sim sim = new Sim();
        Thread gameThread = new Thread(sim);
        gameThread.setName("Sim-Thread");
        gameThread.start();
    }
}
