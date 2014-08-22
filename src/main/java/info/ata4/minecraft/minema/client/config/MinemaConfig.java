/*
 ** 2014 July 28
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.minema.client.config;

import info.ata4.minecraft.minema.util.config.ConfigBoolean;
import info.ata4.minecraft.minema.util.config.ConfigContainer;
import info.ata4.minecraft.minema.util.config.ConfigDouble;
import info.ata4.minecraft.minema.util.config.ConfigEnum;
import info.ata4.minecraft.minema.util.config.ConfigInteger;
import info.ata4.minecraft.minema.util.config.ConfigNumber;
import info.ata4.minecraft.minema.util.config.ConfigString;
import info.ata4.minecraft.minema.util.config.ConfigValue;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MinemaConfig extends ConfigContainer {
    
    private static final int MAX_TEXTURE_SIZE = Minecraft.getGLMaximumTextureSize();
    
    public static final String CATEGORY_ENCODING = "encoding";
    public static final String CATEGORY_CAPTURING = "capturing";
    public static final String CATEGORY_ENGINE = "engine";
    
    private static Set<String> getImageFormats() {
        Set<String> formats = new HashSet<String>();
        
        // add all writer format names and convert them to lower case
        for (String formatName : ImageIO.getWriterFormatNames()) {
            formats.add(formatName.toLowerCase());
        }
        
        // "tga" is always supported
        formats.add("tga");
        
        // "jpeg" is added together with "jpg" for some reason
        formats.remove("jpeg");
        
        return formats;
    }
    
    public final ConfigEnum imageFormat = new ConfigEnum("tga", getImageFormats());
    public final ConfigBoolean useVideoEncoder = new ConfigBoolean(false);
    public final ConfigString videoEncoderPath = new ConfigString("");
    public final ConfigString videoEncoderParams = new ConfigString("");
    
    public final ConfigInteger frameWidth = new ConfigInteger(0, 0, MAX_TEXTURE_SIZE);
    public final ConfigInteger frameHeight = new ConfigInteger(0, 0, MAX_TEXTURE_SIZE);
    public final ConfigDouble frameRate = new ConfigDouble(30.0, 0.01, 1000.0);
    public final ConfigInteger frameLimit = new ConfigInteger(-1, -1);
    public final ConfigString capturePath = new ConfigString("movies");
    public final ConfigBoolean showOverlay = new ConfigBoolean(false);
    
    public final ConfigDouble engineSpeed = new ConfigDouble(1.0, 0.01);
    public final ConfigInteger particleLimit = new ConfigInteger(64000, -1);
    public final ConfigBoolean syncEngine = new ConfigBoolean(true);
    public final ConfigBoolean preloadChunks = new ConfigBoolean(false);
    
    private File movieDir;
    
    public MinemaConfig(Configuration config) {
        super(config);
        
        setLangKeyPrefix("minema.config");
        
        register(imageFormat, "imageFormat", CATEGORY_ENCODING);
        register(useVideoEncoder, "useVideoEncoder", CATEGORY_ENCODING);
        register(videoEncoderPath, "videoEncoderPath", CATEGORY_ENCODING);
        register(videoEncoderParams, "videoEncoderParams", CATEGORY_ENCODING);
        
        register(frameWidth, "frameWidth", CATEGORY_CAPTURING);
        register(frameHeight, "frameHeight", CATEGORY_CAPTURING);
        register(frameRate, "frameRate", CATEGORY_CAPTURING);
        register(frameLimit, "frameLimit", CATEGORY_CAPTURING);
        register(capturePath, "capturePath", CATEGORY_CAPTURING);
        register(showOverlay, "showOverlay", CATEGORY_CAPTURING);
        
        register(engineSpeed, "engineSpeed", CATEGORY_ENGINE);
        register(particleLimit, "particleLimit", CATEGORY_ENGINE);
        register(syncEngine, "syncEngine", CATEGORY_ENGINE);
        register(preloadChunks, "preloadChunks", CATEGORY_ENGINE);
    }
    
    public int getFrameWidth() {
        if (frameWidth.get() == 0 || !OpenGlHelper.isFramebufferEnabled()) {
            return Display.getWidth();
        } else {
            return frameWidth.get();
        }
    }
    
    public int getFrameHeight() {
        if (frameHeight.get() == 0 || !OpenGlHelper.isFramebufferEnabled()) {
            return Display.getHeight();
        } else {
            return frameHeight.get();
        }
    }

    public boolean useFrameSize() {
        return OpenGlHelper.isFramebufferEnabled() && (frameWidth.get() != 0 || frameHeight.get() != 0);
    }
    
    public boolean isSyncEngine() {
        return Minecraft.getMinecraft().isSingleplayer() && syncEngine.get();
    }
    
    public File getMovieDir() {
        return movieDir;
    }

    public void setMovieDir(File movieDir) {
        this.movieDir = movieDir;
    }
}
