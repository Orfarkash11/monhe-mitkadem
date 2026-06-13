package ecosystem.gui;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.factories.EntityFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 */
public class AddEntityDialog extends JDialog {

    // ── Input controls ───────────────────────────────────────────────────────
    private final JComboBox<String> typeCombo;
    private final JSpinner          xSpinner;
    private final JSpinner          ySpinner;
    private final JSpinner          energySpinner;

    // ── State ────────────────────────────────────────────────────────────────
    private final Environment environment;
    private boolean confirmed = false;

    /** All entity type names presented in the dropdown. */
    private static final String[] ENTITY_NAMES = {
            "Lion", "Deer", "Rabbit", "OakTree", "Flower", "Rock", "Water"
    };

    /**
     * Constructs the dialog.
     *
     * @param parent      owner frame for centering; may be {@code null}
     * @param environment target environment where the new entity will be placed
     */
    public AddEntityDialog(JFrame parent, Environment environment) {
        super(parent, "Add Entity to Simulation", true); // modal
        this.environment = environment;

        int maxX = environment.getCols()  - 1;
        int maxY = environment.getRows() - 1;

        // ── Form components ──────────────────────────────────────────────
        typeCombo     = new JComboBox<>(ENTITY_NAMES);
        xSpinner      = new JSpinner(new SpinnerNumberModel(0, 0, maxX, 1));
        ySpinner      = new JSpinner(new SpinnerNumberModel(0, 0, maxY, 1));
        // Use integer model for energy values (energies in the model are integers)
        energySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));

        // ── Layout ───────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
        GridBagConstraints l = labelConstraints();
        GridBagConstraints f = fieldConstraints();

        int row = 0;
        l.gridy = row; f.gridy = row++; form.add(new JLabel("Entity Type:"), l); form.add(typeCombo,     f);
        l.gridy = row; f.gridy = row++; form.add(new JLabel("X Position:"),  l); form.add(xSpinner,      f);
        l.gridy = row; f.gridy = row++; form.add(new JLabel("Y Position:"),  l); form.add(ySpinner,      f);
        l.gridy = row; f.gridy = row++; form.add(new JLabel("Energy:"),      l); form.add(energySpinner, f);

        // Initialize energy field based on initial selection
        updateEnergyField((String) typeCombo.getSelectedItem());

        // When the user changes the type, update the shown energy to the class default
        typeCombo.addActionListener(ev -> updateEnergyField((String) typeCombo.getSelectedItem()));

        // ── Buttons ───────────────────────────────────────────────────────
        JButton btnOK     = new JButton("Add");
        JButton btnCancel = new JButton("Cancel");
        btnOK    .addActionListener(this::onAdd);
        btnCancel.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnOK);
        btnPanel.add(btnCancel);

        setLayout(new BorderLayout());
        add(form,     BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Returns {@code true} if the user clicked "Add" and the entity was
     * successfully created and inserted into the environment.
     *
     * @return {@code true} on confirmed valid submission
     */
    public boolean wasConfirmed() {
        return confirmed;
    }

    // ── Action handler ───────────────────────────────────────────────────────

    /**
     * Validates inputs, constructs the appropriate entity, and adds it to
     * the environment. Shows a friendly error dialog on any invalid input.
     */
    private void onAdd(ActionEvent e) {
        try {
            int    x    = (int) xSpinner.getValue();
            int    y    = (int) ySpinner.getValue();
            String type = (String) typeCombo.getSelectedItem();

            Position pos = new Position(x, y);

            // Check that the cell is not already occupied
            boolean occupied = environment.getEntities().stream()
                    .anyMatch(ent -> ent.getPosition().equals(pos));

            if (occupied) {
                JOptionPane.showMessageDialog(this,
                        "Position (" + x + ", " + y + ") is already occupied.\n"
                                + "Please choose an empty cell.",
                        "Cell Occupied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Construct the entity using the project's existing constructors.
            // Note: many entity classes set their own initial energy internally
            // (e.g., Lion sets energy to 100). We show that default energy in
            // the dialog, but we cannot override it here because the
            // constructors do not accept an energy parameter.
            int energy = (int) energySpinner.getValue();
            AbstractEntity entity = EntityFactory.createEntity(type, pos, energy);

            environment.addEntity(entity);

            confirmed = true;
            dispose();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number format: " + nfe.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this,
                    "Invalid argument: " + iae.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Layout helpers ───────────────────────────────────────────────────────

    private GridBagConstraints labelConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx   = 0;
        c.anchor  = GridBagConstraints.LINE_END;
        c.insets  = new Insets(4, 4, 4, 8);
        return c;
    }

    private GridBagConstraints fieldConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx   = 1;
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets  = new Insets(4, 0, 4, 4);
        return c;
    }

    /**
     * Sets the energy spinner to the default value for the given entity type.
     * If the entity doesn't use a configurable energy (e.g. Rock, Water) the
     * spinner is disabled and set to 0.
     */
    private void updateEnergyField(String type) {
        int value;
        int max = 10000;
        boolean enabled = true;

        switch (type) {
            case "Lion":    value = 100; max = 150; break; // matches Lion constructor
            case "Deer":    value = 70;  max = 100; break;
            case "Rabbit":  value = 50;  max = 80;  break;
            case "OakTree": value = 80;  max = 120; break;
            case "Flower":  value = 30;  max = 70;  break;
            case "Rock":    value = 0;   enabled = false; break;
            case "Water":   value = 0;   enabled = false; break;
            default:         value = 0;   enabled = false; break;
        }

        SpinnerNumberModel model = (SpinnerNumberModel) energySpinner.getModel();
        model.setMinimum(0);
        model.setMaximum(max);
        model.setValue(value);
        energySpinner.setEnabled(enabled);
        energySpinner.setToolTipText(enabled
                ? "Default initial energy for " + type + " (cannot be overridden here)"
                : "No energy for this resource type");
    }
}