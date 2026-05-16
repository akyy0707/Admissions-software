package com.tuyensinh.main;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.tuyensinh.GUI.LoginForm;
import com.tuyensinh.GUI.UITheme;

public class Main {

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    new FlatLightLaf()
            );

            UITheme.install();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        new LoginForm().setVisible(true);
    }
}