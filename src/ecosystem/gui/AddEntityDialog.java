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

    private final JComboBox<String> typeCombo;
    private final JSpinner          xSpinner;
    private final JSpinner          ySpinner;
    private final JSpinner          energySpinner;

    private final Environment environment;
    private boolean confirmed = false;

    public AddEntityDialog(JFrame parent, Environment environment) {
        super(parent, "Add Entity to Simulation", true);
        this.environment = environment;

        int maxX = Math.max(0, environment.getCols()  - 1);
        int maxY = Math.max(0, environment.getRows() - 1);

        typeCombo     = new JComboBox<>(EntityFactory.SUPPORTED_TYPES);
        xSpinner      = new JSpinner(new SpinnerNumberModel(0, 0, maxX, 1));
        ySpinner      = new JSpinner(new SpinnerNumberModel(0, 0, maxY, 1));
        energySpinner = new JSpinner(new SpinnerNumberModel(50, 0, 10000, 1));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
        GridBagConstraints l = labelConstraints();
        GridBagConstraints f = fieldConstraints();

        int row = 0;
        l.gridy = row; f.gridy = row++; form.add(new JLabel("Entity Type:"), l); form.add(typeCombo,     f);
        l.gridy = row; f.gridy = row++; form.add(new JLabel("X Position:"),  l); form.add(xSpinner,      f);
        l.gridy = row; f.gridy = row++; form.add(new JLabel("Y Position:"),  l); form.add(ySpinner,      f);
        l.gridy = row; f.gridy = row++; form.add(new JLabel("Energy:"),      l); form.add(energySpinner, f);

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

    public boolean wasConfirmed() {
        return confirmed;
    }

    private void onAdd(ActionEvent e) {
        try {
            int    x    = (int) xSpinner.getValue();
            int    y    = (int) ySpinner.getValue();
            String type = (String) typeCombo.getSelectedItem();

            Position pos = new Position(x, y);

            boolean occupied = environment.getEntities().stream()
                    .anyMatch(ent -> ent.getPosition().equals(pos));

            if (occupied) {
                JOptionPane.showMessageDialog(this,
                        "Position (" + x + ", " + y + ") is already occupied.\n"
                                + "Please choose an empty cell.",
                        "Cell Occupied", JOptionPane.WARNING_MESSAGE);
                return;
            }

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
}