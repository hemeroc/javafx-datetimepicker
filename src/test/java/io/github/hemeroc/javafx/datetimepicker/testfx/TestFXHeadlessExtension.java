package io.github.hemeroc.javafx.datetimepicker.testfx;

import org.junit.jupiter.api.extension.Extension;

public class TestFXHeadlessExtension implements Extension {

    static {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("java.awt.robot", "true");
        System.setProperty("java.awt.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

}