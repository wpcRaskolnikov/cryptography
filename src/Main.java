import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
    private enum Type {PLAIN, CIPHER, KEY}

    private TextArea[] Tf;
    private final Button enButton = new Button("加密");
    private final Button deButton = new Button("解密");
    private final Button uppButton = new Button("大写");
    private final Button lowButton = new Button("小写");
    private final ToggleGroup group = new ToggleGroup();

    @Override
    public void start(final Stage primaryStage) {

        RadioButton[] selections = new RadioButton[]{new RadioButton("Caesar"),
                new RadioButton("Affine"), new RadioButton("Vigenere"),
                new RadioButton("RC4"), new RadioButton("DES")};
        int buttonAmount = 4;
        HBox buttonBox = new HBox(buttonAmount);
        GridPane mainPane = new GridPane();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(mainPane);
        borderPane.setBottom(buttonBox);

        mainPane.add(new Label("明文: "), 0, 1);
        mainPane.add(new Label("密文: "), 0, 2);
        mainPane.add(new Label("密钥: "), 0, 3);

        Tf = new TextArea[3];
        for (int i = 0; i < Tf.length; i++) {
            Tf[i] = new TextArea();
            Tf[i].setMaxWidth(300);
            mainPane.add(Tf[i], 1, i + 1);
        }

        buttonBox.getChildren().addAll(enButton, deButton, uppButton, lowButton);

        int i = 8;
        for (RadioButton selection : selections) {
            selection.setToggleGroup(group);
            mainPane.add(selection, i/4, i%4);
            selection.setUserData(selection.getText());
            i++;
        }

        enButton.setOnAction(e -> {
            try {
                String text = "";
                if (group.getSelectedToggle() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("请选择加密方式");
                    alert.show();
                } else if (group.getSelectedToggle().getUserData().equals("DES")) {
                    text = DES.encode(Tf[Type.PLAIN.ordinal()].getText(),
                            Tf[Type.KEY.ordinal()].getText());
                } else if (group.getSelectedToggle().getUserData().equals("RC4")) {
                    text = RC4.encode(Tf[Type.PLAIN.ordinal()].getText(),
                            Tf[Type.KEY.ordinal()].getText());
                } else if (group.getSelectedToggle().getUserData().equals("Caesar")) {
                    text = Caesar.encode(Tf[Type.PLAIN.ordinal()].getText(),
                            Integer.parseInt(Tf[Type.KEY.ordinal()].getText()) % 26);
                } else if (group.getSelectedToggle().getUserData().equals("Affine")) {
                    String str = Tf[Type.KEY.ordinal()].getText();
                    int[] key = new int[2];
                    key[0] = Integer.parseInt(str.split(" ")[0]);
                    key[1] = Integer.parseInt(str.split(" ")[1]);
                    text = Affine.encode(Tf[Type.PLAIN.ordinal()].getText(),
                            key);
                } else if (group.getSelectedToggle().getUserData().equals("Vigenere")) {
                    text = Vigenere.encode(Tf[Type.PLAIN.ordinal()].getText(),
                            Tf[Type.KEY.ordinal()].getText());
                }
                Tf[Type.CIPHER.ordinal()].setText(text);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deButton.setOnAction(e -> {
            try {
                String text = "";
                if (group.getSelectedToggle() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("请选择加密方式");
                    alert.show();
                } else if (group.getSelectedToggle().getUserData().equals("DES")) {
                    text = DES.decode(Tf[Type.CIPHER.ordinal()].getText(),
                            Tf[Type.KEY.ordinal()].getText());
                } else if (group.getSelectedToggle().getUserData().equals("RC4")) {
                    text = RC4.decode(Tf[Type.CIPHER.ordinal()].getText(),
                            Tf[Type.KEY.ordinal()].getText());
                } else if (group.getSelectedToggle().getUserData().equals("Caesar")) {
                    text = Caesar.decode(Tf[Type.CIPHER.ordinal()].getText(),
                            Integer.parseInt(Tf[Type.KEY.ordinal()].getText()) % 26);
                } else if (group.getSelectedToggle().getUserData().equals("Affine")) {
                    String str = Tf[Type.KEY.ordinal()].getText();
                    int[] key = new int[2];
                    key[0] = Integer.parseInt(str.split(" ")[0]);
                    key[1] = Integer.parseInt(str.split(" ")[1]);
                    text = Affine.decode(Tf[Type.CIPHER.ordinal()].getText(),
                            key);
                } else if (group.getSelectedToggle().getUserData().equals("Vigenere")) {
                    text = Vigenere.decode(Tf[Type.CIPHER.ordinal()].getText(),
                            Tf[Type.KEY.ordinal()].getText());
                }
                Tf[Type.PLAIN.ordinal()].setText(text);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        uppButton.setOnAction(e -> {
            try {
                String text = Tf[Type.PLAIN.ordinal()].getText().toUpperCase();
                Tf[Type.PLAIN.ordinal()].setText(text);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        lowButton.setOnAction(e -> {
            try {
                String text = Tf[Type.PLAIN.ordinal()].getText().toLowerCase();
                Tf[Type.PLAIN.ordinal()].setText(text);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(borderPane, 500, 300);
        primaryStage.setTitle("cryptography");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}