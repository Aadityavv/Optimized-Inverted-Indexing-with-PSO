import java.util.*;

public class PSO {
    private List<Particle> swarm;
    private Map<String, Double> globalBestPosition;
    private double globalBestFitness;
    private int numParticles;
    private int maxIterations;
    private double inertiaWeight;
    private double cognitiveCoeff;
    private double socialCoeff;
    private Map<String, List<String>> imageTagsMap;
    private Map<String, Integer> tagDocumentFrequency;
    private List<Set<String>> proxyQueries;

    public PSO(Map<String, List<String>> imageTagsMap, int numParticles, int maxIterations) {
        this.imageTagsMap = imageTagsMap;
        this.numParticles = numParticles;
        this.maxIterations = maxIterations;
        this.inertiaWeight = 0.729; // Commonly used value
        this.cognitiveCoeff = 1.49445; // Commonly used value
        this.socialCoeff = 1.49445; // Commonly used value
        this.swarm = new ArrayList<>();
        this.globalBestFitness = Double.NEGATIVE_INFINITY;
        this.tagDocumentFrequency = computeTagDocumentFrequency();
        this.proxyQueries = generateProxyQueries();
    }

    private Map<String, Integer> computeTagDocumentFrequency() {
        Map<String, Integer> df = new HashMap<>();
        for (List<String> tags : imageTagsMap.values()) {
            for (String tag : tags) {
                df.put(tag, df.getOrDefault(tag, 0) + 1);
            }
        }
        return df;
    }

    private List<Set<String>> generateProxyQueries() {
        // Generate combinations of tags as proxy queries
        // For simplicity, we'll use random combinations
        List<Set<String>> queries = new ArrayList<>();
        List<String> allTags = new ArrayList<>(tagDocumentFrequency.keySet());
        Random random = new Random();
        for (int i = 0; i < 100; i++) { // Generate 100 proxy queries
            Set<String> query = new HashSet<>();
            for (int j = 0; j < 3; j++) { // Each query has 3 tags
                String tag = allTags.get(random.nextInt(allTags.size()));
                query.add(tag);
            }
            queries.add(query);
        }
        return queries;
    }

    public void optimize() {
        initializeSwarm();
        for (int iter = 0; iter < maxIterations; iter++) {
            for (Particle particle : swarm) {
                particle.fitness = evaluateFitness(particle.position);
                if (particle.fitness > particle.personalBestFitness) {
                    particle.personalBestFitness = particle.fitness;
                    particle.personalBestPosition = new HashMap<>(particle.position);
                }
                if (particle.fitness > globalBestFitness) {
                    globalBestFitness = particle.fitness;
                    globalBestPosition = new HashMap<>(particle.position);
                }
            }
            updateVelocitiesAndPositions();
            // Optionally, print progress
             System.out.println("Iteration " + iter + ", Global Best Fitness: " + globalBestFitness);
        }
    }

    private void initializeSwarm() {
        Random random = new Random();
        for (int i = 0; i < numParticles; i++) {
            Map<String, Double> initialPosition = new HashMap<>();
            for (String imageId : imageTagsMap.keySet()) {
                initialPosition.put(imageId, random.nextDouble());
            }
            Particle particle = new Particle(initialPosition);
            for (String imageId : imageTagsMap.keySet()) {
                particle.velocity.put(imageId, random.nextDouble() * 0.1 - 0.05); // Small initial velocity
            }
            swarm.add(particle);
        }
    }

    private double evaluateFitness(Map<String, Double> position) {
        double fitness = 0.0;
        for (Set<String> query : proxyQueries) {
            Map<String, Double> imageScores = new HashMap<>();
            for (String imageId : imageTagsMap.keySet()) {
                List<String> tags = imageTagsMap.get(imageId);
                double score = 0.0;
                for (String tag : query) {
                    if (tags.contains(tag)) {
                        double tagImportance = 1.0 / tagDocumentFrequency.get(tag);
                        score += position.get(imageId) * tagImportance;
                    }
                }
                imageScores.put(imageId, score);
            }
            // Sum top N image scores as part of fitness
            fitness += imageScores.values().stream().sorted(Comparator.reverseOrder()).limit(10).mapToDouble(Double::doubleValue).sum();
        }
        return fitness;
    }

    private void updateVelocitiesAndPositions() {
        Random random = new Random();
        for (Particle particle : swarm) {
            for (String imageId : particle.position.keySet()) {
                double r1 = random.nextDouble();
                double r2 = random.nextDouble();

                double cognitive = cognitiveCoeff * r1 * (particle.personalBestPosition.get(imageId) - particle.position.get(imageId));
                double social = socialCoeff * r2 * (globalBestPosition.get(imageId) - particle.position.get(imageId));

                double newVelocity = inertiaWeight * particle.velocity.get(imageId) + cognitive + social;
                particle.velocity.put(imageId, newVelocity);

                double newPosition = particle.position.get(imageId) + newVelocity;
                // Apply constraints
                if (newPosition > 1.0) newPosition = 1.0;
                if (newPosition < 0.0) newPosition = 0.0;
                particle.position.put(imageId, newPosition);
            }
        }
    }

    public Map<String, Double> getOptimizedWeights() {
        return globalBestPosition;
    }
}
