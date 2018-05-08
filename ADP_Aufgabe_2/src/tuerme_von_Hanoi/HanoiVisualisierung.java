package tuerme_von_Hanoi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HanoiVisualisierung extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Hanoi");
        primaryStage.setScene(scene);
        canvas = new Canvas(400, scene.getHeight() - 100);
        root.setCenter(canvas);
        sliderZustandIndex = new Slider();
        sliderZustandIndex.valueProperty().addListener(
                (ov, old_val, new_val) -> {
                    aktuellerZustand = new_val.intValue();
                    zeichneZustand();
                });
        VBox paneControl = new VBox();
        paneControl.getChildren().add(sliderZustandIndex);
        Button buttonPlayStop = new Button("\u25B6");
        buttonPlayStop.setOnAction(ereignis -> {
            if (timer != null) {
                timer.cancel();
                timer = null;
                buttonPlayStop.setText("\u25B6");
            } else {
                int delay = (int) (1.0 / zustaende.size() * 5000);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        aktuellerZustand = (aktuellerZustand < zustaende.size() - 1) ? aktuellerZustand + 1 : aktuellerZustand;
                        sliderZustandIndex.setValue(aktuellerZustand);
                        zeichneZustand();
                    }
                }, delay, delay);
                buttonPlayStop.setText("\u25A0");
            }
        });
        paneControl.getChildren().add(buttonPlayStop);
        root.setBottom(paneControl);

        // Hier in den setup()-Aufruf müssen Sie Ihren Algorithmus einbauen
        HanoiLoesung hanoi = new HanoiLoesung();
        int n = 7;
        setup(n, hanoi.hanoi(n));
        zeichneZustand();
        primaryStage.show();
    }

    /**
     * Liste der Zustände im Algorithmus
     */
    private List<HanoiZustand> zustaende = new ArrayList<HanoiZustand>();

    /**
     * Index des aktuell zu zeichnenden Zustands
     */
    private int aktuellerZustand = 0;

    /**
     * Rand auf der Zeichenfläche
     */
    private static final int ZEICHNEN_PADDING = 20;

    /**
     * Hoehe einer Scheibe
     */
    private static final int HOEHE_SCHEIBE = 20;

    /**
     * Zeichenfläche
     */
    private Canvas canvas;

    /**
     * Slider zur Auswahl des aktuellen Zustands
     */
    private Slider sliderZustandIndex;

    /**
     * Timer für die Animationen.
     */
    private Timer timer = null;

    private class Scheibe {
        public Scheibe(Color farbe, double skalierung) {
            this.farbe = farbe;
            this.skalierung = skalierung;
        }

        /**
         * Farbe der Scheibe
         */
        public Color farbe;

        /**
         * Skalierungsfaktor für die Scheibe (kleiner, je weiter oben)
         */
        public double skalierung;
    }

    /**
     * Innere Klasse, die einen Zustand in einem Hanoi-Durchlauf repräsentiert
     */
    private class HanoiZustand {
        /**
         * Der Zustand besteht aus drei Stapeln mit den den jeweiligen
         */
        public ArrayList<Scheibe>[] stapel = new ArrayList[]{
                new ArrayList<Scheibe>(), new ArrayList<Scheibe>(), new ArrayList<Scheibe>()};

        public HanoiZustand() {
        }

        public HanoiZustand(HanoiZustand hanoiZustand) {
            hanoiZustand.stapel[0].forEach(scheibe -> stapel[0].add(scheibe));
            hanoiZustand.stapel[1].forEach(scheibe -> stapel[1].add(scheibe));
            hanoiZustand.stapel[2].forEach(scheibe -> stapel[2].add(scheibe));
        }

        /**
         * Generiert aus einem Zustand und einem Zug seinen Nachfolgezustand.
         */
        public HanoiZustand schritt(Paar<Integer> zug) {
            HanoiZustand nachfolgeZustand = new HanoiZustand(this);
            int letzerIndexInQuellenstapel = nachfolgeZustand.stapel[zug.getErstes()].size() - 1;
            nachfolgeZustand.stapel[zug.getZweites()].add(nachfolgeZustand.stapel[zug.getErstes()].get(letzerIndexInQuellenstapel));
            nachfolgeZustand.stapel[zug.getErstes()].remove(letzerIndexInQuellenstapel);
            return nachfolgeZustand;
        }
    }

    /**
     * Zeichnet den aktuellen Zustand
     */
    private void zeichneZustand() {
        if (aktuellerZustand < 0 || aktuellerZustand >= zustaende.size()) {
            throw new IllegalArgumentException("Ungültiger Zustandsindex");
        }
        int stapelbreite = (int) ((canvas.getWidth() - 2 * ZEICHNEN_PADDING) / 3.0);
        int scheibenbreite = (int) (stapelbreite * 0.9);
        HanoiZustand zustand = zustaende.get(aktuellerZustand);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int stapelIndex = 0; stapelIndex < 3; stapelIndex++) {
            for (int scheibenIndex = 0; scheibenIndex < zustand.stapel[stapelIndex].size(); scheibenIndex++) {
                zeichneScheibe(gc, zustand.stapel[stapelIndex].get(scheibenIndex),
                        ZEICHNEN_PADDING + stapelIndex * stapelbreite + stapelbreite / 2,
                        (int) (canvas.getHeight() - (ZEICHNEN_PADDING + HOEHE_SCHEIBE * scheibenIndex)), scheibenbreite);
            }
        }
    }

    /**
     * Zeichnet eine Scheibe an der angegebenen Position auf der Zeichenfläche
     */
    private void zeichneScheibe(GraphicsContext gc, Scheibe scheibe, int x, int y, int scheibenbreite) {
        scheibenbreite = (int) (scheibenbreite * scheibe.skalierung);
        gc.setFill(scheibe.farbe);
        gc.setStroke(Color.BLACK);
        gc.fillRect(x - scheibenbreite / 2, y, scheibenbreite, HOEHE_SCHEIBE - 1);
        gc.strokeRect(x - scheibenbreite / 2, y, scheibenbreite, HOEHE_SCHEIBE - 1);
    }

    /**
     * Hier wird die innere Datenstruktur aufgebaut, die zur Visualisierung verwendet wird.
     *
     * @param anzahlScheiben Anzahl der Scheiben auf dem ersten Stapel zu Beginn.
     * @param hanoiZuege     Liste der Züge, um alle Scheiben vom ersten zum dritten Stapel zu bringen.
     */
    private void setup(int anzahlScheiben, List<Paar<Integer>> hanoiZuege) {
        if (anzahlScheiben < 0) {
            throw new IllegalArgumentException("Anzahl Scheiben muss > 0 sein.");
        }
        if (anzahlScheiben > 7) {
            throw new IllegalArgumentException("Visualisierung ist auf maximal 7 Scheiben ausgelegt.");
        }

        // Erzeuge des Startzustand
        HanoiZustand startZustand = new HanoiZustand();
        double skalierungsfaktor = 0.5 / anzahlScheiben;
        for (int n = 0; n < anzahlScheiben; n++) {
            startZustand.stapel[0].add(new Scheibe(Color.color(Math.random(), Math.random(), Math.random()), 1 - n * skalierungsfaktor));
        }
        zustaende.add(startZustand);
        // Generiere folgende Zustände
        for (int i = 0; i < hanoiZuege.size(); i++) {
            Paar<Integer> zug = hanoiZuege.get(i);
            zustaende.add(zustaende.get(zustaende.size() - 1).schritt(zug));
        }
        sliderZustandIndex.setMin(0);
        sliderZustandIndex.setMax(zustaende.size() - 1);
    }

    public static void main(String[] args) {
        launch();
    }
}

