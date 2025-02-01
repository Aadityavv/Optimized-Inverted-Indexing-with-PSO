//import java.util.List;
//import java.util.Set;
//
//public class Main {
//    public static void main(String[] args) {
//        String invertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\invertedIndex.json";
//        String imageTagsMapFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";
//
//        ImageSearchEngine searchEngine = new ImageSearchEngine(invertedIndexFilePath, imageTagsMapFilePath);
//
//        // Example query
//        String query = "cancel protest";
//
//        boolean isAnd = true;
//
//        // Start timing the query processing
//        long startTime = System.nanoTime();
//
//        // Search for images
//        Set<String> resultImageIds = searchEngine.searchImages(query, isAnd);
//
//        // Process the query terms for ranking
//        Set<String> queryTerms = searchEngine.processQuery(query);
//
//        // Rank images
//        List<String> rankedImageIds = searchEngine.rankImages(resultImageIds, queryTerms);
//
//        // End timing the query processing
//        long endTime = System.nanoTime();
//
//        // Calculate response time in milliseconds
//        long responseTimeMs = (endTime - startTime) / 1_000_000;
//
//        // Number of images found
//        int imagesFound = rankedImageIds.size();
//
//        // Display results
//        System.out.println("Search results for query: " + query);
//        System.out.println("Response Time: " + responseTimeMs + "ms");
//        System.out.println("Images Found: " + imagesFound);
//        System.out.println();
//
//        for (String imageId : rankedImageIds) {
//            System.out.println(imageId);
//        }
//    }
//}
//import java.util.List;
//import java.util.Set;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        String invertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\invertedIndex.json";
//        String imageTagsMapFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";
//
//        ImageSearchEngine searchEngine = new ImageSearchEngine(invertedIndexFilePath, imageTagsMapFilePath);
//
//        Scanner scanner = new Scanner(System.in);
//
//        // Get user input for query
//        System.out.print("Enter your search query: ");
//        String query = scanner.nextLine();
//
//        // Get user input for search type
//        System.out.print("Select search type (AND/OR): ");
//        String searchTypeInput = scanner.nextLine().trim().toUpperCase();
//        boolean isAnd = searchTypeInput.equals("AND");
//
//        // Start timing the query processing
//        long startTime = System.nanoTime();
//
//        // Search for images
//        List<String> resultImageIds = searchEngine.searchImages(query, isAnd);
//
//        // End timing the query processing
//        long endTime = System.nanoTime();
//
//        // Calculate response time in milliseconds
//        long responseTimeMs = (endTime - startTime) / 1_000_000;
//
//        // Number of images found
//        int imagesFound = resultImageIds.size();
//
//        // Display results
//        System.out.println("Search results for query: " + query);
//        System.out.println("Response Time: " + responseTimeMs + "ms");
//        System.out.println("Images Found: " + imagesFound);
//        System.out.println();
//
//        List<String> finalImageIds;
//
//        if (isAnd) {
//            // For AND search, no need to rank images
//            finalImageIds = resultImageIds;
//        } else {
//            // For OR search, rank images
//            // Process the query terms for ranking
//            Set<String> queryTerms = searchEngine.processQuery(query);
//            finalImageIds = searchEngine.rankImages(resultImageIds, queryTerms);
//        }
//
//        // Display the image IDs
//        for (String imageId : finalImageIds) {
//            System.out.println(imageId);
//        }
//
//        scanner.close();
//    }
//}



