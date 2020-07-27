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

import javafx.scene.layout.HBox;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  27/07/2020
 */
class CustomControls extends HBox {

    public CustomControls() {
        this.setId("gn-custom-controls");
    }
    
    public void block(){
        getChildren().forEach( e -> e.setMouseTransparent(true));        
    }

    public void unblock(){
        getChildren().forEach( e -> e.setMouseTransparent(false));
    }
    
}
