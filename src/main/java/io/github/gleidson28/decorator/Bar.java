/*
 * Copyright (C) Gleidson Neves da Silveira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.gleidson28.decorator;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/07/2020
 */
class Bar extends HBox implements StageChanges, StageReposition {

    private final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    private GNDecorator    decorator;
    private Stage           stage;

    private final ObservableList<Button> defaultControls;

    private final Label          title               = new Label();
    private final HBox           titleContainer      = new HBox(title);
    private final HBox           controlsContainer   = new HBox();
    private final CustomControls customControls      = new CustomControls();
    private final MenuBar        menuBar             = new MenuBar();

    private final Minimize minimize;
    private final Maximize maximize;
    private final Close    close;

    Bar(GNDecorator decorator) {
        this.setId("gn-bar");

        minimize = new Minimize(decorator);
        maximize = new Maximize(decorator);
        close = new Close(decorator);

        defaultControls = FXCollections.observableArrayList(
            minimize, maximize, close
        );

        confLayout(decorator);

        decorator.titleProperty().bindBidirectional(title.textProperty());
    }

    public CustomControls getCustomBar(){
        return customControls;
    }

    public ObservableList<Node> getCustomControls(){
        return this.customControls.getChildren();
    }

    private final ChangeListener<Boolean> autoHover
            = (observable, oldValue, newValue) -> hoverButtons(newValue);

    public void addAutoHover(){
        defaultControls.forEach(e -> e.hoverProperty()
                .addListener(autoHover)
        );
    }

    public void removeAutoHover(){
        defaultControls.forEach(e -> e.hoverProperty()
                .removeListener(autoHover)
        );
    }

    public void hoverButtons(boolean value){
        close.pseudoClassStateChanged(PseudoClass.getPseudoClass("hover"), value);
        maximize.pseudoClassStateChanged(PseudoClass.getPseudoClass("hover"), value);
        minimize.pseudoClassStateChanged(PseudoClass.getPseudoClass("hover"), value);
    }

    public void invertControls(boolean value){
        if (value) {
            getChildren().removeAll(controlsContainer, menuBar);
            getChildren().add(0, controlsContainer);
            getChildren().add(1, menuBar);
            customControls.setAlignment(Pos.CENTER_RIGHT);
//            controlsContainer.getChildren().clear();
//            controlsContainer.getChildren().addAll(close, minimize, maximize);
        } else {
            System.out.println(getChildren());
            getChildren().removeAll(controlsContainer, menuBar);
            getChildren().add(0, menuBar);
            getChildren().add(3, controlsContainer);
//
//            System.out.println(controlsContainer.getChildren());
//            controlsContainer.getChildren().clear();
////            controlsContainer.getChildren().add(controlsContainer.getChildren().size(), minimize);
////            controlsContainer.getChildren().add(1, maximize);
////            controlsContainer.getChildren().add(2, close);
//            controlsContainer.getChildren().addAll(minimize, maximize, close);
        }
    }

    private void confLayout(GNDecorator decorator){

        this.menuBar.getStyleClass().add("menuBar");
        this.titleContainer.getStyleClass().add("titleContainer");
        this.controlsContainer.getStyleClass().add("controlsContainer");

        controlsContainer.setSpacing(0D);
        this.menuBar.setPadding(new Insets(0D));

        controlsContainer.prefHeightProperty().bind(decorator.barHeight);
        menuBar.prefHeightProperty().bind(decorator.barHeight);

        defaultControls.forEach(e ->
                e.prefHeightProperty().
                        bind(decorator.barHeight));

        controlsContainer.setAlignment(Pos.CENTER_RIGHT);

        titleContainer.setPadding(new Insets(0,0,0,4));

        this.setAlignment(Pos.TOP_RIGHT);
        titleContainer.setAlignment(Pos.CENTER_LEFT);
        controlsContainer.getChildren().setAll(defaultControls);

        this.getChildren().add(menuBar);
        this.getChildren().add(titleContainer);
        this.getChildren().add(customControls);
        this.getChildren().add(controlsContainer);

        HBox.setHgrow(customControls, Priority.ALWAYS);

        this.setPrefHeight(30D);
        this.decorator = decorator;
        this.stage = decorator.stage;

        this.setAlignment(Pos.CENTER);
        configActions();
    }

