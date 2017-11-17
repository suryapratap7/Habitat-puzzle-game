package comp1110.ass1.gui;

import comp1110.ass1.Habitat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * This is a JavaFX application that gives a graphical user interface (GUI) to the
 * simple habitat game.
 *
 * The tasks set for assignment one do NOT require you to refer to this class, so...
 *
 *    YOU MAY IGNORE THE CODE HERE ENTIRELY
 *
 * ...while you do assignment one.
 *
 * However, the class serves as a working example of a number of JavaFX concepts
 * that you may need later in the semester, so you may find this code helpful
 * later in the semester.
 *
 * Among other things, the class demonstrates:
 *   - Using inner classes that subclass standard JavaFX classes such as ImageView
 *   - Using JavaFX groups to control properties such as visibility and lifetime of
 *     a collection of objects
 *   - Using opacity/transparency
 *   - Using mouse events to implement a draggable object
 *   - Making dropped objects snap to legal destinations
 *   - Using a clickable button with an associated event
 *   - Using a slider for user-input
 *   - Using keyboard events to implement toggles controlled by the player
 *   - Using bitmap images (public domain, CC0)
 *   - Using an mp3 audio track (public domain, CC0)
 *   - Using IllegalArgumentExceptions to check for and flag errors
 */
public class Board extends Application {

    /* board layout */
    private static final int ROWS = 6;
    private static final int COLS = 10;
    private static final int MARGIN = 50;
    private static final int SQUARE_SIZE = 100;
    private static final int GAME_WIDTH = COLS * SQUARE_SIZE;
    private static final int GAME_HEIGHT = ROWS * SQUARE_SIZE;
    private static final int BOARD_X = MARGIN * 2 + SQUARE_SIZE * 2;
    private static final int BOARD_Y = SQUARE_SIZE;

    /* where to find media assets */
    private static final String URI_BASE = "assets/";

    /* Loop in public domain CC 0 http://www.freesound.org/people/oceanictrancer/sounds/211684/ */
    private static final String LOOP_URI = Board.class.getResource(URI_BASE + "211684__oceanictrancer__classic-house-loop-128-bpm.wav").toString();
    private AudioClip loop;

    /* game variables */
    private boolean loopPlaying = false;

    /* node groups */
    private final Group root = new Group();
    private final Group pieces = new Group();
    private final Group solution = new Group();
    private final Group hab = new Group();
    private final Group controls = new Group();

    /* the difficulty slider */
    private final Slider difficulty = new Slider();

    /* the grid of skulls */
    private Skull[] skull = new Skull[Habitat.PLACES];

    /* message on completion */
    private final Text competionText = new Text("Well done!");

    /* the habitat game */
    Habitat habitat;


    /**
     * An inner class that represents a square of the habitat.  The class extends
     * Java FX's ImageView class (used for displaying bitmap images).
     */
    class Square extends ImageView {
        /**
         * Construct a particular square at a given position
         * @param id A character representing the type of square to be created.
         * @param pos An integer reflecting the position on the grid (0 .. 15)
         */
        Square(char id, int pos) {
            if (pos < 0 || pos >= Habitat.PLACES) {
                throw new IllegalArgumentException("Bad tile position: " + pos);
            }
            setImage(new Image(Board.class.getResource(URI_BASE + id + ".png").toString()));
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            setLayoutX(BOARD_X + (pos % Habitat.SIDE) * SQUARE_SIZE);
            setLayoutY(BOARD_Y + (pos / Habitat.SIDE) * SQUARE_SIZE);
        }
    }



    /**
     * A simple inner class that extends Square, for the skull images
     */
    class Skull extends Square {
        /**
         * Construct a skull at a particular position
         * @param pos An integer reflecting the position on the grid (0 .. 15)
         */
        Skull(int pos) {
            super('X', pos);
        }
    }



