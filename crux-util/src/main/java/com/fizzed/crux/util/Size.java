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

/**
 * Simple utility immutable class to represent width, and height in space and
 * not rely on the awful Java AWT Dimension or Rectangle class.
 */
public class Size {
 
    final private double width;
    final private double height;

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    
    // helpers
    
    public Size plusWidth(double width) {
        return this.plus(width, 0d);
    }
    
    public Size plusHeight(double height) {
        return this.plus(0d, height);
    }
    
    public Size plus(Size size) {
        if (size == null) {
            return this;
        }
        return this.plus(size.width, size.height);
    }
    
    public Size plus(double width, double height) {
        return new Size(this.width+width, this.height+height);
    }
    
    public int ceilWidth() {
        return (int)Math.ceil(width);
    }
    
    public int ceilHeight() {
        return (int)Math.ceil(height);
    }
    
    public int floorWidth() {
        return (int)Math.floor(width);
    }
    
    public int floorHeight() {
        return (int)Math.floor(height);
    }
    
}