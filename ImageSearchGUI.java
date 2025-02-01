import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ImageSearchGUI extends Application {
    private WeightedImageSearchEngine searchEngine;
    private TextField queryField;
    private ComboBox<String> searchTypeComboBox;
    private FlowPane imageFlowPane;
    private String imageFolderPath;
    private BorderPane mainPane;
    private ProgressIndicator progressIndicator;
    private Scene scene;
    private Label statusLabel;
    private static final String STYLE_SHEET = "style.css";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Adjust the paths according to your directory structure
        String weightedInvertedIndexFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\weightedInvertedIndex.json";
        String imageTagsMapFilePath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\resultData.json";
        imageFolderPath = "C:\\Users\\pansh\\OneDrive\\Documents\\Programs\\Java\\Minor Project - 1\\flickr30k_images\\flickr30k_images"; // Directory containing the images

        // Check if weighted inverted index exists
        File weightedIndexFile = new File(weightedInvertedIndexFilePath);
        if (!weightedIndexFile.exists()) {
            showAlert(Alert.AlertType.ERROR, "Weighted inverted index not found. Please run the PSO optimization first.");
            return;
        }

        searchEngine = new WeightedImageSearchEngine(weightedInvertedIndexFilePath, imageTagsMapFilePath);

        primaryStage.setTitle("Image Search Engine");

        mainPane = new BorderPane();
        mainPane.setPadding(new Insets(0, 20, 10, 20));

        // Header with application title
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 10, 0));
        header.setSpacing(10);

        Label titleLabel = new Label("Image Search");
        titleLabel.getStyleClass().add("title-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titleLabel, spacer);

        // Search bar panel
        VBox searchBarContainer = new VBox(10);
        searchBarContainer.setAlignment(Pos.CENTER);
        searchBarContainer.setPadding(new Insets(0, 0, 20, 0));

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER);

        queryField = new TextField();
        queryField.setPromptText("Search images...");
        queryField.setFont(Font.font("Segoe UI", 16));
        queryField.setPrefWidth(500);
        queryField.getStyleClass().add("search-field");

        Button searchButton = new Button("Search");
        searchButton.setFont(Font.font("Segoe UI", 16));
        searchButton.getStyleClass().add("search-button");

        searchBar.getChildren().addAll(queryField, searchButton);

        // Search options (AND / OR)
        HBox optionsBar = new HBox(10);
        optionsBar.setAlignment(Pos.CENTER);

        String[] searchOptions = {"AND Search", "OR Search"};
        searchTypeComboBox = new ComboBox<>();
        searchTypeComboBox.getItems().addAll(searchOptions);
        searchTypeComboBox.getSelectionModel().selectFirst();
        searchTypeComboBox.getStyleClass().add("search-options");

        optionsBar.getChildren().add(searchTypeComboBox);

        searchBarContainer.getChildren().addAll(searchBar, optionsBar);

        // Status label
        statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");

        // Center panel to display images
        imageFlowPane = new FlowPane();
        imageFlowPane.setPadding(new Insets(15, 0, 15, 0));
        imageFlowPane.setHgap(15);
        imageFlowPane.setVgap(15);
        imageFlowPane.setAlignment(Pos.CENTER);
        imageFlowPane.setPrefWrapLength(800); // Initial width, adjusts with window size

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(imageFlowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("image-scroll-pane");

        // Progress indicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);

        StackPane centerPane = new StackPane();
        centerPane.getChildren().addAll(scrollPane, progressIndicator);

        // Event handlers
        searchButton.setOnAction(e -> performSearch());
        queryField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });

        // Responsive design: adjust wrap length based on window width
        mainPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue() - 40; // Adjust for padding
            imageFlowPane.setPrefWrapLength(width);
        });

        // Assemble the main layout
        VBox topContainer = new VBox(header, searchBarContainer);
        mainPane.setTop(topContainer);
        mainPane.setCenter(centerPane);
        mainPane.setBottom(statusLabel);
        BorderPane.setAlignment(statusLabel, Pos.CENTER);

        scene = new Scene(mainPane, 1200, 800);
        // Apply CSS styling for modern UI
        scene.getStylesheets().add(getClass().getResource(STYLE_SHEET).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void performSearch() {
        String query = queryField.getText().trim();
        if (query.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter a search query.");
            return;
        }

        boolean isAndSearch = searchTypeComboBox.getSelectionModel().getSelectedIndex() == 0;

        // Show progress indicator
        progressIndicator.setVisible(true);
        statusLabel.setText("Searching...");

        // Run search in background to avoid blocking UI
        new Thread(() -> {
            try {
                // Perform search
                List<String> resultImageIds = searchEngine.searchImages(query, isAndSearch);

                // Limit the number of images to display
                int maxImagesToDisplay = 1000; // Adjust as needed
                List<String> limitedImageIds = resultImageIds.size() > maxImagesToDisplay ?
                        resultImageIds.subList(0, maxImagesToDisplay) : resultImageIds;

                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    // Hide progress indicator
                    progressIndicator.setVisible(false);

                    // Display images with animation
                    imageFlowPane.getChildren().clear();

                    if (limitedImageIds.isEmpty()) {
                        statusLabel.setText("No images found for the query.");
                    } else {
                        statusLabel.setText(limitedImageIds.size() + " images found.");
                        for (String imageId : limitedImageIds) {
                            String imagePath = imageFolderPath + File.separator + imageId;
                            File imageFile = new File(imagePath);
                            if (imageFile.exists()) {
                                try {
                                    // Create Image for the thumbnail with background loading
                                    Image thumbnailImage = new Image(imageFile.toURI().toString(), 250, 0, true, true, true);

                                    // Create ImageView for the thumbnail
                                    ImageView thumbnailImageView = new ImageView(thumbnailImage);
                                    thumbnailImageView.setPreserveRatio(true);
                                    thumbnailImageView.setFitWidth(250);
                                    thumbnailImageView.getStyleClass().add("image-view");

                                    // Create VBox to hold the image and label
                                    VBox imageContainer = new VBox();
                                    imageContainer.setAlignment(Pos.CENTER);
                                    imageContainer.setSpacing(5);
                                    imageContainer.getStyleClass().add("image-container");

                                    // Optional: Label below the image (e.g., image ID)
                                    Label imageLabel = new Label(imageId);
                                    imageLabel.getStyleClass().add("image-label");

                                    imageContainer.getChildren().addAll(thumbnailImageView, imageLabel);

                                    // Add to flow pane with fade-in animation
                                    imageFlowPane.getChildren().add(imageContainer);
                                    FadeTransition ft = new FadeTransition(Duration.millis(500), imageContainer);
                                    ft.setFromValue(0);
                                    ft.setToValue(1);
                                    ft.play();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                System.err.println("Image file not found: " + imagePath);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    statusLabel.setText("An error occurred during the search.");
                    showAlert(Alert.AlertType.ERROR, "An error occurred while searching. Please try again.");
                });
            }
        }).start();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.showAndWait();
    }
}