//import java.util.List;
//import java.util.Set;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        String invertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\invertedIndex.json";
//        String imageTagsMapFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";
//
//        Scanner scanner = new Scanner(System.in);
//
//        // Get user input for query
//        System.out.print("Enter your search query: ");
//        String query = scanner.nextLine();
//
//        // Get user input for search type
//        System.out.print("Select search type (AND/OR): ");
//        String searchTypeInput = scanner.nextLine().trim().toUpperCase();
//        boolean isAnd = searchTypeInput.equals("AND");
//
//        System.out.println();
//
//        // ============================
//        // Unoptimized Implementation
//        // ============================
//        System.out.println("Unoptimized Inverted Index using JSON object Results");
//        UnoptimizedImageSearchEngine unoptimizedEngine = new UnoptimizedImageSearchEngine(invertedIndexFilePath, imageTagsMapFilePath);
//
//        long startTime = System.nanoTime();
//        Set<String> resultImageIdsUnoptimized = unoptimizedEngine.searchImages(query, isAnd);
//        long endTime = System.nanoTime();
//        long responseTimeUnoptimized = (endTime - startTime) / 1_000_000;
//
//        int imagesFoundUnoptimized = resultImageIdsUnoptimized.size();
//
//        System.out.println("Search results for query: " + query);
//        System.out.println("Response Time: " + responseTimeUnoptimized + "ms");
//        System.out.println("Images Found: " + imagesFoundUnoptimized);
//        System.out.println();
//
//        List<String> finalImageIdsUnoptimized = unoptimizedEngine.rankImages(resultImageIdsUnoptimized, unoptimizedEngine.processQuery(query));
//
//        for (String imageId : finalImageIdsUnoptimized) {
//            System.out.println(imageId);
//        }
//
//        System.out.println();
//
//        // ============================
//        // Optimized Implementation with HashSet
//        // ============================
//        System.out.println("Optimized Inverted Index using HashSets for images Results");
//        OptimizedImageSearchEngineHashSet optimizedEngineHashSet = new OptimizedImageSearchEngineHashSet(invertedIndexFilePath, imageTagsMapFilePath);
//
//        startTime = System.nanoTime();
//        List<String> resultImageIdsOptimizedHashSet = optimizedEngineHashSet.searchImages(query, isAnd);
//        endTime = System.nanoTime();
//        long responseTimeOptimizedHashSet = (endTime - startTime) / 1_000_000;
//
//        int imagesFoundOptimizedHashSet = resultImageIdsOptimizedHashSet.size();
//
//        System.out.println("Search results for query: " + query);
//        System.out.println("Response Time: " + responseTimeOptimizedHashSet + "ms");
//        System.out.println("Images Found: " + imagesFoundOptimizedHashSet);
//        System.out.println();
//
//        List<String> finalImageIdsOptimizedHashSet;
//        if (isAnd) {
//            finalImageIdsOptimizedHashSet = resultImageIdsOptimizedHashSet;
//        } else {
//            Set<String> queryTerms = optimizedEngineHashSet.processQuery(query);
//            finalImageIdsOptimizedHashSet = optimizedEngineHashSet.rankImages(resultImageIdsOptimizedHashSet, queryTerms);
//        }
//
//        for (String imageId : finalImageIdsOptimizedHashSet) {
//            System.out.println(imageId);
//        }
//
//        System.out.println();
//
//        // ============================
//        // Optimized Implementation with ArrayList
//        // ============================
//        System.out.println("Optimized Inverted Index using ArrayLists for images Results");
//        OptimizedImageSearchEngineArrayList optimizedEngineArrayList = new OptimizedImageSearchEngineArrayList(invertedIndexFilePath, imageTagsMapFilePath);
//
//        startTime = System.nanoTime();
//        List<String> resultImageIdsOptimizedArrayList = optimizedEngineArrayList.searchImages(query, isAnd);
//        endTime = System.nanoTime();
//        long responseTimeOptimizedArrayList = (endTime - startTime) / 1_000_000;
//
//        int imagesFoundOptimizedArrayList = resultImageIdsOptimizedArrayList.size();
//
//        System.out.println("Search results for query: " + query);
//        System.out.println("Response Time: " + responseTimeOptimizedArrayList + "ms");
//        System.out.println("Images Found: " + imagesFoundOptimizedArrayList);
//        System.out.println();
//
//        List<String> finalImageIdsOptimizedArrayList;
//        if (isAnd) {
//            finalImageIdsOptimizedArrayList = resultImageIdsOptimizedArrayList;
//        } else {
//            Set<String> queryTerms = optimizedEngineArrayList.processQuery(query);
//            finalImageIdsOptimizedArrayList = optimizedEngineArrayList.rankImages(resultImageIdsOptimizedArrayList, queryTerms);
//        }
//
//        for (String imageId : finalImageIdsOptimizedArrayList) {
//            System.out.println(imageId);
//        }
//
//        scanner.close();
//    }
//}