    /**
     * A simple inner class that extends Square, for the three diffrent
     * kinds of place (predator, land, and water).
     */
    class Place extends Square {
        /**
         * Construct a place at a particular position.
         * @param place  A character representing the kind of place to be created
         * @param pos An integer reflecting the position on the grid (0 .. 15)
         */
        Place(char place, int pos) {
            super(place, pos);
            if (!(place == 'P' || place == 'L' || place == 'W')) {
                throw new IllegalArgumentException("Bad hab place: \"" + place + "\"");
            }
        }
    }



    /**
     * An inner class that represents transparent pieces used in the game.
     * Each of these is a visual representaton of an underlying piece.
     */
    class FXPiece extends ImageView {
        char piece;

        /**
         * Construct a particular playing piece
         * @param piece The letter representing the piece to be created.
         */
        FXPiece(char piece) {
            if (!(piece >= 'Q' && piece <= 'V')) {
                throw new IllegalArgumentException("Bad piece: \"" + piece + "\"");
            }
            /*
             * Images used for game are all in the public domain, CC 0
             *  https://pixabay.com/en/animal-1299834_1280.png
             *  https://pixabay.com/en/bones-1295805_1280.png
             *  https://pixabay.com/en/cats-30746_1280.png
             *  https://pixabay.com/en/fish-260709_1280.png
             *  https://pixabay.com/en/cartoon-style-1293997_1280.png
             *  https://pixabay.com/en/sheep-183057_1280.png
             */
            setImage(new Image(Board.class.getResource(URI_BASE + piece + ".png").toString()));
            this.piece = piece;
            setFitHeight(SQUARE_SIZE * 2);
            setFitWidth(SQUARE_SIZE * 2);
        }

        /**
         * Construct a particular playing piece at a particular place on the
         * board at a given orientation.
         * @param position A three-character string describing
         *                 - the place the piece is to be located ('A' - 'P'),
         *                 - the piece ('R' - 'V'), and
         *                 - the orientatiojn ('W' - 'Z')
         */
        FXPiece(String position) {
            this(position.charAt(1));
            if (position.length() != 3 ||
                    position.charAt(0) < 'A' || position.charAt(0) > 'P' ||
                    position.charAt(2) < 'W' || position.charAt(2) > 'Z') {
                throw new IllegalArgumentException("Bad position string: " + position);
            }
            int pos = position.charAt(0) - 'A';
            int o = (position.charAt(2) - 'W');
            int x = (pos % Habitat.SIDE) - (((o + 1) / 2) % 2);
            int y = (pos / Habitat.SIDE) - (o / 2);
            setLayoutX(BOARD_X + x * SQUARE_SIZE);
            setLayoutY(BOARD_Y + y * SQUARE_SIZE);
            setRotate(90 * o);
        }
    }



    /**
     * This class extends FXPiece with the capacity for it to be dragged and dropped,
     * and snap-to-grid.
     */
    class DraggableFXPiece extends FXPiece {
        int position;               // the current game position of the piece 0 .. 15 (-1 is off-board)
        int homeX, homeY;           // the position in the window where the piece should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)

