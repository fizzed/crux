package com.fizzed.crux.util;

import org.junit.Test;

import java.util.List;

import static com.fizzed.crux.util.TextTable.Column.column;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class TextTableTest {

    @Test
    public void render1ColumnLeft() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.LEFT, 10)
            )
            .addRow("Item1")
            ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("ColA      "));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("----------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("Item1     "));
    }

    @Test
    public void render1ColumnRight() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.RIGHT, 10)
            )
            .addRow("Item1")
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("      ColA"));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("----------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("     Item1"));
    }

    @Test
    public void render1ColumnCenter() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.CENTER, 10)
            )
            .addRow("Item1")
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("   ColA   "));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("----------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("  Item1   "));
    }

    @Test
    public void renderLessColumnsForRow() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.LEFT, 10)
            )
            .addRow()
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("ColA      "));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("----------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("          "));
    }

    @Test
    public void render3ColumnsLeft() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.LEFT, 10),
                column("ColB", TextTable.Align.LEFT, 10),
                column("ColC", TextTable.Align.LEFT, 10)
            )
            .addRow("Item1", "Item2", "Item3")
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("ColA       ColB       ColC      "));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("--------------------------------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("Item1      Item2      Item3     "));
    }

    @Test
    public void render3ColumnsRight() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.RIGHT, 10),
                column("ColB", TextTable.Align.RIGHT, 10),
                column("ColC", TextTable.Align.RIGHT, 10)
            )
            .addRow("Item1", "Item2", "Item3")
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("      ColA       ColB       ColC"));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("--------------------------------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("     Item1      Item2      Item3"));
    }

    @Test
    public void render4ColumnsRightLeftRightLeft() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.RIGHT, 10),
                column("ColB", TextTable.Align.LEFT, 10),
                column("ColC", TextTable.Align.RIGHT, 10),
                column("ColD", TextTable.Align.LEFT, 10)
            )
            .addRow("Item1", "Item2", "Item3", "Item4")
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("      ColA ColB             ColC ColD      "));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("-------------------------------------------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("     Item1 Item2           Item3 Item4     "));
    }

    @Test
    public void render3ColumnsLeftButNotEnoughCols() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.LEFT, 10),
                column("ColB", TextTable.Align.LEFT, 10),
                column("ColC", TextTable.Align.LEFT, 10)
            )
            .addRow("Item1", "Item2")
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("ColA       ColB       ColC      "));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("--------------------------------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("Item1      Item2                "));
    }

    @Test
    public void renderNullValues() {
        TextTable table;
        int renderWidth;
        List<String> lines;

        table = new TextTable()
            .addColumns(
                column("ColA", TextTable.Align.LEFT, 10),
                column("ColB", TextTable.Align.LEFT, 10),
                column("ColC", TextTable.Align.LEFT, 10)
            )
            .addRow("Item1", "Item2", null)
        ;

        renderWidth = table.renderWidth();
        lines = table.renderLines();

        assertThat(lines, hasSize(3));
        assertThat(lines.get(0).length(), is(renderWidth));
        assertThat(lines.get(0), is("ColA       ColB       ColC      "));
        assertThat(lines.get(1).length(), is(renderWidth));
        assertThat(lines.get(1), is("--------------------------------"));
        assertThat(lines.get(2).length(), is(renderWidth));
        assertThat(lines.get(2), is("Item1      Item2      null      "));
    }

}