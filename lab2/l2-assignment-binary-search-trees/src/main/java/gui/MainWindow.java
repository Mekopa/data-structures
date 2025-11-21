package gui;

import demo.Car;
import demo.CarsGenerator;
import utils.Ks;
import utils.ParsableAvlSet;
import utils.ParsableBstSet;
import utils.ParsableSortedSet;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Swing window
 * <pre>
 *                  MainWindow (BorderLayout)
 *  |-----------------------Center-------------------------|
 *  |  |-----------------scrollOutput-------------------|  |
 *  |  |  |------------------------------------------|  |  |
 *  |  |  |                                          |  |  |
 *  |  |  |                                          |  |  |
 *  |  |  |                 taOutput                 |  |  |
 *  |  |  |                                          |  |  |
 *  |  |  |                                          |  |  |
 *  |  |  |                                          |  |  |
 *  |  |  |                                          |  |  |
 *  |  |  |                                          |  |  |
 *  |  |  |------------------------------------------|  |  |                                                              |  |
 *  |  |------------------------------------------------|  |                                          |
 *  |------------------------South-------------------------|
 *  |  |~~~~~~~~~~~~~~~~~~~scrollSouth~~~~~~~~~~~~~~~~~~|  |
 *  |  |                                                |  |
 *  |  |    |------------||-----------||-----------|    |  |
 *  |  |    | panButtons || panParam1 || panParam2 |    |  |
 *  |  |    |            ||           ||           |    |  |
 *  |  |    |------------||-----------||-----------|    |  |
 *  |  |                                                |  |
 *  |  |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|  |
 *  |------------------------------------------------------|
 * </pre>
 *
 * @author darius.matulis@ktu.lt
 */
public class MainWindow extends JFrame implements ActionListener {

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("edu.ktu.ds.lab2.gui.messages", Locale.US);
    private static final int TF_WIDTH = 8;

    private final JTextArea taOutput = new JTextArea();
    private final JScrollPane scrollOutput = new JScrollPane(taOutput);
    private final JTextField tfDelimiter = new JTextField();
    private final JTextField tfInput = new JTextField();
    private final JComboBox<String> cmbTreeType = new JComboBox<>();
    private final JPanel panSouth = new JPanel();
    private final JScrollPane scrollSouth = new JScrollPane(panSouth);
    private final JPanel panParam2 = new JPanel();
    private MainWindowMenu mainWindowMenu;
    private Panels panParam1, panButtons;

    private static ParsableSortedSet<Car> carsSet;
    private final CarsGenerator carsGenerator = new CarsGenerator();

    private int sizeOfInitialSubSet, sizeOfGenSet, sizeOfLeftSubSet;
    private double shuffleCoef;
    private final String[] errors;

    public MainWindow() {
        errors = new String[]{
                MESSAGES.getString("badSetSize"),
                MESSAGES.getString("badInitialData"),
                MESSAGES.getString("badSetSizes"),
                MESSAGES.getString("badShuffleCoef")
        };
        initComponents();
    }

