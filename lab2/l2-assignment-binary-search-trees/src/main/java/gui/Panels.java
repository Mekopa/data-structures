package gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Two panels can be created in the class object: a table of parameters and a table of buttons
 * grid. The number of objects that can be placed in a panel is determined by the parameters.
 *
 * @author darius.matulis@ktu.lt
 */
public class Panels extends JPanel {

    private final static int SPACING = 4;
    private final List<JTextField> tfs = new ArrayList<>();
    private final List<JButton> btns = new ArrayList<>();

    /**
     * Create parameter table (GridBag option)
     * <pre>
     * |-------------------------------|
     * |                |------------| |
     * |   lblTexts[0]  | tfTexts[0] | |
     * |                |------------| |
     * |                               |
     * |                |------------| |
     * |   lblTexts[1]  | tfTexts[1] | |
     * |                |------------| |
     * |      ...             ...      |
     * |-------------------------------|
     * </pre>
     *
     * @param lblTexts
     * @param tfTexts
     * @param columnWidth
     */
    public Panels(String[] lblTexts, String[] tfTexts, int columnWidth) {
        super();
        if (lblTexts == null || tfTexts == null) {
            throw new IllegalArgumentException("Arguments for table of parameters are incorrect");
        }

        if (lblTexts.length > tfTexts.length) {
            tfTexts = Arrays.copyOf(tfTexts, lblTexts.length);
            Arrays.fill(tfTexts, "");
        }

        initTableOfParameters(columnWidth, lblTexts, tfTexts);
    }

    /**
     * Create buttons grid (GridLayout option)
     * <pre>
     * |-------------------------------------|
     * | |-------------| |-------------|     |
     * | | btnNames[0] | | btnNames[1] | ... |
     * | |-------------| |-------------|     |
     * |                                     |
     * | |-------------| |-------------|     |
     * | | btnNames[2] | | btnNames[3] | ... |
     * | |-------------| |-------------|     |
     * |       ...              ...          |
     * |-------------------------------------|
     * </pre>
     *
     * @param btnNames
     * @param gridX
     * @param gridY
     */
    public Panels(String[] btnNames, int gridX, int gridY) {
        super();
        if (btnNames == null || gridX < 1 || gridY < 1) {
            throw new IllegalArgumentException("Arguments for buttons grid are incorrect");
        }
        initGridOfButtons(gridX, gridY, btnNames);
    }

    private void initTableOfParameters(int columnWidth, String[] lblTexts, String[] tfTexts) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Spacing between components
        c.insets = new Insets(SPACING, SPACING, SPACING, SPACING);
        // Left alignment
        c.anchor = GridBagConstraints.WEST;
        // Select the first column..
        c.gridx = 0;
        // ..and add the labels to it
        Arrays.stream(lblTexts).forEach((lblText) -> add(new JLabel(lblText), c));
        // Select the second column..
        c.gridx = 1;
        // ..and add textfields to it

        for (String tfText : tfTexts) {
            JTextField tf = new JTextField(tfText, columnWidth);
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setBackground(Color.WHITE);
            tfs.add(tf);
            add(tf, c);
        }
    }

    private void initGridOfButtons(int gridX, int gridY, String[] btnNames) {
        setLayout(new GridLayout(gridY, gridX, SPACING, SPACING));
        Queue<String> btnNamesQueue = new LinkedList<>(Arrays.asList(btnNames));
        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                if (btnNamesQueue.isEmpty()) {
                    break;
                }
                JButton button = new JButton(btnNamesQueue.poll());
                btns.add(button);
                add(button);
            }
        }
    }

    /**
     * Returns the list of parameters in the parameter table
     *
     * @return the list of parameters in the parameter table
     */
    public List<String> getParametersOfTable() {
        return tfs.stream().map(JTextComponent::getText).collect(Collectors.toList());
    }

    /**
     * Returns the list of JTextField objects in the parameter table
     *
     * @return the list of JTextField objects in the parameter table
     */
    public List<JTextField> getTfOfTable() {
        return tfs;
    }

    /**
     * Returns the list of JButton grid objects
     *
     * @return the list of JButton grid objects
     */
    public List<JButton> getButtons() {
        return btns;
    }
}
