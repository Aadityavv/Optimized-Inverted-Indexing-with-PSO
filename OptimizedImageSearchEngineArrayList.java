import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class OptimizedImageSearchEngineArrayList {

    private Map<String, List<String>> invertedIndex;
    private Map<String, List<String>> imageTagsMap;

    public OptimizedImageSearchEngineArrayList(String invertedIndexFilePath, String imageTagsMapFilePath) {
        this.invertedIndex = loadIndexFromFile(invertedIndexFilePath);
        this.imageTagsMap = loadIndexFromFile(imageTagsMapFilePath);
    }

    private Map<String, List<String>> loadIndexFromFile(String filePath) {
        Map<String, List<String>> index = new HashMap<>();
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            index = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index;
    }

    public List<String> searchImages(String query, boolean isAndSearch) {
        // Process the query terms
        Set<String> queryTerms = processQuery(query);

        if (isAndSearch) {
            if (queryTerms.isEmpty()) {
                return Collections.emptyList();
            }

            // Create a list of query terms sorted by the size of their inverted lists
            List<String> sortedTerms = new ArrayList<>(queryTerms);
            sortedTerms.sort(Comparator.comparingInt(term -> invertedIndex.getOrDefault(term, Collections.emptyList()).size()));

            String firstTerm = sortedTerms.get(0);
            List<String> initialList = invertedIndex.getOrDefault(firstTerm, Collections.emptyList());
            if (initialList.isEmpty()) {
                return Collections.emptyList();
            }

            // Convert to Set for faster retainAll operations
            Set<String> resultSet = new HashSet<>(initialList);

            // Intersect with image IDs for other terms
            for (int i = 1; i < sortedTerms.size(); i++) {
                String term = sortedTerms.get(i);
                List<String> imagesForTag = invertedIndex.getOrDefault(term, Collections.emptyList());
                if (imagesForTag.isEmpty()) {
                    // Early exit if any term has no images
                    return Collections.emptyList();
                }
                resultSet.retainAll(imagesForTag);
                if (resultSet.isEmpty()) {
                    // Early exit if result is empty
                    return Collections.emptyList();
                }
            }
            return new ArrayList<>(resultSet);
        } else {
            // OR search
            Set<String> resultSet = new HashSet<>();
            for (String term : queryTerms) {
                List<String> imagesForTag = invertedIndex.get(term);
                if (imagesForTag != null) {
                    resultSet.addAll(imagesForTag);
                }
            }
            return new ArrayList<>(resultSet);
        }
    }

    Set<String> processQuery(String query) {
        // Simple query processing: split by whitespace, lowercase, remove punctuation
        Set<String> processedTerms = new HashSet<>();
        String[] terms = query.toLowerCase().split("\\s+");
        for (String term : terms) {
            term = term.replaceAll("[^a-zA-Z0-9]", "");
            if (!term.isEmpty()) {
                processedTerms.add(term);
            }
        }
        return processedTerms;
    }

    public List<String> rankImages(List<String> imageIds, Set<String> queryTerms) {
        Map<String, Double> imageScores = new HashMap<>();

        for (String imageId : imageIds) {
            List<String> tags = imageTagsMap.get(imageId);
            double score = 0.0;
            for (String term : queryTerms) {
                if (tags.contains(term)) {
                    // Assign weights based on match type
                    // For simplicity, let's assume:
                    // Exact match weight: 1.0
                    score += 1.0;
                }
                // Additional logic to handle synonyms and hypernyms can be added here
            }
            imageScores.put(imageId, score);
        }
        // Sort images by score in descending order
        List<Map.Entry<String, Double>> sortedImages = new ArrayList<>(imageScores.entrySet());
        sortedImages.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Extract sorted image IDs
        List<String> sortedImageIds = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedImages) {
            sortedImageIds.add(entry.getKey());
        }
        return sortedImageIds;
    }
}
