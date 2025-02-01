import java.io.*;
import java.util.*;
import com.google.gson.*;

public class UnoptimizedImageSearchEngine {

    private JsonObject invertedIndex;
    private JsonObject imageTagsMap;

    public UnoptimizedImageSearchEngine(String invertedIndexFilePath, String imageTagsMapFilePath) {
        this.invertedIndex = loadJsonObjectFromFile(invertedIndexFilePath);
        this.imageTagsMap = loadJsonObjectFromFile(imageTagsMapFilePath);
    }

    private JsonObject loadJsonObjectFromFile(String filePath) {
        JsonObject jsonObject = null;
        try (Reader reader = new FileReader(filePath)) {
            jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public Set<String> searchImages(String query, boolean isAndSearch) {
        Set<String> queryTerms = processQuery(query);

        if (isAndSearch) {
            Iterator<String> iterator = queryTerms.iterator();
            if (!iterator.hasNext()) {
                return Collections.emptySet();
            }
            String firstTerm = iterator.next();
            Set<String> resultImageIds = getImageSetFromJsonArray(invertedIndex.getAsJsonArray(firstTerm));

            while (iterator.hasNext()) {
                String term = iterator.next();
                Set<String> imagesForTag = getImageSetFromJsonArray(invertedIndex.getAsJsonArray(term));
                if (imagesForTag == null || imagesForTag.isEmpty()) {
                    return Collections.emptySet();
                }
                resultImageIds.retainAll(imagesForTag);
                if (resultImageIds.isEmpty()) {
                    break;
                }
            }
            return resultImageIds;
        } else {
            Set<String> resultImageIds = new HashSet<>();
            for (String term : queryTerms) {
                Set<String> imagesForTag = getImageSetFromJsonArray(invertedIndex.getAsJsonArray(term));
                if (imagesForTag != null) {
                    resultImageIds.addAll(imagesForTag);
                }
            }
            return resultImageIds;
        }
    }

    private Set<String> getImageSetFromJsonArray(JsonArray jsonArray) {
        if (jsonArray == null) {
            return Collections.emptySet();
        }
        Set<String> imageSet = new HashSet<>();
        for (JsonElement element : jsonArray) {
            imageSet.add(element.getAsString());
        }
        return imageSet;
    }

    Set<String> processQuery(String query) {
        Set<String> processedTerms = new HashSet<>();
        String[] terms = query.toLowerCase().split("\\s+");
        for (String term : terms) {
            term = term.replaceAll("[^a-z0-9]", "");
            if (!term.isEmpty()) {
                processedTerms.add(term);
            }
        }
        return processedTerms;
    }

    public List<String> rankImages(Set<String> imageIds, Set<String> queryTerms) {
        Map<String, Double> imageScores = new HashMap<>();

        for (String imageId : imageIds) {
            Set<String> tags = getTagSetFromJsonArray(imageTagsMap.getAsJsonArray(imageId));
            double score = 0.0;
            for (String term : queryTerms) {
                if (tags.contains(term)) {
                    score += 1.0;
                }
            }
            imageScores.put(imageId, score);
        }

        List<Map.Entry<String, Double>> sortedImages = new ArrayList<>(imageScores.entrySet());
        sortedImages.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<String> sortedImageIds = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedImages) {
            sortedImageIds.add(entry.getKey());
        }
        return sortedImageIds;
    }

    private Set<String> getTagSetFromJsonArray(JsonArray jsonArray) {
        if (jsonArray == null) {
            return Collections.emptySet();
        }
        Set<String> tagSet = new HashSet<>();
        for (JsonElement element : jsonArray) {
            tagSet.add(element.getAsString());
        }
        return tagSet;
    }
}
