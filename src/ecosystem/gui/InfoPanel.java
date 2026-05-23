package ecosystem.gui;

import ecosystem.core.Position;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 */
public class InfoPanel extends JPanel {

    private final JLabel    typeLabel;
    private final JLabel    posLabel;
    private final JTextArea detailArea;

    /**
     * Constructs the info panel with a fixed preferred width.
     *
     * @param preferredWidth pixel width
     */
    public InfoPanel(int preferredWidth) {
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Selected Entity",
                TitledBorder.LEFT, TitledBorder.TOP));
        setPreferredSize(new Dimension(preferredWidth, 200));

        JPanel header = new JPanel(new GridLayout(2, 1, 2, 2));
        typeLabel = new JLabel("Type: —");
        posLabel  = new JLabel("Position: —");
        typeLabel.setFont(typeLabel.getFont().deriveFont(Font.BOLD));
        header.add(typeLabel);
        header.add(posLabel);

        detailArea = new JTextArea(6, 20);
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        detailArea.setBackground(UIManager.getColor("Panel.background"));

        add(header,                              BorderLayout.NORTH);
        add(new JScrollPane(detailArea),         BorderLayout.CENTER);

        showEmpty();
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Populates the panel with data from the given entity.
     *
     * @param entity   the entity to display; {@code null} clears the panel
     * @param position the grid position of the entity
     */
    public void display(Object entity, Position position) {
        if (entity == null) {
            showEmpty();
            return;
        }
        typeLabel.setText("Type: " + entity.getClass().getSimpleName());
        posLabel.setText ("Position: (" + position.getCol() + ", " + position.getRow() + ")");
        detailArea.setText(entity.toString());
        detailArea.setCaretPosition(0);
    }

    // ── Private ──────────────────────────────────────────────────────────────

    private void showEmpty() {
        typeLabel.setText("Type: —");
        posLabel.setText ("Position: —");
        detailArea.setText("Click a cell to view entity details.");
    }
}