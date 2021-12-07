package controls;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

public class TextBox extends GridPane {
    SVGPath svgIcon;
//    SVGPath ok, notOk;
//    Text error;
//    boolean isRequired
    Label hintLabel;
    TextField input;
    Separator line;
    TranslateTransition moveHint;
    boolean isHintMoved;
    public StringProperty textProperty;

    public TextBox(String hint, String icon){
        hintLabel = new Label(hint);
        hintLabel.setTextFill(Color.GRAY);
        svgIcon = new SVGPath();
        svgIcon.setContent(icon);
        svgIcon.setFill(Color.LIGHTBLUE);
        input = new TextField();
        input.setBackground(null);
        line = new Separator();
        line.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, Color.TRANSPARENT, Color.TRANSPARENT,Color.TRANSPARENT, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,BorderStrokeStyle.NONE,BorderStrokeStyle.NONE, null, null, null)));

        addRow(0, svgIcon, input);
        addRow(1, line);
        add(hintLabel, 1, 0);
        setColumnSpan(line, 2);
        setAlignment(Pos.CENTER_LEFT);
        setHgrow(input, Priority.ALWAYS);
        setMargin(hintLabel, new Insets(0,0,0,5));

        moveHint = new TranslateTransition(Duration.millis(100));
        moveHint.setInterpolator(Interpolator.EASE_IN);
        moveHint.setNode(hintLabel);
        setMinHeight(40);
        textProperty = input.textProperty();
        input.addEventHandler(MouseEvent.ANY, this::onMouseEvents);
        input.focusedProperty().addListener(this::onFocusChanged);
        input.textProperty().addListener(this::onTextChanged);
        //hintLabel.addEventFilter(MouseEvent.ANY, this::onMouseEvents);
    }
    void onMouseEvents(MouseEvent e){
        if(e.getEventType() == MouseEvent.MOUSE_RELEASED) input.requestFocus();
        else if(e.getEventType() == MouseEvent.MOUSE_EXITED) {
            if(!input.isFocused()){
                svgIcon.requestFocus();
            }
        }
    }
    void onTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue){
        if(newValue == null || newValue.isEmpty()){
            if(isHintMoved && !input.isFocused())
                moveHintDown();
        }
    }
    void onFocusChanged(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
        if(moveHint.getStatus() == Animation.Status.RUNNING) moveHint.stop();
        if(!newValue && textIsNullOrEmpty()){
            resetFocusColor();
            moveHintDown();
        }
        else if(!newValue && !textIsNullOrEmpty()){
            resetFocusColor();
        }
        else if(newValue){
            setFocusColor();
            if(!isHintMoved) moveHintUp();
        }
    }
    boolean textIsNullOrEmpty(){
        return input.getText() == null || input.getText().isEmpty();
    }
    void moveHintUp(){
        moveHint.setByY(-15);
        moveHint.setByX(-5);
        moveHint.play();
        isHintMoved = true;
    }
    void moveHintDown(){
        moveHint.setByY(15);
        moveHint.setByX(5);
        moveHint.play();
        isHintMoved = false;
    }
    void setFocusColor(){
        svgIcon.setFill(Color.CORNFLOWERBLUE);
        line.setBorder(new Border(new BorderStroke(Color.CORNFLOWERBLUE, Color.TRANSPARENT, Color.TRANSPARENT,Color.TRANSPARENT, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,BorderStrokeStyle.NONE,BorderStrokeStyle.NONE, null, null, null)));
    }
    void resetFocusColor(){
        svgIcon.setFill(Color.LIGHTBLUE);
        line.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, Color.TRANSPARENT, Color.TRANSPARENT,Color.TRANSPARENT, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,BorderStrokeStyle.NONE,BorderStrokeStyle.NONE, null, null, null)));
    }
}