    private void initComponents() {
        // To ensure that the view always jumps to the bottom when attaching text to a JTextArea
        scrollOutput.getVerticalScrollBar()
                .addAdjustmentListener((AdjustmentEvent e) -> taOutput.select(taOutput.getCaretPosition() * taOutput.getFont().getSize(), 0));
        //======================================================================
        // Button grid is formed (blue). We use the Panels class.
        //======================================================================
        // 4 rows, 2 columns
        panButtons = new Panels(
                new String[]{
                        MESSAGES.getString("button1"),
                        MESSAGES.getString("button2"),
                        MESSAGES.getString("button3"),
                        MESSAGES.getString("button4"),
                        MESSAGES.getString("button5"),
                        MESSAGES.getString("button6")},
                2, 3);
        panButtons.getButtons().forEach((btn) -> btn.addActionListener(this));
        enableButtons(false);
        //======================================================================
        // The first table of parameters (green) is formed. We use the Panels class.
        //======================================================================
        panParam1 = new Panels(
                new String[]{
                        MESSAGES.getString("lblParam11"),
                        MESSAGES.getString("lblParam12"),
                        MESSAGES.getString("lblParam13"),
                        MESSAGES.getString("lblParam14"),
                        MESSAGES.getString("lblParam15")},
                new String[]{
                        MESSAGES.getString("tfParam11"),
                        MESSAGES.getString("tfParam12"),
                        MESSAGES.getString("tfParam13"),
                        MESSAGES.getString("tfParam14"),
                        MESSAGES.getString("tfParam15")},
                TF_WIDTH);
        //======================================================================
        // A second table of parameters (yellowish) is formed.
        //======================================================================
        panParam2.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 6, 3, 4);
        // Left alignment
        c.anchor = GridBagConstraints.WEST;
        // Component expansion to the maximum cell size
        c.fill = GridBagConstraints.BOTH;
        // First column
        c.gridx = 0;
        panParam2.add(new JLabel(MESSAGES.getString("lblParam21")), c);
        panParam2.add(new JLabel(MESSAGES.getString("lblParam22")), c);
        panParam2.add(new JLabel(MESSAGES.getString("lblParam23")), c);
        // Second column
        c.gridx = 1;
        for (String s : new String[]{
                MESSAGES.getString("cmbTreeType1"),
                MESSAGES.getString("cmbTreeType2"),
                MESSAGES.getString("cmbTreeType3")
        }) {
            cmbTreeType.addItem(s);
        }
        cmbTreeType.addActionListener(this);
        panParam2.add(cmbTreeType, c);
        tfDelimiter.setHorizontalAlignment(JTextField.CENTER);
        panParam2.add(tfDelimiter, c);
        // First column again, but this time 2 cells wide
        c.gridx = 0;
        c.gridwidth = 2;
        tfInput.setEditable(false);
        tfInput.setBackground(Color.lightGray);
        panParam2.add(tfInput, c);
        //======================================================================
        // A common parameter panel (dark grey) is formed.
        //======================================================================
        panSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panSouth.add(panButtons);
        panSouth.add(panParam1);
        panSouth.add(panParam2);

