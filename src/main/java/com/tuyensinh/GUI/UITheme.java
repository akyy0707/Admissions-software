package com.tuyensinh.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public final class UITheme {

    private UITheme() {
    }

    public static void install() {
        scaleDefaultFonts(1.08f);

        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("TextComponent.focusWidth", 1);
        UIManager.put("TextComponent.innerFocusWidth", 1);

        UIManager.put("ScrollBar.width", 12);
        UIManager.put("SplitPane.dividerSize", 6);

        UIManager.put("Table.rowHeight", 42);
        UIManager.put("Table.showHorizontalLines", Boolean.TRUE);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 0));
        UIManager.put("Table.selectionBackground", new Color(220, 235, 252));
        UIManager.put("Table.selectionForeground", new Color(30, 30, 30));
    }

    private static void scaleDefaultFonts(float scale) {
        UIDefaults defaults = UIManager.getDefaults();
        for (Enumeration<Object> e = defaults.keys(); e.hasMoreElements();) {
            Object key = e.nextElement();
            Object value = defaults.get(key);
            if (value instanceof Font) {
                Font font = (Font) value;
                float newSize = Math.max(11f, font.getSize2D() * scale);
                FontUIResource scaled = new FontUIResource(font.deriveFont(newSize));
                defaults.put(key, scaled);
            }
        }
    }
}
