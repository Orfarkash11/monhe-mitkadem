package ecosystem.gui;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * Singleton image cache that loads all entity icons exactly once at startup.
 *
 * <p>Icons are stored as scaled {@link ImageIcon}s keyed by {@link EntityType}.
 * Callers must never store the returned icons in local fields; always retrieve
 * them through {@link #getIcon(EntityType)} to ensure correct sizing.</p>
 *
 * <p>Usage:
 * <pre>{@code
 *   ImageManager.getInstance().initialize(48);   // call once at startup
 *   ImageIcon icon = ImageManager.getInstance().getIcon(EntityType.LION);
 * }</pre>
 * </p>
 */
public final class ImageManager {

    // ── Singleton ───────────────────────────────────────────────────────────

    private static ImageManager instance;

    /** Private constructor — use {@link #getInstance()}. */
    private ImageManager() {}

    /**
     * Returns the single shared instance.
     *
     * @return the ImageManager singleton; never {@code null}
     */
    public static synchronized ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    // ── Entity type enum ────────────────────────────────────────────────────

    /**
     * All renderable entity types, each mapped to an image resource path.
     */
    public enum EntityType {
        LION     ("/images/lion.png"),
        DEER     ("/images/deer.png"),
        RABBIT   ("/images/rabbit.png"),
        OAK_TREE ("/images/oak_tree.png"),
        FLOWER   ("/images/flower.png"),
        ROCK     ("/images/rock.png"),
        WATER    ("/images/water.png"),
        EMPTY    ("/images/empty.png");

        private final String resourcePath;

        EntityType(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        /** Returns the classpath resource path for this entity's icon. */
        public String getResourcePath() {
            return resourcePath;
        }
    }

    // ── Cache ───────────────────────────────────────────────────────────────

    private final Map<EntityType, ImageIcon> cache = new EnumMap<>(EntityType.class);
    private boolean initialized = false;
    private int cellSize = 48;

    // ── Public API ──────────────────────────────────────────────────────────

    /**
     * Loads all entity images into memory, scaled to {@code cellSize × cellSize} pixels.
     * Must be called once before any call to {@link #getIcon(EntityType)}.
     *
     * <p>If an image cannot be loaded, a placeholder {@link ImageIcon} is stored
     * and an error dialog is shown — the application does not crash.</p>
     *
     * @param cellSize the pixel dimension (width = height) to scale each icon to
     */
    public synchronized void initialize(int cellSize) {
        if (initialized) return;
        this.cellSize  = cellSize;

        for (EntityType type : EntityType.values()) {
            cache.put(type, loadIcon(type));
        }
        initialized = true;
    }

    /**
     * Returns the pre-loaded {@link ImageIcon} for the given entity type.
     *
     * @param type the entity type whose icon is requested
     * @return scaled icon; never {@code null}
     * @throws IllegalStateException if {@link #initialize(int)} has not been called
     */
    public ImageIcon getIcon(EntityType type) {
        if (!initialized) {
            throw new IllegalStateException(
                    "ImageManager.initialize() must be called before getIcon().");
        }
        return cache.getOrDefault(type, cache.get(EntityType.EMPTY));
    }

    /**
     * Maps a fully-qualified entity class name to its {@link EntityType}.
     * Extend this method if new entity types are added to the simulation.
     *
     * @param entity the entity whose class will be inspected; may be {@code null}
     * @return the matching EntityType, or {@link EntityType#EMPTY} if unknown / null
     */
    public EntityType resolveType(Object entity) {
        if (entity == null) return EntityType.EMPTY;
        String name = entity.getClass().getSimpleName().toUpperCase();
        return switch (name) {
            case "LION"    -> EntityType.LION;
            case "DEER"    -> EntityType.DEER;
            case "RABBIT"  -> EntityType.RABBIT;
            case "OAKTREE" -> EntityType.OAK_TREE;
            case "FLOWER"  -> EntityType.FLOWER;
            case "ROCK"    -> EntityType.ROCK;
            case "WATER"   -> EntityType.WATER;
            default        -> EntityType.EMPTY;
        };
    }

    // ── Private helpers ─────────────────────────────────────────────────────

    /**
     * Attempts to load and scale one icon from the classpath.
     * Falls back to an empty icon on failure and shows a user-friendly error.
     */
    private ImageIcon loadIcon(EntityType type) {
        try (InputStream is = getClass().getResourceAsStream(type.getResourcePath())) {
            if (is == null) {
                throw new IOException("Resource not found: " + type.getResourcePath());
            }
            Image raw    = ImageIO.read(is);
            Image scaled = raw.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Could not load image for " + type.name() + ":\n" + e.getMessage()
                            + "\nA placeholder will be used.",
                    "Image Load Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return new ImageIcon(); // transparent placeholder
        }
    }
}