        mainWindowMenu = new MainWindowMenu() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    taOutput.setBackground(Color.white);
                    Object command = ae.getSource();
                    if (command.equals(mainWindowMenu.getMenu(0).getItem(0))) {
                        fileChooseMenu();
                    } else if (command.equals(mainWindowMenu.getMenu(0).getItem(1))) {
                        KsGui.ounerr(taOutput, MESSAGES.getString("notImplemented"));
                    } else if (command.equals(mainWindowMenu.getMenu(0).getItem(3))) {
                        System.exit(0);
                    } else if (command.equals(mainWindowMenu.getMenu(1).getItem(0))) {
                        JOptionPane.showOptionDialog(this,
                                MESSAGES.getString("author"),
                                MESSAGES.getString("menuItem21"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new String[]{"OK"},
                                null);
                    }
                } catch (ValidationException e) {
                    KsGui.ounerr(taOutput, e.getMessage());
                } catch (Exception e) {
                    KsGui.ounerr(taOutput, MESSAGES.getString("systemError"));
                    e.printStackTrace(System.out);
                }
            }
        };
        // The menu bar is placed in a window
        setJMenuBar(mainWindowMenu);
        //======================================================================
        // L2 window is formed
        //======================================================================
        JPanel mainWindow = new JPanel();
        mainWindow.setLayout(new BorderLayout());
        // in the centre and in the south we place objects
        mainWindow.add(scrollOutput, BorderLayout.CENTER);
        mainWindow.add(scrollSouth, BorderLayout.SOUTH);

        // The L2 panel is placed in the "content" of this frame
        getContentPane().add(mainWindow);
        appearance();
    }

    private void appearance() {
        // Frames for graphic objects
        TitledBorder outputBorder = new TitledBorder(MESSAGES.getString("border1"));
        outputBorder.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        scrollOutput.setBorder(outputBorder);
        TitledBorder southBorder = new TitledBorder(MESSAGES.getString("border2"));
        southBorder.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        scrollSouth.setBorder(southBorder);

        panParam2.setBackground(new Color(255, 255, 153));// Gelsva
        panParam1.setBackground(new Color(204, 255, 204));// Šviesiai žalia
        panParam1.getTfOfTable().get(2).setEditable(false);
        panParam1.getTfOfTable().get(2).setForeground(Color.red);
        panParam1.getTfOfTable().get(4).setEditable(false);
        panParam1.getTfOfTable().get(4).setBackground(Color.lightGray);
        panButtons.setBackground(new Color(112, 162, 255)); // Blyškiai mėlyna
        panSouth.setBackground(Color.GRAY);
        taOutput.setFont(Font.decode("courier new-12"));
        taOutput.setEditable(false);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            System.gc();
            System.gc();
            System.gc();
            taOutput.setBackground(Color.white);

            Object source = ae.getSource();
            if (source instanceof JButton) {
                handleButtons(source);
            } else if (source instanceof JComboBox && source.equals(cmbTreeType)) {
                enableButtons(false);
            }
        } catch (ValidationException e) {
            if (e.getCode() >= 0 && e.getCode() <= 3) {
                KsGui.ounerr(taOutput, errors[e.getCode()] + ": " + e.getMessage());
                if (e.getCode() == 2) {
                    panParam1.getTfOfTable().get(0).setBackground(Color.red);
                    panParam1.getTfOfTable().get(1).setBackground(Color.red);
                }
            } else if (e.getCode() == 4) {
                KsGui.ounerr(taOutput, MESSAGES.getString("allSetIsPrinted"));
            } else {
                KsGui.ounerr(taOutput, e.getMessage());
            }
        } catch (UnsupportedOperationException e) {
            KsGui.ounerr(taOutput, e.getLocalizedMessage());
        } catch (Exception e) {
            KsGui.ounerr(taOutput, MESSAGES.getString("systemError"));
            e.printStackTrace(System.out);
        }
    }

    private void handleButtons(Object source) throws ValidationException {
        if (source.equals(panButtons.getButtons().get(0))) {
            treeGeneration(null);
        } else if (source.equals(panButtons.getButtons().get(1))) {
            treeIteration();
        } else if (source.equals(panButtons.getButtons().get(2))) {
            treeAdd();
        } else if (source.equals(panButtons.getButtons().get(3))) {
            treeRemove();
        } else if (source.equals(panButtons.getButtons().get(4))) {
            treeRemoveAllOneByOne();
        } else if (source.equals(panButtons.getButtons().get(5))) {
            KsGui.setFormatStartOfLine(true);
            KsGui.ounerr(taOutput, MESSAGES.getString("notImplemented"));
            KsGui.setFormatStartOfLine(false);
        }
    }

    public void treeGeneration(String filePath) throws ValidationException {
        // Read parameters
        readTreeParameters();
        // Creates an array object depending on the choice of tree
        // in cmbTreeType object
        createTree();

        Car[] carsArray;
        // If no file is specified, the following is generated
        if (filePath == null) {
            carsArray = carsGenerator.generateShuffle(sizeOfGenSet, sizeOfInitialSubSet, shuffleCoef);
            panParam1.getTfOfTable().get(2).setText(String.valueOf(sizeOfLeftSubSet = sizeOfGenSet - sizeOfInitialSubSet));
        } else { // Read from file
            carsGenerator.generateShuffle(sizeOfGenSet, 0, shuffleCoef);
            panParam1.getTfOfTable().get(2).setText(String.valueOf(sizeOfLeftSubSet = sizeOfGenSet));
            carsSet.load(filePath);
            carsArray = carsSet.toArray(Car.class);
            // The file is shuffled using the standard Collections.shuffle method.
            Collections.shuffle(Arrays.asList(carsArray), new Random());
        }

        carsSet.clear();
        // The elements of the shuffled array are written into the set
        Arrays.stream(carsArray).forEach(carsSet::add);

        // Output results
        // Set to not count the number of rows to be displayed at the beginning of the row
        KsGui.setFormatStartOfLine(true);
        KsGui.oun(taOutput, carsSet.toVisualizedString(tfDelimiter.getText()), MESSAGES.getString("setInTree"));
        // counts the number of lines to be displayed at the beginning of a row
        KsGui.setFormatStartOfLine(false);
        enableButtons(true);
    }

    private void treeAdd() throws ValidationException {
        KsGui.setFormatStartOfLine(true);
        Car car = carsGenerator.takeCar();
        if (car == null) {
            KsGui.ounerr(taOutput, MESSAGES.getString("setEmpty"));
        } else {
            carsSet.add(car);
            panParam1.getTfOfTable().get(2).setText(String.valueOf(--sizeOfLeftSubSet));
            KsGui.oun(taOutput, car, MESSAGES.getString("setAdd"));
            KsGui.oun(taOutput, carsSet.toVisualizedString(tfDelimiter.getText()));
        }
        KsGui.setFormatStartOfLine(false);
    }

    private void treeRemove() {
        KsGui.setFormatStartOfLine(true);
        if (carsSet.isEmpty()) {
            KsGui.ounerr(taOutput, MESSAGES.getString("setIsEmpty"));
        } else {
            removeRandomElement();
        }
        KsGui.oun(taOutput, carsSet.toVisualizedString(tfDelimiter.getText()));
        KsGui.setFormatStartOfLine(false);
    }

    private void treeRemoveAllOneByOne() {
        KsGui.setFormatStartOfLine(true);
        if (carsSet.isEmpty()) {
            KsGui.ounerr(taOutput, MESSAGES.getString("setIsEmpty"));
        } else {
            KsGui.oun(taOutput, MESSAGES.getString("setRemovalAll"));
            int initialSetSize = carsSet.size();
            for (int i = 0; i < initialSetSize; i++) {
                removeRandomElement();
            }
        }
        KsGui.oun(taOutput, carsSet.toVisualizedString(tfDelimiter.getText()));
        KsGui.setFormatStartOfLine(false);
    }

    private void removeRandomElement(){
        Car auto = carsSet.toArray(Car.class)[new Random().nextInt(carsSet.size())];
        if (auto == null) {
            KsGui.ounerr(taOutput, MESSAGES.getString("sizeCalculationError"));
        } else {
            carsSet.remove(auto);
            KsGui.oun(taOutput, auto, MESSAGES.getString("setRemoval"));
        }
    }
    private void treeIteration() {
        KsGui.setFormatStartOfLine(true);
        if (carsSet.isEmpty()) {
            KsGui.ounerr(taOutput, MESSAGES.getString("setIsEmpty"));
        } else {
            KsGui.oun(taOutput, carsSet, MESSAGES.getString("setIterator"));
        }
        KsGui.setFormatStartOfLine(false);
    }

    private void readTreeParameters() throws ValidationException {
        // A little bit of cosmetics...
        for (int i = 0; i < 2; i++) {
            panParam1.getTfOfTable().get(i).setBackground(Color.WHITE);
        }
        // Parameter values are read. If converting a String
        // an error occurs, a NumberFormatException is thrown. In order to
        // distinguish which JTextfield the error occurred in, MyException is used.
        int i = 0;
        try {
            // replace is used in order to cause an exception in case of
            // a negative number
            sizeOfGenSet = Integer.parseInt(panParam1.getParametersOfTable().get(i).replace("-", "x"));
            sizeOfInitialSubSet = Integer.parseInt(panParam1.getParametersOfTable().get(++i).replace("-", "x"));
            ++i;
            shuffleCoef = Double.parseDouble(panParam1.getParametersOfTable().get(++i).replace("-", "x"));
        } catch (NumberFormatException e) {
            // You can also do this: catch an exception and throw again
            throw new ValidationException(panParam1.getParametersOfTable().get(i), e, i);
        }
    }

    private void createTree() throws ValidationException {
        switch (cmbTreeType.getSelectedIndex()) {
            case 0:
                carsSet = new ParsableBstSet<>(Car::new);
                break;
            case 1:
                carsSet = new ParsableAvlSet<>(Car::new);
                break;
            default:
                enableButtons(false);
                throw new ValidationException(MESSAGES.getString("notImplemented"));
        }
    }

    private void enableButtons(boolean enable) {
        IntStream.rangeClosed(1, 5)
                .filter(i -> i < panButtons.getButtons().size() && panButtons.getButtons().get(i) != null)
                .forEachOrdered(i -> panButtons.getButtons().get(i).setEnabled(enable));
    }

    private void fileChooseMenu() throws ValidationException {
        JFileChooser fc = new JFileChooser(".");

        // Remove "all Files" filter
        // fc.setAcceptAllFileFilterUsed(false);
        // Supplemented by our filters
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                String filename = file.getName();
                // Only directories and txt files are shown
                return file.isDirectory() || filename.endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "*.txt";
            }
        });
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            treeGeneration(file.getAbsolutePath());
        }
    }

    public static void createAndShowGUI() {
        Locale.setDefault(Locale.US); // Unify number formats
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()
                    // Alternatively, the appearance of the swing components will depend on
                    // depending on the OS used:
                    // UIManager.getSystemLookAndFeelClassName()
                    // Either that:
                    // "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
                    // On Linux, also like this:
                    // "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
            );
            UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            Ks.ou(ex.getMessage());
        }
        MainWindow window = new MainWindow();
        window.setLocation(50, 50);
        window.setIconImage(new ImageIcon(MESSAGES.getString("icon")).getImage());
        window.setTitle(MESSAGES.getString("title"));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(1100, 650));
        window.pack();
        window.setVisible(true);
    }
}
