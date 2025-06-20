package app.project.view;

import app.project.controller.GameController;
import app.project.utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static app.project.model.AppStage.MAIN_MENU;

public class MainMenuPanel extends JPanel {

    private static final int PORT = 12345;

    private final GameController gameController;
    private final Runnable goToSetupFunction;
    private final Runnable goToGameFunction;

    private JTextField ipInput;
    private Border defaultBorder;
    private JCheckBox testModeCheckbox;
    private JCheckBox autoPlaceCheckbox;
    private JCheckBox foeShipsVisibleCheckbox;

    public MainMenuPanel(GameController gameController,
                         Runnable goToSetupFunction,
                         Runnable goToGameFunction) {
        this.gameController = gameController;
        this.goToSetupFunction = goToSetupFunction;
        this.goToGameFunction = goToGameFunction;
        initComponents();
    }

    private void initComponents() {
        setName(MAIN_MENU.name());
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(initSettingsPanel(), BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Witaj w grze w statki!");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        Dimension buttonSize = new Dimension(300, 80);
        JButton hostButton = new JButton("Załóż serwer");
        hostButton.setFont(hostButton.getFont().deriveFont(Font.BOLD, 16));
        hostButton.setPreferredSize(buttonSize);
        hostButton.setMaximumSize(buttonSize);
        hostButton.setMinimumSize(buttonSize);
        hostButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton clientButton = new JButton("Dołącz do gry");
        clientButton.setFont(clientButton.getFont().deriveFont(Font.BOLD, 16));
        clientButton.setPreferredSize(buttonSize);
        clientButton.setMaximumSize(buttonSize);
        clientButton.setMinimumSize(buttonSize);
        clientButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(hostButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(clientButton);
        add(centerPanel, BorderLayout.CENTER);

        hostButton.addActionListener(_ -> serverOnClickAction());
        clientButton.addActionListener(_ -> clientOnClickAction());
    }

    private JPanel initSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel ipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel ipLabel = new JLabel("Adres IP:");
        ipLabel.setFont(ipLabel.getFont().deriveFont(Font.PLAIN, 14));
        ipInput = new JTextField("localhost", 12);
        defaultBorder = ipInput.getBorder();
        ipInput.setFont(ipInput.getFont().deriveFont(Font.PLAIN, 14));
        ipInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateIpInput(ipInput.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateIpInput(ipInput.getText()); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateIpInput(ipInput.getText()); }
        });
        ipPanel.add(ipLabel);
        ipPanel.add(ipInput);
        ipPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        testModeCheckbox = new JCheckBox("Włącz tryb testowy");
        testModeCheckbox.setFont(testModeCheckbox.getFont().deriveFont(Font.PLAIN, 14));
        testModeCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        autoPlaceCheckbox = new JCheckBox("Wstępnie ustaw statki");
        autoPlaceCheckbox.setFont(autoPlaceCheckbox.getFont().deriveFont(Font.PLAIN, 14));
        autoPlaceCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        foeShipsVisibleCheckbox = new JCheckBox("Pokaż statki przeciwnika");
        foeShipsVisibleCheckbox.setFont(foeShipsVisibleCheckbox.getFont().deriveFont(Font.PLAIN, 14));
        foeShipsVisibleCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        settingsPanel.add(ipPanel);
        settingsPanel.add(Box.createVerticalStrut(5));
        settingsPanel.add(testModeCheckbox);
        settingsPanel.add(autoPlaceCheckbox);
        settingsPanel.add(foeShipsVisibleCheckbox);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        return settingsPanel;
    }

    private void serverOnClickAction() {
        gameController.createServer(PORT, goToGameFunction, goToSetupFunction);
        System.out.println("Serwer przechodzi do edycji.");
    }

    private void clientOnClickAction() {
        gameController.createClientSocket(ipInput.getText(), PORT, goToSetupFunction, goToGameFunction, this::showErrorMsgDialog);
        System.out.println("Klient przechodzi do edycji.");
    }

    private void validateIpInput(String ip) {;
        if (ValidationUtils.ipIsValid(ip)) {
            ipInput.setBorder(defaultBorder);
            ipInput.setBackground(Color.WHITE);
        } else {
            ipInput.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            ipInput.setBackground(new Color(255, 228, 225));
        }
    }

    private void showErrorMsgDialog() {
        JOptionPane.showConfirmDialog(this, "Nie udało się odnaleźć serwera gry. Załóż serwer lub poczekaj aż drugi gracz go założy.", "Nie odnaleziono serwera", JOptionPane.DEFAULT_OPTION);
    }
}
