package com.mykovol.Simburde;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SimburdeConfig extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(SimburdeConfig.class);
    private static SimburdeConfig SimburdeConfigInstance = new SimburdeConfig();
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonExit;
    private JTextField mouseLeftUpperAngle;
    private JTextField mouseRightBottomAngle;
    private JTextField mouseSpeed;
    private JTextField sleepInMouseClick;
    private JTextField sleepBeforeMouseMove;
    private JRadioButton oneClickRadioButton;
    private JRadioButton noClickRadioButton;
    private JTextField mouseScroll;
    private JCheckBox interruptByMoveCheckBox;
    private JLabel msgField;
    private JButton checkForUpdatesButton;
    public JButton updateButton;
    private JTextField keyCharacters;
    private String defaultMsg = "F6 - start/stop   Ctrl+Alt+8 - show form";

    public void setDefaultMsg(String msgText) {
        defaultMsg = msgText;
    }

    static SimburdeConfig getInstance() {
        return SimburdeConfigInstance;
    }

    private void initValue() {
        mouseLeftUpperAngle.setText(AppProperties.getLeftUpperAngleX() + ":" + AppProperties.getLeftUpperAngleY());
        mouseRightBottomAngle.setText(AppProperties.getRightBottomAngleX() + ":" + AppProperties.getRightBottomAngleY());
        mouseSpeed.setText(Integer.toString(AppProperties.getMouseSpeed()));
        sleepInMouseClick.setText(Integer.toString(AppProperties.getSleepInMouseClick()));
        sleepBeforeMouseMove.setText(Integer.toString(AppProperties.getSleepBeforeMouseMove()));
        mouseScroll.setText(Integer.toString(AppProperties.getMouseWheelMove()));
        keyCharacters.setText(AppProperties.getKeyCharacters());
        oneClickRadioButton.setSelected(AppProperties.getClickOption() == 1);
        noClickRadioButton.setSelected(AppProperties.getClickOption() == 0);
        interruptByMoveCheckBox.setSelected(AppProperties.isInterruptByMove());
        msgField.setText(defaultMsg);
        updateButton.setVisible(false);
    }

    public void showForm() {
        initValue();
        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }


    public void setMouseLeftUpperAngle() {
        MouesPosition mouseNow = new MouesPosition();

        AppProperties.setLeftUpperAngleX((int) mouseNow.getX());
        AppProperties.setLeftUpperAngleY((int) mouseNow.getY());

        mouseLeftUpperAngle.setText(AppProperties.getLeftUpperAngleX() + ":" + AppProperties.getLeftUpperAngleY());
    }

    public void setMouseRightBottomAngle() {
        MouesPosition mouseNow = new MouesPosition();

        AppProperties.setRightBottomAngleX((int) mouseNow.getX());
        AppProperties.setRightBottomAngleY((int) mouseNow.getY());

        mouseRightBottomAngle.setText(AppProperties.getRightBottomAngleX() + ":" + AppProperties.getRightBottomAngleY());
    }


    public SimburdeConfig() {
        setTitle("Simburde (aka ИБД) - " + SimburdeConfig.class.getPackage().getImplementationVersion() + " by MykoVol");
        setContentPane(contentPane);
        pack();
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        buttonExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onClose();

            }
        });
        buttonOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onOK();

            }
        });
        contentPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) onCancel();
            }
        });
        buttonOK.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) onOK();
            }
        });
        buttonOK.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) onOK();
            }
        });
        checkForUpdatesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onCheckForUpdates();
            }
        });
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onUpdate();
            }
        });
    }

    private void onUpdate() {
        try {
//            ProcessBuilder builder = new ProcessBuilder("cmd", "/b", "applyUpdate.bat");
//            builder.start();
            Runtime.getRuntime().exec("cmd start /C applyUpdate.bat");
//            final int exitVal = process.waitFor();
//            if (exitVal == 0)
            System.exit(0);
//        } catch (InterruptedException e) {
//            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error("cannot find apply bat", e);
        }
    }

    private void onCheckForUpdates() {
        Thread updateThread = new Thread(() -> new AppUpdate());
        updateThread.start();
    }

    public void setMsg(String msg) {
        msgField.setText(msg);
    }

    private int toInt(String s) {
        int res;
        try {
            res = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (!s.isEmpty()) LOGGER.error("Value " + s + " is incorrect and will be set no 0");
            res = 0;
        }

        return res;
    }

    public void saveSettings() {
        AppProperties.setMouseSpeed(toInt(mouseSpeed.getText()));
        AppProperties.setSleepInMouseClick(toInt(sleepInMouseClick.getText()));
        AppProperties.setSleepBeforeMouseMove(toInt(sleepBeforeMouseMove.getText()));
        AppProperties.setMouseWheelMove(toInt(mouseScroll.getText()));
        AppProperties.setKeyCharacters(keyCharacters.getText());
        if (oneClickRadioButton.isSelected()) AppProperties.setClickOption(1);
        if (noClickRadioButton.isSelected()) AppProperties.setClickOption(0);
        AppProperties.setInterruptByMove(interruptByMoveCheckBox.isSelected());

        AppProperties.saveProperties();
    }

    private void onOK() {
        saveSettings();

        setVisible(false);
    }

    private void onCancel() {
        setVisible(false);
    }

    private void onClose() {
        System.exit(0);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setEnabled(false);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        buttonOK.setToolTipText("save properties");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonExit = new JButton();
        buttonExit.setText("Exit");
        buttonExit.setToolTipText("exit application. To hide press Escape, OK or Close window");
        panel1.add(buttonExit, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        msgField = new JLabel();
        msgField.setEnabled(false);
        msgField.setForeground(new Color(-4473925));
        msgField.setInheritsPopupMenu(true);
        msgField.setText("F6 - start/stop   Ctrl+Alt+8 - show form ");
        panel1.add(msgField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mouseLeftUpperAngle = new JTextField();
        mouseLeftUpperAngle.setEditable(false);
        mouseLeftUpperAngle.setToolTipText("to set press Ctrl+Alt+'('");
        panel3.add(mouseLeftUpperAngle, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        mouseRightBottomAngle = new JTextField();
        mouseRightBottomAngle.setEditable(false);
        mouseRightBottomAngle.setToolTipText("to set press Ctrl+Alt+')'");
        panel3.add(mouseRightBottomAngle, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Left-Top point");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Right-Bottom point");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Mouse speed");
        panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mouseSpeed = new JTextField();
        mouseSpeed.setEnabled(true);
        mouseSpeed.setText("20");
        mouseSpeed.setToolTipText("mouse delta speed between two points");
        panel3.add(mouseSpeed, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sleepInMouseClick = new JTextField();
        sleepInMouseClick.setText("50");
        sleepInMouseClick.setToolTipText("random sleep(ms) in mouse click");
        panel3.add(sleepInMouseClick, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Sleep");
        panel3.add(label4, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        oneClickRadioButton = new JRadioButton();
        oneClickRadioButton.setSelected(true);
        oneClickRadioButton.setText("One click");
        oneClickRadioButton.setToolTipText("one left mouse click and scroll after sleep");
        panel3.add(oneClickRadioButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        noClickRadioButton = new JRadioButton();
        noClickRadioButton.setText("No click");
        noClickRadioButton.setToolTipText("double left mouse click + escape after sleep");
        panel3.add(noClickRadioButton, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sleepBeforeMouseMove = new JTextField();
        sleepBeforeMouseMove.setText("100");
        sleepBeforeMouseMove.setToolTipText("random sleep(ms) after click");
        panel3.add(sleepBeforeMouseMove, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        mouseScroll = new JTextField();
        mouseScroll.setText("5");
        mouseScroll.setToolTipText("scroll up and down");
        panel3.add(mouseScroll, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Scroll");
        panel3.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        checkForUpdatesButton = new JButton();
        checkForUpdatesButton.setText("Check for Updates");
        panel3.add(checkForUpdatesButton, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        interruptByMoveCheckBox = new JCheckBox();
        interruptByMoveCheckBox.setSelected(true);
        interruptByMoveCheckBox.setText("Interrupt by move");
        panel3.add(interruptByMoveCheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Update");
        updateButton.setVisible(true);
        panel3.add(updateButton, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        keyCharacters = new JTextField();
        keyCharacters.setToolTipText("set of characters that will be pressed after click");
        panel3.add(keyCharacters, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Characters");
        panel3.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(oneClickRadioButton);
        buttonGroup.add(noClickRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
