package tuppergraph;

/**
 *  A simple program based on Jeff Tupper's famous "Self Referential Formula".
 *
 *  Tupper's formula is as follows:
 *      1/2 < floor( mod( (floor( y/17 ) ) / ( 2^( 17 * floor(x) + mod( floor(y), 17 ) ) ), 2 ) )
 *
 *  This peculiar formula uses modulus and floor functions to partition the xy-plane into discrete "pixels",
 *  and when properly viewed, creates a column 106 pixels wide, separated into horizontal sections 17 pixels tall.
 *
 *  Each of these 106x17 "graphs" are unique, and Tupper's formula plots every
 *  possible graph of a 107x16 grid vertically stacked upon one another,
 *  including, famously, a graph of itself! Hence it's name,
 *
 *          At k =  9609393799189588849716729621278527547150043396601293066515055192717028023952664246
 *                  8964284217435071812126715378277062335599323728087414430789132596394133772348785773
 *                  5749823926629715517173716995165232890538221612403238855866184013235585136048828693
 *                  3379024914542292886670810961844960917051834540678277315517054053816273809676025656
 *                  2501698148208341878316384911559022561000365235137034387446184837873723819822484986
 *                  3465033159410054974700593138339226497249461751545728366702369745461014655997933798
 *                  537483143786841806593422227898388722980000748404719
 *
 *  units above the x-axis, it plots a picture it's own written formula.
 *
 *  Because each of the pixels are binary, each plot forms a bitmap, and can be represented as a 1802-bit number.
 *  The beautiful quality of the formula is that the vertical location of each plot is the same as the decimal
 *  form of that binary number! (x17 in this case, because the height is chosen as 17 pixels, but that number is arbitrary.)
 *
 *  But as mentioned earlier, it plots every possible combination of pixels. For example,
 *
 *             k =  9572393478691603843040696944304220558947757607414092789689772220117830826841543166
 *                  5535209576508084736204612754954116961002254402409340259476323624137461373882923726
 *                  4735576425373679971533973032124268228068867407562015911363723525520990362674427455
 *                  6006047285295032025837097171554390542172802380457775308894172711105716151439652229
 *                  9287116742601294311641116234329109236424988481630056351075709161772426019678867419
 *                  4958705021119177797059126793670648206069579380440109899749426051546876490046286687
 *                  535922657289945743360
 *  is my name!
 *
 *
 *
 *  This program takes an input number for 'k' (the vertical distance from the x-axis) and prints Tupper's
 *  formula from k to k + 17, or the "plot" at that location.
 *
 *  The graph is stored as an array, and JavaFX is used to make the basic GUI.
 *
 *  @author Aaron Anderson
 *          12/25/2017
 */


import java.math.BigInteger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class TupperGraph extends Application {
    // An array representation of a 106x17 graph, with 0 = empty and 1 = filled
    int[][] graphArray = new int[106][17];

    // Main method
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Creates master frame pane
        BorderPane frame = new BorderPane();

        // Creates input pane
        HBox input = new HBox();
        input.setPadding(new Insets(20));

        // Creates pane for input buttons
        VBox inputButtons = new VBox();
        inputButtons.setPadding(new Insets(5));
        inputButtons.setSpacing(5);
        Button plot = new Button("Plot");
        plot.setMinWidth(50);
        Button clear = new Button("Clear");
        clear.setMinWidth(50);
        inputButtons.getChildren().addAll(plot, clear);

        // Creates input text field, and initializes it to the "referential" value for 'k'.
        TextArea inputText = new TextArea();
        inputText.setMinWidth(1100);
        inputText.setWrapText(true);
        inputText.setPadding(new Insets(20));
        inputText.setText("960939379918958884971672962127852754715004339660129306651505519271702802" +
                "3952664246896428421743507181212671537827706233559932372808741443078913259639413377" +
                "2348785773574982392662971551717371699516523289053822161240323885586618401323558513" +
                "6048828693337902491454229288667081096184496091705183454067827731551705405381627380" +
                "9676025656250169814820834187831638491155902256100036523513703438744618483787372381" +
                "9822484986346503315941005497470059313833922649724946175154572836670236974546101465" +
                "5997933798537483143786841806593422227898388722980000748404719");

        // Populates input pane with input buttons and input text field
        input.getChildren().addAll(inputButtons, inputText);

        // Creates pane for graph
        GridPane graph = new GridPane();
        graph.setPadding(new Insets(20));

        // Populates frame with graph and input panes
        frame.setTop(input);
        frame.setCenter(graph);

        // Action for plot button: converts decimal number from 'inputText' to appropriate binary,
        // stores bitmap in 'graphArray', then plots to 'graph' pane.
        plot.setOnAction(e -> {
            String kBinary;
            BigInteger k = new BigInteger(inputText.getText());
            k = k.divide(new BigInteger("17"));
            kBinary = String.format("%1802s", k.toString(2)).replace(' ', '0');

            for (int i = 0; i < 106; i++) {
                for (int j = 0; j < 17; j++) {
                    graphArray[105 - i][16 - j] = kBinary.charAt(17 * i + j);
                }
            }
            print(graphArray, graph);
        });

        // clears input text field when 'clear' is pressed
        clear.setOnAction(e -> {
            inputText.clear();
        });


        Scene scene = new Scene(frame, 1200, 600);
        primaryStage.setTitle("TupperGraph");
        primaryStage.setScene(scene);
        primaryStage.show();
        print(graphArray, graph);
    }

    /** Prints graph to graph pane.
     *
     * @param graphArray: array representation of graph
     * @param graph: pane to print out
     */
    public void print(int[][] graphArray, GridPane graph) {
        /*  Loops through array and for prints a grey rectangle in the corresponding area of graph
         *  and for each '1', a white rectangle for each '0'.
         */
        for (int i = 0; i < 106; i++) {
            for (int j = 0; j < 17; j++) {
                Rectangle rect = new Rectangle(10, 10);
                rect.setStroke(Color.BLACK);

                if (graphArray[i][j] == '1') {
                    rect.setFill(Color.GREY);
                } else {
                    rect.setFill(Color.WHITE);
                }

                graph.add(rect, 106 - i, j);
            }
        }
    }

}