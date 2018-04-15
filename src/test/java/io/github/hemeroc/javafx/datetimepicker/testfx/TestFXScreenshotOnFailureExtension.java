package io.github.hemeroc.javafx.datetimepicker.testfx;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;
import static org.testfx.util.DebugUtils.saveScreenshot;

public class TestFXScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final String SCREENSHOT_PATH = "testfx-result/";
    private static final String INDENT = "    ";

    @Override
    public void afterTestExecution(ExtensionContext context) {
        context.getExecutionException().ifPresent(t -> context.getTestMethod().ifPresent(m -> {
            StringBuilder screenshotResult = new StringBuilder();
            String testClassName = m.getDeclaringClass().getSimpleName();
            String testMethodName = m.getName();
            String screenshotName = testClassName + "-" + testMethodName + ".png";
            try {
                createDirectories(Paths.get(SCREENSHOT_PATH));
                saveScreenshot(() -> Paths.get(SCREENSHOT_PATH, screenshotName), INDENT).apply(screenshotResult);
            } catch (IOException e) {
                System.err.println("Failed to create screenshot path '" + SCREENSHOT_PATH + "', " + e.getMessage());
                saveScreenshot(() -> Paths.get(screenshotName), INDENT).apply(screenshotResult);
            }
            System.out.println(screenshotResult);
        }));
    }
}
