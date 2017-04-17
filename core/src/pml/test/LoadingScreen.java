package pml.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Gerber Lóránt on 2017. 04. 15..
 */

public class LoadingScreen {
    private Texture bg, spinner;
    private float imgScale, width, height;
    private State state;

    private float rotation = 0f;
    private float spinnerScale = 0f;

    private boolean scheduleFinish = false;

    public LoadingScreen(float imgScale, float width, float height) {
        this.imgScale = imgScale;
        this.width = width;
        this.height = height;
        this.bg = new Texture("bg.png");
        this.spinner = new Texture("spinner.png");
        this.state = State.ANIMATE_IN;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(bg, 0, 0, this.width, this.height);

        switch (state) {
            case ANIMATE_IN:
                renderAnimateIn(spriteBatch);
                break;
            case ANIMATE_IN_2:
                renderAnimateIn2(spriteBatch);
                break;
            case ANIMATE:
                renderAnimate(spriteBatch);
                break;
            case ANIMATE_OUT:
                renderAnimateOut(spriteBatch);
                break;
            case ANIMATE_OUT_2:
                renderAnimateOut2(spriteBatch);
                break;
            default:
                break;
        }

        if(rotation <= -360)
            rotation = 0;
    }

    public void done() {
        if(state == State.ANIMATE) {
            spinnerScale = 1f;
            state = State.ANIMATE_OUT;
        } else {
            scheduleFinish = true;
        }
    }

    private void renderAnimateOut2(SpriteBatch spriteBatch) {
        drawSpinner(spriteBatch);

        spinnerScale -= Gdx.graphics.getDeltaTime() * 3;
        if(spinnerScale <= 0f && state != State.DONE) {
            spinnerScale = 0f;
            state = State.DONE;
        }
    }

    private void renderAnimateOut(SpriteBatch spriteBatch) {
        drawSpinner(spriteBatch);

        spinnerScale += Gdx.graphics.getDeltaTime() * 3;
        if(spinnerScale >= 1.8f) {
            spinnerScale = 1.8f;
            state = State.ANIMATE_OUT_2;
        }
    }

    private void renderAnimate(SpriteBatch spriteBatch) {
        drawSpinner(spriteBatch);
        rotateSpinner();
    }

    private void rotateSpinner() {
        if(Math.abs(rotation) <= 40 || Math.abs(rotation) >= 320) {
            rotation -= Gdx.graphics.getDeltaTime() * 100;
        } else {
            rotation -= Gdx.graphics.getDeltaTime() * 500;
        }
    }

    private void renderAnimateIn2(SpriteBatch spriteBatch) {
        drawSpinner(spriteBatch);

        spinnerScale -= Gdx.graphics.getDeltaTime() * 3;
        if(spinnerScale <= 1.3f) {
            spinnerScale = 1.3f;
            state = State.ANIMATE;
        }
    }

    private void renderAnimateIn(SpriteBatch spriteBatch) {
        drawSpinner(spriteBatch);

        spinnerScale += Gdx.graphics.getDeltaTime() * 3;
        if(spinnerScale >= 1.8f) {
            spinnerScale = 1.8f;
            state = State.ANIMATE_IN_2;
        }
    }

    private void drawSpinner(SpriteBatch spriteBatch) {
        float sWidth = (256 * imgScale) * spinnerScale;
        float sHeight = (256 * imgScale) * spinnerScale;
        spriteBatch.draw(spinner, width / 2 - sWidth / 2, height / 2 - sHeight / 2, sWidth / 2, sHeight / 2, sWidth, sHeight, 1, 1, rotation, 0, 0, spinner.getWidth(), spinner.getHeight(), false, false);
    }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public State getState() {
        return state;
    }

    enum State {
        ANIMATE_IN, ANIMATE, ANIMATE_OUT, ANIMATE_IN_2, ANIMATE_OUT_2, DONE
    }
}
