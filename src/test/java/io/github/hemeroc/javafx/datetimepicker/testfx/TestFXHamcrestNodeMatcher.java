package io.github.hemeroc.javafx.datetimepicker.testfx;

import javafx.scene.Node;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.GeneralMatchers;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

public class TestFXHamcrestNodeMatcher {

    private TestFXHamcrestNodeMatcher() {
    }

    @Factory
    public static Matcher<Node> hasNoChild(String query) {
        String descriptionText = "Node has no child \"" + query + "\"";
        return GeneralMatchers.baseMatcher(descriptionText, (node) -> {
            NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
            NodeQuery nodeQuery = nodeFinder.from(node);
            return nodeQuery.lookup(query).queryAll().isEmpty();
        });
    }

}
