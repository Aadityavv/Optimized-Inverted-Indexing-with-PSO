//import java.io.*;
//import java.util.*;
//import java.lang.reflect.Type;
//import com.google.gson.*;
//import com.google.gson.reflect.TypeToken;
//
//
//public class InvertedIndexBuilder {
//
//    public static void main(String[] args) {
//        String jsonFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";
//
//        // Step 1: Read the JSON file into a Map<String, Set<String>>
//        Map<String, Set<String>> imageTagsMap = readImageTagsMap(jsonFilePath);
//
//        // Step 2: Build the inverted index
//        Map<String, Set<String>> invertedIndex = buildInvertedIndex(imageTagsMap);
//
//        // (Optional) Step 3: Save the inverted index to a file
//        String invertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\invertedIndex.json";
//        saveInvertedIndex(invertedIndex, invertedIndexFilePath);
//
//        // The inverted index is now ready for use
//        System.out.println("Inverted index built and saved successfully.");
//    }
//
//    private static Map<String, Set<String>> readImageTagsMap(String jsonFilePath) {
//        Map<String, Set<String>> imageTagsMap = new HashMap<>();
//        try (Reader reader = new FileReader(jsonFilePath)) {
//            Gson gson = new Gson();
//            Type type = new TypeToken<Map<String, Set<String>>>() {}.getType();
//            imageTagsMap = gson.fromJson(reader, type);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return imageTagsMap;
//    }
//
//    private static Map<String, Set<String>> buildInvertedIndex(Map<String, Set<String>> imageTagsMap) {
//        Map<String, Set<String>> invertedIndex = new HashMap<>();
//        for (Map.Entry<String, Set<String>> entry : imageTagsMap.entrySet()) {
//            String imageId = entry.getKey();
//            Set<String> tags = entry.getValue();
//            for (String tag : tags) {
//                invertedIndex.computeIfAbsent(tag, k -> new HashSet<>()).add(imageId);
//            }
//        }
//        return invertedIndex;
//    }
//
//    private static void saveInvertedIndex(Map<String, Set<String>> invertedIndex, String filePath) {
//        try (Writer writer = new FileWriter(filePath)) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            gson.toJson(invertedIndex, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class InvertedIndexBuilder {

    public static void main(String[] args) {
        String jsonFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";

        // Step 1: Read the JSON file into a Map<String, List<String>>
        Map<String, List<String>> imageTagsMap = readImageTagsMap(jsonFilePath);

        // Step 2: Build the inverted index
        Map<String, List<String>> invertedIndex = buildInvertedIndex(imageTagsMap);

        // Step 3: Save the inverted index to a file
        String invertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\invertedIndex.json";
        saveInvertedIndex(invertedIndex, invertedIndexFilePath);

        System.out.println("Inverted index built and saved successfully.");
    }

    private static Map<String, List<String>> readImageTagsMap(String jsonFilePath) {
        Map<String, List<String>> imageTagsMap = new HashMap<>();
        try (Reader reader = new FileReader(jsonFilePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            imageTagsMap = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageTagsMap;
    }

    private static Map<String, List<String>> buildInvertedIndex(Map<String, List<String>> imageTagsMap) {
        Map<String, List<String>> invertedIndex = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : imageTagsMap.entrySet()) {
            String imageId = entry.getKey();
            List<String> tags = entry.getValue();
            for (String tag : tags) {
                invertedIndex.computeIfAbsent(tag, k -> new ArrayList<>()).add(imageId);
            }
        }
        return invertedIndex;
    }

    private static void saveInvertedIndex(Map<String, List<String>> invertedIndex, String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(invertedIndex, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
