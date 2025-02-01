import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class WeightedImageSearchEngine {

    private Map<String, List<ImageWeight>> invertedIndex;
    private Map<String, List<String>> imageTagsMap;

    public WeightedImageSearchEngine(String invertedIndexFilePath, String imageTagsMapFilePath) {
        this.invertedIndex = loadWeightedIndexFromFile(invertedIndexFilePath);
        this.imageTagsMap = loadIndexFromFile(imageTagsMapFilePath);
    }

    private Map<String, List<ImageWeight>> loadWeightedIndexFromFile(String filePath) {
        Map<String, List<ImageWeight>> index = new HashMap<>();
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<ImageWeight>>>() {}.getType();
            index = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index;
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

        Map<String, Double> imageScores = new HashMap<>();

        if (isAndSearch) {
            List<String> sortedTerms = new ArrayList<>(queryTerms);
            sortedTerms.sort(Comparator.comparingInt(term -> invertedIndex.getOrDefault(term, Collections.emptyList()).size()));

            String firstTerm = sortedTerms.get(0);
            List<ImageWeight> initialList = invertedIndex.getOrDefault(firstTerm, Collections.emptyList());
            if (initialList.isEmpty()) {
                return Collections.emptyList();
            }

            Set<String> resultSet = new HashSet<>();
            for (ImageWeight iw : initialList) {
                resultSet.add(iw.imageId);
            }

            // Intersect with image IDs for other terms
            for (int i = 1; i < sortedTerms.size(); i++) {
                String term = sortedTerms.get(i);
                List<ImageWeight> imagesForTag = invertedIndex.getOrDefault(term, Collections.emptyList());
                if (imagesForTag.isEmpty()) {
                    // Early exit if any term has no images
                    return Collections.emptyList();
                }
                Set<String> imagesSet = new HashSet<>();
                for (ImageWeight iw : imagesForTag) {
                    imagesSet.add(iw.imageId);
                }
                resultSet.retainAll(imagesSet);
                if (resultSet.isEmpty()) {
                    // Early exit if result is empty
                    return Collections.emptyList();
                }
            }

            // Collect weights and rank images
            for (String imageId : resultSet) {
                double score = 0.0;
                for (String term : queryTerms) {
                    List<ImageWeight> imagesForTag = invertedIndex.get(term);
                    if (imagesForTag != null) {
                        for (ImageWeight iw : imagesForTag) {
                            if (iw.imageId.equals(imageId)) {
                                score += iw.weight;
                                break;
                            }
                        }
                    }
                }
                imageScores.put(imageId, score);
            }

        } else {
            // OR search
            for (String term : queryTerms) {
                List<ImageWeight> imagesForTag = invertedIndex.get(term);
                if (imagesForTag != null) {
                    for (ImageWeight iw : imagesForTag) {
                        imageScores.put(iw.imageId, imageScores.getOrDefault(iw.imageId, 0.0) + iw.weight);
                    }
                }
            }
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
}
