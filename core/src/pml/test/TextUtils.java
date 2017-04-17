package pml.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;


/**
 * Makes drawing high-quality text much easier
 * @author glorantq
 */
public class TextUtils {
    /**
     * The {@link com.badlogic.gdx.graphics.g2d.SpriteBatch} to use for drawing
     */
    private SpriteBatch spriteBatch;
    /**
     * The {@link com.badlogic.gdx.graphics.g2d.GlyphLayout} used to calculate sizes of text
     */
    private GlyphLayout layout;
    /**
     * The fon file used for drawing
     */
    private FileHandle fontFile;
    /**
     * Cache for fonts.
     */
    private HashMap<Integer, BitmapFont> fontCache = new HashMap<Integer, BitmapFont>();

    public TextUtils(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.layout = new GlyphLayout();
        this.fontFile = Gdx.files.internal("font.ttf");
    }

    /**
     * Draw text (Origin in top-left)
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawText(String text, float x, float y, int size, Color color) {
        BitmapFont font = getFont(size);
        font.setColor(color);
        font.draw(spriteBatch, text, x, y);
    }

    /**
     * Draw centered text (Origin in top-left)
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawCenteredText(String text, float x, float y, int size, Color color) {
        x = x - getTextSize(text, size).x / 2;
        drawText(text, x, y, size, color);
    }

    /**
     * Draws text with the origin in bottom-left
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawTextBottomLeft(String text, float x, float y, int size, Color color) {
        y = y + getTextSize(text, size).y;
        drawText(text, x, y, size, color);
    }

    /**
     * Draws centered text with the origin in bottom-left
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawCenteredBottomLeft(String text, float x, float y, int size, Color color) {
        x = x - getTextSize(text, size).x / 2;
        y = y + getTextSize(text, size).y;
        drawText(text, x, y, size, color);
    }

    /**
     * Draws text centered on both the X and Y axis (Origin in top-left)
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawFullCentered(String text, float x, float y, int size, Color color) {
        x = x - getTextSize(text, size).x / 2;
        y = y + getTextSize(text, size).y / 2;
        drawText(text, x, y, size, color);
    }

    /**
     * Draws text centered on both the X and Y axis with the origin in bottom-left
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawFullCenteredBottomLeft(String text, float x, float y, int size, Color color) {
        x = x - getTextSize(text, size).x / 2;
        y = y + getTextSize(text, size).y / 2;
        drawText(text, x, y, size, color);
    }

    /**
     * Draws right-aligned text (Origin in top-left)
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawTextRight(String text, float x, float y, int size, Color color) {
        x = x - getTextSize(text, size).x;
        drawText(text, x, y, size, color);
    }

    /**
     * Draws right-aligned text with the origin in bottom-left
     * @param text The text
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param size Size
     * @param color Color
     */
    public void drawTextRightBottomLeft(String text, float x, float y, int size, Color color) {
        x = x - getTextSize(text, size).x;
        drawTextBottomLeft(text, x, y, size, color);
    }

    /**
     * Draws wrapped text (Origin in top-left)
     * @param text The text to draw
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param width Max width of a line
     * @param size Text Size
     * @param color Color
     */
    public void drawWrapped(String text, float x, float y, float width, int size, Color color) {
        BitmapFont font = getFont(size);
        font.setColor(color);
        font.draw(spriteBatch, text, x, y, width, Align.topLeft, true);
    }

    /**
     * Draws wrapped centered text (Origin in top-left)
     * @param text The text to draw
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param width Max width of a line
     * @param size Text Size
     * @param color Color
     */
    public void drawWrappedCenter(String text, float x, float y, float width, int size, Color color) {
        BitmapFont font = getFont(size);
        x = x - width / 2;
        font.setColor(color);
        font.draw(spriteBatch, text, x, y, width, Align.center, true);
    }

    /**
     * Draws wrapped centered text with the origin in bottom-left
     * @param text The text to draw
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param width Max width of a line
     * @param size Text Size
     * @param color Color
     */
    public void drawWrappedCenterBottomLeft(String text, float x, float y, float width, int size, Color color) {
        BitmapFont font = getFont(size);
        Vector2 textSize = getWrappedTextSize(text, size, width);
        y += textSize.y;
        x -= width / 2;
        font.setColor(color);
        font.draw(spriteBatch, text, x, y, width, Align.center, true);
    }

    /**
     * Gets the size for wrapped text
     * @param text The text
     * @param size Size
     * @param width Max width of a line
     * @return A {@link com.badlogic.gdx.math.Vector2} containing the dimensions of the text
     */
    public Vector2 getWrappedTextSize(String text, int size, float width) {
        BitmapFont font = getFont(size);
        layout.setText(font, text, Color.WHITE, width, Align.topLeft, true);
        return new Vector2(layout.width, layout.height);
    }

    /**
     * Gets the size of a line of text
     * @param text The text
     * @param size Size
     * @return A {@link com.badlogic.gdx.math.Vector2} containing the dimensions of the text
     */
    public Vector2 getTextSize(String text, int size) {
        BitmapFont font = getFont(size);
        layout.setText(font, text);
        return new Vector2(layout.width, layout.height);
    }

    /**
     * Checks if a cached font size is available
     * @param size Size
     * @return true if available, false otherwise
     */
    private boolean isFontCached(int size) {
        return fontCache.containsKey(size);
    }

    /**
     * Returns a {@link com.badlogic.gdx.graphics.g2d.BitmapFont} with the correct size.
     * <p>If there is no cached font it makes a new Object and saves it</p>
     * @param size The font size
     * @return A {@link com.badlogic.gdx.graphics.g2d.BitmapFont}
     */
    private BitmapFont getFont(int size) {
        if(isFontCached(size)) {
            BitmapFont font = fontCache.get(size);
            font.getData().setScale(1);
            return font;
        } else {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator
                    .FreeTypeFontParameter();
            parameter.size = size;
            parameter.characters =
                    "abcdefghijklmnopqrstuvwxyzöüóőúéáűíABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ÖÜÓŐÚÉÁŰÍ][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>:Š³" +
                    "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя";
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            fontCache.put(size, font);
            return font;
        }
    }
}
