/*
 * Copyright 2019 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.crux.util;

import java.util.Objects;

/**
 * Simple utility immutable class to represent x, y, width, and height in 2D space and
 * not rely on the awful Java AWT Rectangle class.
 */
public class Rect2D {
 
    final private double x;
    final private double y;
    final private double width;
    final private double height;

    public Rect2D(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rect2D(double x, double y, Size2D size) {
        Objects.requireNonNull(size, "size was null");
        this.x = x;
        this.y = y;
        this.width = size.getWidth();
        this.height = size.getHeight();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // helpers
    
    public Rect2D plusX(double x) {
        return this.plus(x, 0d, 0d, 0d);
    }
    
    public Rect2D plusY(double y) {
        return this.plus(0d, y, 0d, 0d);
    }
    
    public Rect2D plusWidth(double width) {
        return this.plus(0d, 0d, width, 0d);
    }
    
    public Rect2D plusHeight(double height) {
        return this.plus(0d, 0d, 0d, height);
    }
    
    public Rect2D plus(Rect2D rect) {
        if (rect == null) {
            return this;
        }
        return this.plus(rect.x, rect.y, rect.width, rect.height);
    }
    
    public Rect2D plus(double x, double y, double width, double height) {
        return new Rect2D(this.x+x, this.y+y, this.width+width, this.height+height);
    }
    
    public int ceilX() {
        return (int)Math.ceil(x);
    }
    
    public int ceilY() {
        return (int)Math.ceil(y);
    }
    
    public int ceilWidth() {
        return (int)Math.ceil(width);
    }
    
    public int ceilHeight() {
        return (int)Math.ceil(height);
    }
    
    public int floorX() {
        return (int)Math.floor(x);
    }
    
    public int floorY() {
        return (int)Math.floor(y);
    }
    
    public int floorWidth() {
        return (int)Math.floor(width);
    }
    
    public int floorHeight() {
        return (int)Math.floor(height);
    }
    
}