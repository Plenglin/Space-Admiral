package xyz.plenglin.spaceadmiral.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import xyz.plenglin.spaceadmiral.SpaceAdmiral;

public final class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.resizable = false;
        new LwjglApplication(new SpaceAdmiral(), config);
    }
}