        /**
         * Construct a draggable piece
         * @param piece The piece identifier ('Q' - 'V')
         */
        DraggableFXPiece(char piece) {
            super(piece);
            position = -1; // off screen
            homeX = MARGIN + (SQUARE_SIZE * (((piece - 'Q') % 2) * 7));
            setLayoutX(homeX);
            homeY = MARGIN + (SQUARE_SIZE * (2 * ((piece - 'Q') / 2)));
            setLayoutY(homeY);

            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                hideSkulls();
                hideCompletion();
                rotate();
                event.consume();
            });
            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });
            setOnMouseDragged(event -> {      // mouse is being dragged
                hideCompletion();
                hideSkulls();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                snapToGrid();
            });
        }


        /**
         * Snap the piece to the nearest grid position (if it is over the grid)
         */
        private void snapToGrid() {
            setLayoutX(SQUARE_SIZE * (((int) getLayoutX() + (SQUARE_SIZE / 2)) / SQUARE_SIZE));
            setLayoutY(SQUARE_SIZE * (((int) getLayoutY() + (SQUARE_SIZE / 2)) / SQUARE_SIZE));
            setPosition();
            if (position != -1) {
                checkMove();
            } else {
                snapToHome();
            }
        }


        /**
         * Snap the piece to its home position (if it is not on the grid)
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
            setRotate(0);
            position = -1;
        }


        /**
         * Rotate the piece by 90 degrees
         */
        private void rotate() {
            setRotate((getRotate() + 90) % 360);
            setPosition();
            checkMove();
        }


        /**
         * Determine whether the whole piece is on the board, given x and y
         * coordinates representing the top-left corner of the piece in its
         * current rotation.
         * @param x The column that the origin of the piece is on
         * @param y The row that the origin of the piece is on
         * @return True if the entire piece is on the board
         */
        private boolean isOnBoard(int x, int y) {
            if (piece < 'U')  // 'L'-shaped pieces are simple, because they're basically square
                return x >= 0 && x < 3 && y >= 0 && y < 3;
            else {            // For 'I'-shaped pieces it depends on the orientation
                switch ((int) getRotate()) {
                    case 0:
                        return x >= 0  && x < 3 && y >= 0  && y < 4;
                    case 90:
                        return x >= -1 && x < 3 && y >= 0  && y < 3;
                    case 180:
                        return x >= 0  && x < 3 && y >= -1 && y < 3;
                    case 270: default:
                        return x >= 0  && x < 4 && y >= 0  && y < 3;
                }
            }
        }


        /**
         * Determine the grid-position of the origin of the piece (0 .. 15)
         * or -1 if it is off the grid, taking into account its rotation.
         */
        private void setPosition() {
            int x = (int) (getLayoutX() - BOARD_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() - BOARD_Y) / SQUARE_SIZE;
            if (isOnBoard(x,y)) {
                /*  find 'position' (reference point is top left of *un*rotated piece */
                int rotate = (int) getRotate() / 90;
                x += (rotate == 0 || rotate == 3) ? 0 : 1;
                y += rotate / 2;
                position = x + y * Habitat.SIDE;
            } else
                position = -1;
        }


        /** Represent the piece placement as a string */
        public String toString() {
            char orientation = (char) ('W' + (int) (getRotate()/ 90));
            return position == -1 ? "" : "" + (char)('A'+position) + piece + orientation;
        }
    }


    /**
     * A move has been made.  Determine whether there are errors,
     * and if so, show skulls, and determine whether the game is
     * complete, and if so, show the completion message.
     */
    private void checkMove() {
        String placement = "";
        for(Node p : pieces.getChildren()) {
            placement += p.toString();
        }

        if (!habitat.isPlacementSound(placement)) {
            showSkulls(habitat.getPlacementErrors(placement));
        } else {
            if (habitat.isPlacementComplete(placement)) {
                showCompletion();
            }
        }
    }


    /**
     * Set up event handlers for the main game
     *
     * @param scene  The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M) {
                toggleSoundLoop();
                event.consume();
            } else if (event.getCode() == KeyCode.Q) {
                Platform.exit();
                event.consume();
            } else if (event.getCode() == KeyCode.SLASH) {
                solution.setOpacity(1.0);
                event.consume();
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SLASH) {
                solution.setOpacity(0);
                event.consume();
            }
        });
    }


    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */
    private void setUpSoundLoop() {
        try {
            loop = new AudioClip(LOOP_URI);
            loop.setCycleCount(AudioClip.INDEFINITE);
        } catch (Exception e) {
            System.err.println(":-( something bad happened ("+LOOP_URI+"): "+e);
        }
    }


    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }


    /**
     * Set up the group that represents the solution (and make it transparent)
     * @param solution The solution string.
     */
    private void makeSolution(String solution) {
        this.solution.getChildren().clear();
        if (solution.length() != (comp1110.ass1.Piece.values().length * 3)) {
            throw new IllegalArgumentException("Solution incorrect length: " + solution);
        }
        for (int i = 0; i < solution.length() / 3; i++) {
            this.solution.getChildren().add(new FXPiece(solution.substring(i * 3, (i + 1) * 3)));
        }
        this.solution.setOpacity(0);
    }


    /**
     * Set up the group that represents the places that make the habitat
     * @param h The habitat string
     */
    private void makeHabitat(String h) {
        hab.getChildren().clear();
        if (h.length() != Habitat.PLACES) {
            throw new IllegalArgumentException("Habitat incorrect length: " + h);
        }
        for (int i = 0; i < Habitat.PLACES; i++) {
            hab.getChildren().add(new Place(h.charAt(i), i));
        }
        hab.toBack();
    }


    /**
     * Set up each of the six pieces
     */
    private void makePieces() {
        pieces.getChildren().clear();
        for (char p = 'Q'; p < 'W'; p++) {
            pieces.getChildren().add(new DraggableFXPiece(p));
        }
    }


    /**
     * Put all of the pieces back in their home position
     */
    private void resetPieces() {
        pieces.toFront();
        for (Node n : pieces.getChildren()) {
            ((DraggableFXPiece) n).snapToHome();
        }
    }


    /**
     * Set up skulls, one in each habitat square, and make the transparent.
     * They will be made visible when errors occur at the respective square.
     */
    private void makeSkulls() {
        for (int i = 0; i < Habitat.PLACES; i++) {
            skull[i] = new Skull(i);
            skull[i].setOpacity(0);
            root.getChildren().add(skull[i]);
        }
    }


    /**
     * Reveal the skull at each point in the habitat where an error
     * has occured.
     * @param errors  An array of booleans representing the errors
     */
    private void showSkulls(boolean[] errors) {
        for (int i = 0; i < errors.length; i++) {
            if (errors[i]) {
                skull[i].setOpacity(1);
                skull[i].toFront();
            } else {
                skull[i].setOpacity(0);
                skull[i].toBack();
            }
        }
    }


    /**
     * Hide all of the skulls
     */
    private void hideSkulls() {
        for (int i = 0; i < skull.length; i++)
            skull[i].setOpacity(0);
    }


    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {
        Button button = new Button("Restart");
        button.setLayoutX(6*SQUARE_SIZE);
        button.setLayoutY(5.5*SQUARE_SIZE);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                newGame();
            }
        });
        controls.getChildren().add(button);

        difficulty.setMin(0);
        difficulty.setMax(10);
        difficulty.setValue(0);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(5);
        difficulty.setMinorTickCount(1);
        difficulty.setSnapToTicks(true);

        difficulty.setLayoutX(4.25*SQUARE_SIZE);
        difficulty.setLayoutY(5.5*SQUARE_SIZE);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(3.25*SQUARE_SIZE);
        difficultyCaption.setLayoutY(5.5*SQUARE_SIZE);
        controls.getChildren().add(difficultyCaption);
    }


    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        competionText.setFill(Color.BLACK);
        competionText.setFont(Font.font("Arial", 80));
        competionText.setLayoutX(3.1*SQUARE_SIZE);
        competionText.setLayoutY(0.9*SQUARE_SIZE);
        competionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(competionText);
    }


    /**
     * Show the completion message
     */
    private void showCompletion() {
        competionText.toFront();
        competionText.setOpacity(1);
    }


    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        competionText.toBack();
        competionText.setOpacity(0);
    }


    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
            hideCompletion();
            hideSkulls();
            habitat = new Habitat(difficulty.getValue());
            makeHabitat(habitat.toString());
            makeSolution(habitat.getSolution());
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. "+ e);
            Thread.dumpStack();
            Platform.exit();
        }
        resetPieces();
    }



    /**
     * The entry point for JavaFX.  This method gets called when JavaFX starts
     * The key setup is all done by this method.
     *
     * @param primaryStage The stage (window) in which the game occurs.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Habitat");
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        root.getChildren().add(pieces);
        root.getChildren().add(hab);
        root.getChildren().add(solution);
        root.getChildren().add(controls);

        setUpHandlers(scene);
        setUpSoundLoop();
        makePieces();
        makeSkulls();
        makeControls();
        makeCompletion();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
