package com.fizzed.crux.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class TextTable {

    public enum Align {
        LEFT,
        CENTER,
        RIGHT
    }

    static public class Column {

        private String name;
        private Align align;
        private int width;

        public Column() {
        }

        public String getName() {
            return name;
        }

        public Column setName(String name) {
            this.name = name;
            return this;
        }

        public Align getAlign() {
            return align;
        }

        public Column setAlign(Align align) {
            this.align = align;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Column setWidth(int width) {
            this.width = width;
            return this;
        }

        @Override
        public String toString() {
            return this.name;
        }

        static public Column column(String name, Align align, int width) {
            return new Column()
                .setName(name)
                .setAlign(align)
                .setWidth(width);
        }

    }

    private List<Column> columns;
    private List<Object[]> rows;
    private int columnPadding;
    private char columnSeparator;

    public TextTable() {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.columnPadding = 1;
        this.columnSeparator = 0;   // none
    }

    public int getColumnPadding() {
        return columnPadding;
    }

    public TextTable setColumnPadding(int columnPadding) {
        this.columnPadding = columnPadding;
        return this;
    }

    public char getColumnSeparator() {
        return columnSeparator;
    }

    public TextTable setColumnSeparator(char columnSeparator) {
        this.columnSeparator = columnSeparator;
        return this;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public TextTable setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    public List<Object[]> getRows() {
        return rows;
    }

    public TextTable setRows(List<Object[]> rows) {
        this.rows = rows;
        return this;
    }

    public int renderWidth() {
        int width = 0;

        // add the width of every column
        for (Column c : this.columns) {
            width += c.getWidth();
        }

        // if a separator is used
        if (this.columnSeparator > 0) {
            // width between columns
            width += ((this.columns.size()-1) * (this.columnPadding+1+this.columnPadding));
            // front and back
            width += ((1 + this.columnPadding) * 2);
        } else {
            // width between columns
            width += ((this.columns.size()-1) * this.columnPadding);
        }

        return width;
    }

    public List<String> renderLines() {
        final List<String> lines = new ArrayList<>(2+this.rows.size());
        this.render(lines::add);
        return lines;
    }

    public String renderHeader() {
        final StringBuilder sb = new StringBuilder();
        this.renderHeader(sb);
        return sb.toString();
    }

    public void renderHeader(StringBuilder sb) {
        this.renderRow(sb, ' ', this.columns.toArray());
    }

    public String renderHeaderSeparator() {
        final StringBuilder sb = new StringBuilder();
        this.renderHeaderSeparator(sb);
        return sb.toString();
    }

    public void renderHeaderSeparator(StringBuilder sb) {
        String[] headerSeparatorRow = new String[this.columns.size()];
        for (int i = 0; i < this.columns.size(); i++) {
            headerSeparatorRow[i] = "";
        }
        this.renderRow(sb, '-', headerSeparatorRow);
    }

    public String renderRow(Object... row) {
        final StringBuilder sb = new StringBuilder();
        this.renderRow(sb, row);
        return sb.toString();
    }

    public void renderRow(StringBuilder sb, Object... row) {
        this.renderRow(sb, ' ', row);
    }

    public void render(Consumer<String> lineConsumer) {
        final int renderWidth = this.renderWidth();
        // string builder we'll reuse
        final StringBuilder sb = new StringBuilder();

        //
        // render header line
        //

        this.renderHeader(sb);
        lineConsumer.accept(sb.toString());

        //
        // render header separator line
        //

        sb.setLength(0);
        this.renderHeaderSeparator(sb);
        lineConsumer.accept(sb.toString());

        //
        // render row lines
        //

        for (Object[] row : this.rows) {
            sb.setLength(0);
            this.renderRow(sb, ' ', row);
            lineConsumer.accept(sb.toString());
        }
    }

    private void renderRow(StringBuilder sb, char paddingChar, Object[] row) {
        if (this.columnSeparator > 0) {
            sb.append(this.columnSeparator);
            repeat(sb, paddingChar, this.columnPadding);
        }

        for (int i = 0; i < this.columns.size(); i++) {
            if (i > 0) {
                if (this.columnSeparator > 0) {
                    repeat(sb, paddingChar, this.columnPadding);
                    sb.append(this.columnSeparator);
                    repeat(sb, paddingChar, this.columnPadding);
                } else {
                    repeat(sb, paddingChar, this.columnPadding);
                }
            }

            final Column c = this.columns.get(i);
            final Object o = row.length > i ? row[i] : "";

            if (c.align == null || c.align == Align.LEFT) {
                rightPad(sb, Objects.toString(o), c.getWidth(), paddingChar);
            } else if (c.align == Align.RIGHT) {
                leftPad(sb, Objects.toString(o), c.getWidth(), paddingChar);
            } else if (c.align == Align.CENTER) {
                centerPad(sb, Objects.toString(o), c.getWidth(), paddingChar);
            }
        }

        if (this.columnSeparator > 0) {
            repeat(sb, paddingChar, this.columnPadding);
            sb.append(this.columnSeparator);
        }
    }

    // helpers

    public TextTable addColumn(Column column) {
        this.columns.add(column);
        return this;
    }

    public TextTable addColumns(Column... columns) {
        for (Column c : columns) {
            this.addColumn(c);
        }
        return this;
    }

    public TextTable addRow(Object... row) {
        this.rows.add(row);
        return this;
    }


    static public void repeat(StringBuilder sb, char repeatChar, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(repeatChar);
        }
    }

    public static void rightPad(StringBuilder sb, String str, int size, char padChar) {
        if (str == null) {
            return;
        }

        if (str.length() > size) {
            sb.append(str.substring(0, size));
        } else {
            final int padCount = size - str.length();
            sb.append(str);
            repeat(sb, padChar, padCount);
        }
    }

    public static void leftPad(StringBuilder sb, String str, int size, char padChar) {
        if (str == null) {
            return;
        }

        if (str.length() > size) {
            sb.append(str.substring(0, size));
        } else {
            final int padCount = size - str.length();
            repeat(sb, padChar, padCount);
            sb.append(str);
        }
    }

    public static void centerPad(StringBuilder sb, String str, int size, char padChar) {
        if (str == null) {
            return;
        }

        if (str.length() > size) {
            sb.append(str.substring(0, size));
        } else {
            final int padCount = size - str.length();
            final int padLeftCount = padCount/2;
            final int padRightCount = padCount - padLeftCount;
            repeat(sb, padChar, padLeftCount);
            sb.append(str);
            repeat(sb, padChar, padRightCount);
        }
    }

}