//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.*;
//import java.lang.reflect.Type;
//import java.util.*;
//
//public class Main {
//    public static void main(String[] args) {
//        String invertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\invertedIndex.json";
//        String imageTagsMapFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";
//        String weightedInvertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\weightedInvertedIndex.json";
//
//
//        Scanner scanner = new Scanner(System.in);
//
//        // Get user input for query
//        System.out.print("Enter your search query: ");
//        String query = scanner.nextLine();
//
//        // Get user input for search type
//        System.out.print("Select search type (AND/OR): ");
//        String searchTypeInput = scanner.nextLine().trim().toUpperCase();
//        boolean isAnd = searchTypeInput.equals("AND");
//
//        System.out.println();
//
//        // ============================
//        // PSO Optimization
//        // ============================
//        // Check if weighted inverted index already exists; if not, run PSO
//        // For simplicity, we'll assume we need to run PSO every time
//
//        System.out.println("Optimizing weights using PSO...");
//        PSO pso = new PSO(loadImageTagsMap(imageTagsMapFilePath), 10, 50); // 10 particles, 50 iterations
//        pso.optimize();
//        System.out.println("PSO optimization completed.");
//
//        // Save weighted inverted index
//        saveWeightedInvertedIndex(weightedInvertedIndexFilePath, loadInvertedIndex(invertedIndexFilePath), pso.getOptimizedWeights());
//
//        // ============================
//        // Weighted Inverted Index Results
//        // ============================
//        System.out.println("Optimized Inverted Index using weights assigned to the images using PSO Results");
//        WeightedImageSearchEngine weightedEngine = new WeightedImageSearchEngine(weightedInvertedIndexFilePath, imageTagsMapFilePath);
//
//        long startTime = System.nanoTime();
//        List<String> resultImageIdsWeighted = weightedEngine.searchImages(query, isAnd);
//        long endTime = System.nanoTime();
//        long responseTimeWeighted = (endTime - startTime) / 1_000_000;
//
//        int imagesFoundWeighted = resultImageIdsWeighted.size();
//
//        System.out.println("Search results for query: " + query);
//        System.out.println("Response Time: " + responseTimeWeighted + "ms");
//        System.out.println("Images Found: " + imagesFoundWeighted);
//        System.out.println();
//
//        for (String imageId : resultImageIdsWeighted) {
//            System.out.println(imageId);
//        }
//
//        // Close scanner
//        scanner.close();
//    }
//
//    private static Map<String, List<String>> loadInvertedIndex(String filePath) {
//        Map<String, List<String>> invertedIndex = new HashMap<>();
//        try (Reader reader = new FileReader(filePath)) {
//            Gson gson = new Gson();
//            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
//            invertedIndex = gson.fromJson(reader, type);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return invertedIndex;
//    }
//
//    private static Map<String, List<String>> loadImageTagsMap(String filePath) {
//        Map<String, List<String>> imageTagsMap = new HashMap<>();
//        try (Reader reader = new FileReader(filePath)) {
//            Gson gson = new Gson();
//            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
//            imageTagsMap = gson.fromJson(reader, type);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return imageTagsMap;
//    }
//
//    // Update the saveWeightedInvertedIndex method
//    private static void saveWeightedInvertedIndex(String filePath, Map<String, List<String>> invertedIndex, Map<String, Double> optimizedWeights) {
//        Map<String, List<ImageWeight>> weightedIndex = new HashMap<>();
//
//        for (String tag : invertedIndex.keySet()) {
//            List<ImageWeight> imageWeights = new ArrayList<>();
//            for (String imageId : invertedIndex.get(tag)) {
//                double weight = optimizedWeights.getOrDefault(imageId, 0.0);
//                imageWeights.add(new ImageWeight(imageId, weight));
//            }
//            weightedIndex.put(tag, imageWeights);
//        }
//
//        try (Writer writer = new FileWriter(filePath)) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            gson.toJson(weightedIndex, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//


