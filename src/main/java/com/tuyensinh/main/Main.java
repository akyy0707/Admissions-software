package com.tuyensinh.main;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.tuyensinh.GUI.LoginForm;

public class Main {

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    new FlatLightLaf()
            );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        new LoginForm().setVisible(true);
    }
}