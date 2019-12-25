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
package com.gn;

import com.gn.decorator.GNDecorator;
import com.gn.decorator.options.ButtonType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  23/09/2018
 * Version 1.0
 */
public class App1 extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/com/gn/resources/sample.fxml"));
        VBox content = new VBox();
        content.setPrefSize(400,400);
        GNDecorator window = new GNDecorator();
        window.setContent(content);
        window.initTheme(GNDecorator.Theme.DARKULA);
//        window.addButton(ButtonType.FULL_EFFECT);


        window.setTitle("Application");
        window.addButton(ButtonType.FULL_EFFECT);
        window.setIcon(null);
        window.setTitle(null);
        window.fullBody();
        window.fullBody();
//        window.addCustom(new FullScreen());
        window.centralizeTitle();
        window.show();

        window.getScene().getStylesheets().addAll(getClass().getResource("/com/gn/resources/css/custom.css").toExternalForm());

        ScenicView.show(window.getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
