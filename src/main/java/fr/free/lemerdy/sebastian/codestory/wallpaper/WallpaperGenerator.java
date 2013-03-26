package fr.free.lemerdy.sebastian.codestory.wallpaper;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * Generates Code Story wallpaper from <ol>
 * <li>Code Story logo</li>
 * <li>Original Gilded Rose source code used by Code Story contest on 1st February 2012</li>
 * </ol>
 * Code base comes from http://www.generative-gestaltung.de/P_4_3_2_01.
 */
public class WallpaperGenerator extends PApplet {

    private static final float KERNING = 0.5f;
    private static final float FONT_SIZE = 10;

    private PFont font;
    private PImage img;
    private int codeWidth;
    private int codeHeight;
    private float paddingLeft;
    private float paddingTop;
    private String inputText;

    @Override
    public boolean sketchFullScreen() {
        return true;
    }

    @Override
    public void setup() {
        sketchPath = System.getProperty("user.home");

        size(displayWidth, displayHeight);
        smooth();

        font = createFont("Menlo-Regular", FONT_SIZE);

        img = loadImage("logo.png");
        codeWidth = (int) (displayWidth * .8);
        codeHeight = codeWidth * img.height / img.width;

        paddingLeft = (width - codeWidth) / 2;
        paddingTop = (height - codeHeight) / 2;

        noLoop();

        loadGuildedRoseCode();
    }

    @Override
    public void draw() {
        background(19, 22, 67);
        textAlign(LEFT);

        float spacing = 12;
        float x = paddingLeft, y = paddingTop + spacing / 2;
        int counter = 0;

        while (y < paddingTop + codeHeight) {
            pushMatrix();
            translate(x, y);

            textFont(font, FONT_SIZE);
            fill(getColorFromImage(x, y));

            final char letter = inputText.charAt(counter);
            text(letter, 0, 0);
            float letterWidth = textWidth(letter) + KERNING;
            x = x + letterWidth; // update x-coordinate
            popMatrix();

            if (x + letterWidth >= paddingLeft + codeWidth) {
                x = paddingLeft;
                y = y + spacing; // add line height
            }

            counter++;
            if (counter > inputText.length() - 1) {
                counter = 0;
            }
        }

        sign();

        saveFrame("codestory-wallpaper.png");
    }

    private int getColorFromImage(float x, float y) {
        // translate position (display) to position (image)
        int imgX = (int) map(x, paddingLeft, paddingLeft + codeWidth, 0, img.width);
        int imgY = (int) map(y, paddingTop, paddingTop + codeHeight, 0, img.height);
        // get current color
        int c = img.pixels[imgY * img.width + imgX];
        int lightColor = color(64);
        return blendColor(c, lightColor, LIGHTEST);
    }

    private void loadGuildedRoseCode() {
        final StringBuilder inputTextWithoutCarriageReturns = new StringBuilder();
        for (final String line : loadStrings("Inn.txt")) {
            if (line.isEmpty()) {
                continue;
            }
            inputTextWithoutCarriageReturns
                    .append(line.replaceAll("\t", ""))
                    .append(' ');
        }
        inputText = inputTextWithoutCarriageReturns.toString();
    }

    private void sign() {
        fill(235);
        textAlign(RIGHT);
        text("the power of refactoring", paddingLeft + codeWidth - 4, codeHeight + paddingTop + 20);
    }

    public static void main(String... args) {
        PApplet.main(WallpaperGenerator.class.getName());
    }
}