    private final EventHandler<MouseEvent> maximizeFromBar = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(event.getClickCount() == 2){
                if(!decorator.isMaximized()) {
                    fireEvent(new StageEvent(StageEvent.MAXIMIZE, decorator));
                } else {
                    fireEvent(new StageEvent(StageEvent.RESTORE, decorator));
                }

            }
        }
    };

    private void configActions(){
        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                setInitX(event.getScreenX());
                setInitY(event.getScreenY());
                event.consume();

            }
        });

        this.setOnMouseClicked(maximizeFromBar);

        this.setOnMouseDragged(event -> {
            if (!event.isPrimaryButtonDown() || getInitX() == -1 ) {
                return;
            }

            if (event.isStillSincePress()) {
                return;
            }

            if(decorator.isMaximized() && decorator.isResizable()) {

                if (bounds.getMaxX() < (event.getScreenX() + decorator.noMaximizedBounds.getWidth())) {
                    stage.setX(bounds.getMaxX() - (decorator.noMaximizedBounds.getWidth()));
                } else if (bounds.getMinX() < (decorator.noMaximizedBounds.getWidth() - event.getScreenX())) {
                    stage.setX(0);
                } else {
                    stage.setX(event.getScreenX() - (decorator.noMaximizedBounds.getWidth() / 2));
                }

                if (decorator.noMaximizedBounds.getHeight() > bounds.getMaxY()) {
                    stage.setHeight(bounds.getMaxY() - 100);
                } else {
                    stage.setHeight(decorator.noMaximizedBounds.getHeight());
                }

                if (decorator.noMaximizedBounds.getWidth() > bounds.getMaxX()) {
                    stage.setWidth(bounds.getWidth() - 200);
                } else {
                    stage.setWidth(decorator.noMaximizedBounds.getWidth());
                }
                stage.setY(0);
                stage.setMaximized(false);
            } else {

                if(decorator.isResizable()) {

                    Stage stage = decorator.translucentStage;
                    decorator.stage.setAlwaysOnTop(true);

                    if (isOnTopLeft(event)) {
                        repositionOnTopLeft(stage, 0);
                        stage.show();
                    } else if (isOnBottomLeft(event)) {
                        repositionOnBottomLeft(stage, 20);
                        stage.show();
                    } else if (isOnTopRight(event)) {
                        repositionOnTopRight(stage, 20);
                        stage.show();
                    } else if (isOnBottomRight(event)) {
                        repositionOnBottomRight(stage, 20);
                        stage.show();
                    } else if (isOnRight(event)) {
                        repositionOnRight(stage, 20);
                        stage.show();
                    } else if (isOnLeft(event)) {
                        repositionOnLeft(stage, 20);
                        stage.show();
                    } else if (isOnTop(event)) {
                        repositionOnTop(stage, 20);
                        stage.show();
                    } else {
                        decorator.translucentStage.close();
                    }
                }

                setNewX(event.getScreenX());
                setNewY(event.getScreenY());

                double deltaX = getNewX() - getInitX();
                double deltaY = getNewY() - getInitY();

                setInitX(getNewX());
                setInitY(getNewY());

                this.stage.setX(this.stage.getX() + deltaX);
                setStageY(this.stage, this.stage.getY() + deltaY);

            }

            this.setCursor(Cursor.MOVE);
            decorator.setMaximized(false);

        });

        this.setOnMouseReleased(event -> {
            this.setCursor(Cursor.HAND);

            if(decorator.isResizable()) {

                Stage stage = decorator.stage;
                this.decorator.stage.setAlwaysOnTop(false);

                if (isOnTopLeft(event)) {
                    repositionOnTopLeft(stage, 0);
                } else if (isOnBottomLeft(event)) {
                    repositionOnBottomLeft(stage, 0);
                } else if (isOnTopRight(event)) {
                    repositionOnTopRight(stage, 0);
                } else if (isOnBottomRight(event)) {
                    repositionOnBottomRight(stage, 0);
                } else if (isOnRight(event)) {
                    repositionOnRight(stage, 0);
                } else if (isOnLeft(event)) {
                    repositionOnLeft(stage, 0);
                } else if (isOnTop(event)) {
                    repositionOnTop(stage, 0);
                    decorator.setMaximized(true);

                } else {
                    decorator.translucentStage.close();
                }

                decorator.translucentStage.close();
            }

        });
    }

    private boolean isOnTopLeft(MouseEvent event){
        return event.getScreenY() <= 0 && event.getScreenX() <= 0;
    }

    private boolean isOnBottomLeft(MouseEvent event){
        return event.getScreenX() <= 0 && event.getScreenY() >= bounds.getMaxY();
    }

    private boolean isOnTopRight(MouseEvent event){
        return event.getScreenX() >= (bounds.getMaxX() -2) && event.getScreenY() <= bounds.getMinY();
    }

    private boolean isOnBottomRight(MouseEvent event){
        return event.getScreenX() >= (bounds.getMaxX() - 2) && event.getScreenY() >= bounds.getMaxY();
    }

    private boolean isOnRight(MouseEvent event){
        return event.getScreenX() >= ( bounds.getMaxX() -2 );
    }

    private boolean isOnLeft(MouseEvent event){
        return event.getScreenX() <= 0;
    }

    private boolean isOnTop(MouseEvent event){
        return event.getScreenY() <= 0;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public void disableActions(boolean disable) {
        if (disable) {
            maximize.setDisable(true);
            setOnMouseClicked(null);
        } else {
            maximize.setDisable(false);
            setOnMouseClicked(maximizeFromBar);
        }
    }
}