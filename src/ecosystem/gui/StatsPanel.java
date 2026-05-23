package ecosystem.gui;

import ecosystem.core.Environment;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;
import ecosystem.gui.observer.SimulationEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 */
public class StatsPanel extends JPanel {

    private final JLabel tickLabel;
    private final JLabel energyLabel;
    private final JLabel entityCountLabel;

    /** Maps entity simple class name → count label for quick updates. */
    private final Map<String, JLabel> countLabels = new LinkedHashMap<>();

    // Known entity types — extend this list as needed
    private static final String[] ENTITY_TYPES = {
            "Lion", "Deer", "Rabbit", "OakTree", "Flower", "Rock", "Water"
    };

    /**
     * Constructs the stats panel.
     *
     * @param preferredWidth pixel width
     */
    public StatsPanel(int preferredWidth) {
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Live Statistics",
                TitledBorder.LEFT, TitledBorder.TOP));
        setPreferredSize(new Dimension(preferredWidth, 220));

        JPanel grid = new JPanel(new GridLayout(0, 2, 4, 4));

        // Global stats
        tickLabel        = addRow(grid, "Ticks:");
        energyLabel      = addRow(grid, "Total Energy:");
        entityCountLabel = addRow(grid, "Total Entities:");

        // Per-type counts
        for (String type : ENTITY_TYPES) {
            countLabels.put(type, addRow(grid, type + ":"));
        }

        add(grid, BorderLayout.NORTH);
        reset();
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Refreshes all displayed statistics from the latest simulation event.
     * Must be called on the EDT.
     *
     * @param event the latest simulation snapshot
     */
    public void update(SimulationEvent event) {
        Environment env = event.getEnvironment();
        var entities    = env.getEntities();

        tickLabel.setText(String.valueOf(event.getTickCount()));

        double totalEnergy = entities.stream()
                .filter(e -> e instanceof LivingEntity)
                .mapToDouble(e -> ((LivingEntity) e).getEnergy())
                .sum();
        energyLabel.setText(String.format("%.1f", totalEnergy));
        entityCountLabel.setText(String.valueOf(entities.size()));

        for (String type : ENTITY_TYPES) {
            long count = entities.stream()
                    .filter(e -> e.getClass().getSimpleName().equals(type))
                    .count();
            JLabel lbl = countLabels.get(type);
            if (lbl != null) lbl.setText(String.valueOf(count));
        }
    }

    // ── Private ──────────────────────────────────────────────────────────────

    private void reset() {
        tickLabel.setText("0");
        energyLabel.setText("0.0");
        entityCountLabel.setText("0");
        countLabels.values().forEach(l -> l.setText("0"));
    }

    /**
     * Adds a label–value row to a grid panel and returns the value label.
     */
    private JLabel addRow(JPanel grid, String labelText) {
        JLabel key = new JLabel(labelText, SwingConstants.RIGHT);
        key.setFont(key.getFont().deriveFont(Font.BOLD, 11f));
        JLabel val = new JLabel("—", SwingConstants.LEFT);
        grid.add(key);
        grid.add(val);
        return val;
    }
}