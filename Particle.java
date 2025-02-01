import java.util.HashMap;
import java.util.Map;

public class Particle {
    Map<String, Double> position; // Image weights
    Map<String, Double> velocity;
    double fitness;
    Map<String, Double> personalBestPosition;
    double personalBestFitness;

    public Particle(Map<String, Double> initialPosition) {
        this.position = new HashMap<>(initialPosition);
        this.velocity = new HashMap<>();
        this.personalBestPosition = new HashMap<>(initialPosition);
        this.personalBestFitness = Double.NEGATIVE_INFINITY;
    }
}