import java.lang.reflect.Type;
import java.util.*;
import java.io.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Main {
    public static void main(String[] args) {
        String invertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\invertedIndex.json";
        String imageTagsMapFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";
        String weightedInvertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\weightedInvertedIndex.json";

        Scanner scanner = new Scanner(System.in);

        // Get user input for query
        System.out.print("Enter your search query: ");
        String query = scanner.nextLine();

        // Get user input for search type
        System.out.print("Select search type (AND/OR): ");
        String searchTypeInput = scanner.nextLine().trim().toUpperCase();
        boolean isAnd = searchTypeInput.equals("AND");

        System.out.println();

        // ============================
        // Unoptimized Implementation
        // ============================
        System.out.println("Unoptimized Inverted Index using JSON object Results");
        UnoptimizedImageSearchEngine unoptimizedEngine = new UnoptimizedImageSearchEngine(invertedIndexFilePath, imageTagsMapFilePath);

        long startTime = System.nanoTime();
        Set<String> resultImageIdsUnoptimized = unoptimizedEngine.searchImages(query, isAnd);
        long endTime = System.nanoTime();
        long responseTimeUnoptimized = (endTime - startTime) / 1_000_000;

        int imagesFoundUnoptimized = resultImageIdsUnoptimized.size();

        System.out.println("Search results for query: " + query);
        System.out.println("Response Time: " + responseTimeUnoptimized + "ms");
        System.out.println("Images Found: " + imagesFoundUnoptimized);
        System.out.println();

        List<String> finalImageIdsUnoptimized = unoptimizedEngine.rankImages(resultImageIdsUnoptimized, unoptimizedEngine.processQuery(query));

        for (String imageId : finalImageIdsUnoptimized) {
            System.out.println(imageId);
        }

        System.out.println();

        // ============================
        // Optimized Implementation with HashSet
        // ============================
        System.out.println("Optimized Inverted Index using HashSets for images Results");
        OptimizedImageSearchEngineHashSet optimizedEngineHashSet = new OptimizedImageSearchEngineHashSet(invertedIndexFilePath, imageTagsMapFilePath);

        startTime = System.nanoTime();
        List<String> resultImageIdsOptimizedHashSet = optimizedEngineHashSet.searchImages(query, isAnd);
        endTime = System.nanoTime();
        long responseTimeOptimizedHashSet = (endTime - startTime) / 1_000_000;

        int imagesFoundOptimizedHashSet = resultImageIdsOptimizedHashSet.size();

        System.out.println("Search results for query: " + query);
        System.out.println("Response Time: " + responseTimeOptimizedHashSet + "ms");
        System.out.println("Images Found: " + imagesFoundOptimizedHashSet);
        System.out.println();

        List<String> finalImageIdsOptimizedHashSet;
        if (isAnd) {
            finalImageIdsOptimizedHashSet = resultImageIdsOptimizedHashSet;
        } else {
            Set<String> queryTerms = optimizedEngineHashSet.processQuery(query);
            finalImageIdsOptimizedHashSet = optimizedEngineHashSet.rankImages(resultImageIdsOptimizedHashSet, queryTerms);
        }

        for (String imageId : finalImageIdsOptimizedHashSet) {
            System.out.println(imageId);
        }

        System.out.println();

        // ============================
        // Optimized Implementation with ArrayList
        // ============================
        System.out.println("Optimized Inverted Index using ArrayLists for images Results");
        OptimizedImageSearchEngineArrayList optimizedEngineArrayList = new OptimizedImageSearchEngineArrayList(invertedIndexFilePath, imageTagsMapFilePath);

        startTime = System.nanoTime();
        List<String> resultImageIdsOptimizedArrayList = optimizedEngineArrayList.searchImages(query, isAnd);
        endTime = System.nanoTime();
        long responseTimeOptimizedArrayList = (endTime - startTime) / 1_000_000;

        int imagesFoundOptimizedArrayList = resultImageIdsOptimizedArrayList.size();

        System.out.println("Search results for query: " + query);
        System.out.println("Response Time: " + responseTimeOptimizedArrayList + "ms");
        System.out.println("Images Found: " + imagesFoundOptimizedArrayList);
        System.out.println();

        List<String> finalImageIdsOptimizedArrayList;
        if (isAnd) {
            finalImageIdsOptimizedArrayList = resultImageIdsOptimizedArrayList;
        } else {
            Set<String> queryTerms = optimizedEngineArrayList.processQuery(query);
            finalImageIdsOptimizedArrayList = optimizedEngineArrayList.rankImages(resultImageIdsOptimizedArrayList, queryTerms);
        }

        for (String imageId : finalImageIdsOptimizedArrayList) {
            System.out.println(imageId);
        }

        System.out.println();

        // ============================
        // Weighted Inverted Index Results
        // ============================
        System.out.println("Optimized weighted Inverted Index using PSO Results");
        // Check if weighted inverted index exists
        File weightedIndexFile = new File(weightedInvertedIndexFilePath);
        if (!weightedIndexFile.exists()) {
            System.out.println("Weighted inverted index not found. Running PSO optimization...");
            PSO pso = new PSO(loadImageTagsMap(imageTagsMapFilePath), 10, 50); // Adjust parameters as needed
            pso.optimize();
            System.out.println("PSO optimization completed.");
            // Save weighted inverted index
            saveWeightedInvertedIndex(weightedInvertedIndexFilePath, loadInvertedIndex(invertedIndexFilePath), pso.getOptimizedWeights());
        } else {
            System.out.println("Weighted inverted index found. Skipping PSO optimization.");
        }

        WeightedImageSearchEngine weightedEngine = new WeightedImageSearchEngine(weightedInvertedIndexFilePath, imageTagsMapFilePath);

        startTime = System.nanoTime();
        List<String> resultImageIdsWeighted = weightedEngine.searchImages(query, isAnd);
        endTime = System.nanoTime();
        long responseTimeWeighted = (endTime - startTime) / 1_000_000;

        int imagesFoundWeighted = resultImageIdsWeighted.size();

        System.out.println("Search results for query: " + query);
        System.out.println("Response Time: " + responseTimeWeighted + "ms");
        System.out.println("Images Found: " + imagesFoundWeighted);
        System.out.println();

        for (String imageId : resultImageIdsWeighted) {
            System.out.println(imageId);
        }

        // Close scanner
        scanner.close();
    }

    // Method to load the inverted index
    private static Map<String, List<String>> loadInvertedIndex(String filePath) {
        Map<String, List<String>> invertedIndex = new HashMap<>();
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            invertedIndex = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invertedIndex;
    }

    // Method to load the image tags map
    private static Map<String, List<String>> loadImageTagsMap(String filePath) {
        Map<String, List<String>> imageTagsMap = new HashMap<>();
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            imageTagsMap = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageTagsMap;
    }

    // Method to save the weighted inverted index
    private static void saveWeightedInvertedIndex(String filePath, Map<String, List<String>> invertedIndex, Map<String, Double> optimizedWeights) {
        Map<String, List<ImageWeight>> weightedIndex = new HashMap<>();

        for (String tag : invertedIndex.keySet()) {
            List<ImageWeight> imageWeights = new ArrayList<>();
            for (String imageId : invertedIndex.get(tag)) {
                double weight = optimizedWeights.getOrDefault(imageId, 0.0);
                imageWeights.add(new ImageWeight(imageId, weight));
            }
            weightedIndex.put(tag, imageWeights);
        }

        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(weightedIndex, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
