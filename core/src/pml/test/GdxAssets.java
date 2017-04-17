package pml.test;

/**
 * Created by Gerber Lóránt on 2017. 03. 14..
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.oscim.backend.AssetAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * LibGDX asset adapter for VTM
 */
public class GdxAssets extends AssetAdapter {
    private static final Logger log = LoggerFactory.getLogger(GdxAssets.class);

    private static String pathPrefix = "";

    private GdxAssets(String path) {
        pathPrefix = path;
    }

    @Override
    public InputStream openFileAsStream(String fileName) {
        FileHandle file = Gdx.files.internal(pathPrefix + fileName);
        if (file == null)
            throw new IllegalArgumentException("missing file " + fileName);

        try {
            return file.read();
        } catch (GdxRuntimeException e) {
            log.debug(e.getMessage());
            return null;
        }
    }

    public static void init(String path) {
        AssetAdapter.init(new GdxAssets(path));
    }
}
