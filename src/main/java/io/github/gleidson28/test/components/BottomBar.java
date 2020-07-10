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
package io.github.gleidson28.test.components;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/07/2020
 */
public class BottomBar extends Pane implements StageChanges {

    private final Stage stage;

    protected BottomBar(Stage stage) {
        this.getStyleClass().add("gn-bottom-bar");
        this.setId("gn-bottom-bar");
        this.setCursor(Cursor.S_RESIZE);
        this.setMinHeight(3D);
        this.stage = stage;
        this.setBackground(new Background(
                new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        configActions();
    }

    private void configActions(){
        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                setInitX(event.getScreenX());
                setInitY(event.getScreenY());
                event.consume();
            }
        });

        this.setOnMouseDragged(event -> {
            if (!event.isPrimaryButtonDown() || (getInitX() == -1 && getInitY() == -1)) {
                return;
            }

            if (this.stage.isFullScreen()) {
                return;
            }

            if (event.isStillSincePress()) {
                return;
            }

            setNewX(event.getScreenX());
            setNewY(event.getScreenY());

            double deltaY = getNewY() - getInitY();

            if (Cursor.S_RESIZE.equals(this.getCursor())) {
                setStageHeight(this.stage, this.stage.getHeight() + deltaY);
                event.consume();
            }
        });
    }
}