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

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  09/07/2020
 */
public class StageEvent extends Event {

    public static final EventType<StageEvent> ANY = new EventType<>(Event.ANY, "ALL_STAGE_EVENT");

    static final EventType<StageEvent> MAXIMIZE = new EventType<>(ANY, "MAXIMIZE");
    static final EventType<StageEvent> RESTORE = new EventType<>(ANY, "RESTORE");
    static final EventType<StageEvent> MINIMIZE = new EventType<>(ANY, "MINIMIZE");
    static final EventType<StageEvent> CLOSE = new EventType<>(ANY, "CLOSE");

    private GNDecoratorT decorator;

    private Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    StageEvent(EventType<? extends Event> eventType,GNDecoratorT decorator) {
        super(eventType);

        this.decorator = decorator;

        if (MAXIMIZE.equals(eventType)) {
            maximizeEvent();
        } else if(RESTORE.equals(eventType)){
            restoreEvent();
        } else if(MINIMIZE.equals(eventType)){
            minimizeEvent();
        } else if(CLOSE.equals(eventType)){
            closeEvent();
        }
    }

    private void maximizeEvent(){

        decorator.setBounds(
                new BoundingBox(
                        decorator.getStage().getX(),
                        decorator.getStage().getY(),
                        decorator.getStage().getWidth(),
                        decorator.getStage().getHeight()
                )
        );

        decorator.getStage().setX(bounds.getMinX());
        decorator.getStage().setY(bounds.getMinY());
        decorator.getStage().setWidth(bounds.getWidth());
        decorator.getStage().setHeight(bounds.getHeight());

        decorator.getStage().setMaximized(true);
    }

    private void restoreEvent(){
        decorator.getStage().setX(decorator.getNoMaximizedBounds().getMinX());
        decorator.getStage().setY(decorator.getNoMaximizedBounds().getMinY());
        decorator.getStage().setWidth(decorator.getNoMaximizedBounds().getWidth());
        decorator.getStage().setHeight(decorator.getNoMaximizedBounds().getHeight());

        decorator.getStage().setMaximized(false);
    }

    private void minimizeEvent(){
        System.out.println("minimize");
    }

    private void closeEvent(){
        System.out.println("close");
    }
}