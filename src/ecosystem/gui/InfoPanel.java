package ecosystem.gui;

import ecosystem.core.Position;
import ecosystem.entities.LivingEntity;
import ecosystem.decorators.EntityDecorator;
import ecosystem.decorators.PoisonedDecorator;
import ecosystem.decorators.SpeedBoostDecorator;

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

    // --- שדות חדשים עבור הדקורטור ---
    private JButton applyPoisonBtn;
    private JButton applySpeedBtn;
    private LivingEntity currentSelectedEntity = null;

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
        setPreferredSize(new Dimension(preferredWidth, 230)); // הגדלתי מעט את הגובה כדי שיהיה מקום לכפתורים

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

        // --- הוספת אזור הכפתורים בתחתית ---
        applyPoisonBtn = new JButton("start poisoning");
        applySpeedBtn = new JButton("start speed boost");
        applyPoisonBtn.setEnabled(false);
        applySpeedBtn.setEnabled(false);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 4, 4));
        buttonPanel.add(applyPoisonBtn);
        buttonPanel.add(applySpeedBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- הגדרת פעולות הלחיצה על הכפתורים ---
        applyPoisonBtn.addActionListener(e -> {
            if (currentSelectedEntity != null && currentSelectedEntity.getEngine() != null) {
                PoisonedDecorator pd = new PoisonedDecorator(currentSelectedEntity);
                currentSelectedEntity.getEngine().replaceEntity(currentSelectedEntity, pd);
                currentSelectedEntity = pd; // עדכון הרפרנס
                // כיבוי הכפתורים כדי לא לאפשר לשים דקורטור על דקורטור
                applyPoisonBtn.setEnabled(false);
                applySpeedBtn.setEnabled(false);
            }
        });

        applySpeedBtn.addActionListener(e -> {
            if (currentSelectedEntity != null && currentSelectedEntity.getEngine() != null) {
                SpeedBoostDecorator sd = new SpeedBoostDecorator(currentSelectedEntity);
                currentSelectedEntity.getEngine().replaceEntity(currentSelectedEntity, sd);
                currentSelectedEntity = sd; // עדכון הרפרנס
                // כיבוי הכפתורים כדי לא לאפשר לשים דקורטור על דקורטור
                applyPoisonBtn.setEnabled(false);
                applySpeedBtn.setEnabled(false);
            }
        });

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
            // איפוס הבחירה וכיבוי הכפתורים
            currentSelectedEntity = null;
            applyPoisonBtn.setEnabled(false);
            applySpeedBtn.setEnabled(false);
            return;
        }
        typeLabel.setText("Type: " + entity.getClass().getSimpleName());
        posLabel.setText ("Position: (" + position.getCol() + ", " + position.getRow() + ")");
        detailArea.setText(entity.toString());
        detailArea.setCaretPosition(0);

        // --- הלוגיקה שמדליקה את הכפתורים רק אם נבחרה ישות חיה שאינה כבר דקורטור ---
        if (entity instanceof LivingEntity && !(entity instanceof EntityDecorator)) {
            currentSelectedEntity = (LivingEntity) entity;
            applyPoisonBtn.setEnabled(true);
            applySpeedBtn.setEnabled(true);
        } else {
            currentSelectedEntity = null;
            applyPoisonBtn.setEnabled(false);
            applySpeedBtn.setEnabled(false);
        }
    }

    // ── Private ──────────────────────────────────────────────────────────────

    private void showEmpty() {
        typeLabel.setText("Type: —");
        posLabel.setText ("Position: —");
        detailArea.setText("Click a cell to view entity details.");
    }
}