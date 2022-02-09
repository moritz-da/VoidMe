package de.hdmstuttgart.voidme.shared.utils.ui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DrawHelperTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void drawHelper_test() {
        int colorInt = DrawHelper.getIntFromColor(0,0,0);
        Assert.assertEquals(0xFF000000, colorInt);
        int colorInt2 = DrawHelper.getIntFromColor(255,255,255);
        Assert.assertEquals(0xFFFFFFFF, colorInt2);
    }

    @Test
    public void testAdjustAlpha() {
    }
}