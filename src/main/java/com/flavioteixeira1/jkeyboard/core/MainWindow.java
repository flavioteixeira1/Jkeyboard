package com.flavioteixeira1.jkeyboard.core;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MainWindow extends JFrame {
    private JComboBox<String> perfilCombo;
    private JButton addPerfil, removePerfil, renomearPerfil;
    private JButton importBtn, exportBtn, saveBtn, revertBtn;
    private JTabbedPane joystickTabs;
    private List<DevicePanel> devicePanels = new ArrayList<>();
    private JoystickManager joystickManager1, joystickManager2;
    private javax.swing.Timer uiUpdateTimer;

    public MainWindow() {
        super("Jkeyboard (QJoypad java clone)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Top Bar ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        perfilCombo = new JComboBox<>(new String[]{"Master_System_Osmose"});
        addPerfil = new JButton("+");
        removePerfil = new JButton("-");
        renomearPerfil = new JButton("Renomear");
        
        topBar.add(new JLabel("Perfil:"));
        topBar.add(perfilCombo);
        topBar.add(addPerfil); 
        topBar.add(removePerfil); 
        topBar.add(renomearPerfil);

        importBtn = new JButton("Importar");
        exportBtn = new JButton("Exportar");
        saveBtn = new JButton("Salvar");
        revertBtn = new JButton("Reverter");
        
        topBar.add(Box.createHorizontalStrut(20));
        topBar.add(importBtn); 
        topBar.add(exportBtn); 
        topBar.add(saveBtn); 
        topBar.add(revertBtn);

        add(topBar, BorderLayout.NORTH);

        // --- Joystick Tabs ---
        joystickTabs = new JTabbedPane();
        
        // Inicializar joysticks
        joystickManager1 = JoystickManager.getInstanceForPlayer(0);
        joystickManager2 = JoystickManager.getInstanceForPlayer(1);
        
        // Painel para Player 1
        DevicePanel panel1 = new DevicePanel(joystickManager1, 0);
        joystickTabs.addTab("Player 1", createPlayerPanel(panel1, 0));
        
        // Painel para Player 2  
        DevicePanel panel2 = new DevicePanel(joystickManager2, 1);
        joystickTabs.addTab("Player 2", createPlayerPanel(panel2, 1));
        
        // Painel de status
        JPanel statusPanel = new JPanel(new BorderLayout());
        JTextArea statusArea = new JTextArea(5, 50);
        statusArea.setEditable(false);
        statusArea.setText(JoystickManager.getGlobalStatus());
        statusPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton("Atualizar Status");
        refreshBtn.addActionListener(e -> {
            statusArea.setText(JoystickManager.getGlobalStatus());
        });
        statusPanel.add(refreshBtn, BorderLayout.SOUTH);
        
        joystickTabs.addTab("Status", statusPanel);
        
        add(joystickTabs, BorderLayout.CENTER);

        // --- Bottom Bar ---
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton clearBtn = new JButton("Limpar");
        JButton quickSetBtn = new JButton("Configuração Rápida");
        JButton toggleMappingBtn = new JButton("Usar Mapeamento Customizado");
        JButton closeBtn = new JButton("Fechar Diálogo");
        JButton quitBtn = new JButton("Sair");
        
        toggleMappingBtn.addActionListener(e -> {
            boolean useCustom = !joystickManager1.getUseCustomMapping();
            joystickManager1.setUseCustomMapping(useCustom);
            joystickManager2.setUseCustomMapping(useCustom);
            toggleMappingBtn.setText(useCustom ? "Usar Mapeamento Padrão" : "Usar Mapeamento Customizado");
        });
        
        bottomBar.add(clearBtn); 
        bottomBar.add(quickSetBtn);
        bottomBar.add(toggleMappingBtn);
        bottomBar.add(closeBtn); 
        bottomBar.add(quitBtn);
        add(bottomBar, BorderLayout.SOUTH);

        // Configurar ações
        clearBtn.addActionListener(e -> clearAllMappings());
        quickSetBtn.addActionListener(e -> showQuickSetupDialog());
        closeBtn.addActionListener(e -> this.setVisible(false));
        quitBtn.addActionListener(e -> {
            JoystickManager.globalCleanup();
            System.exit(0);
        });

        // Iniciar timer para atualizar UI
        startUIUpdateTimer();
    }
    
    private JPanel createPlayerPanel(DevicePanel devicePanel, int playerId) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(devicePanel, BorderLayout.CENTER);
        
        // Adicionar informações do joystick
        JLabel infoLabel = new JLabel("Joystick: " + 
            (playerId == 0 ? joystickManager1.getJoystickName() : joystickManager2.getJoystickName()));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(infoLabel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private void startUIUpdateTimer() {
        uiUpdateTimer = new javax.swing.Timer(50, e -> { // 20 FPS
            for (DevicePanel panel : devicePanels) {
                panel.updateUIState();
            }
        });
        uiUpdateTimer.start();
    }
    
    private void clearAllMappings() {
        int option = JOptionPane.showConfirmDialog(this,
            "Deseja limpar todos os mapeamentos?",
            "Confirmar limpeza",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            joystickManager1.setUseCustomMapping(false);
            joystickManager2.setUseCustomMapping(false);
            
            // Recarregar mapeamento padrão
            JoystickManager.globalCleanup();
            joystickManager1 = JoystickManager.getInstanceForPlayer(0);
            joystickManager2 = JoystickManager.getInstanceForPlayer(1);
            
            // Atualizar painéis
            for (DevicePanel panel : devicePanels) {
                panel.updateButtonLabels();
            }
        }
    }
    
    private void showQuickSetupDialog() {
        JDialog dialog = new JDialog(this, "Configuração Rápida", true);
        dialog.setLayout(new BorderLayout());
        
        JTextArea instructions = new JTextArea(
            "Configuração Rápida:\n\n" +
            "1. Clique em qualquer botão na tela principal\n" +
            "2. Pressione a tecla desejada no teclado\n" +
            "3. Para eixos: configure direções separadamente\n" +
            "4. Use 'Usar Mapeamento Customizado' para ativar\n\n" +
            "Dica: Pressione ESC para cancelar durante a captura."
        );
        instructions.setEditable(false);
        instructions.setMargin(new Insets(10, 10, 10, 10));
        
        dialog.add(new JScrollPane(instructions), BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Fechar");
        closeBtn.addActionListener(e -> dialog.dispose());
        dialog.add(closeBtn, BorderLayout.SOUTH);
        
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Painel para cada dispositivo de entrada
    public class DevicePanel extends JPanel {
        private JButton[] axisBtns = new JButton[4];    // Eixos (X, Y, Z, RZ)
        private JButton[] buttonBtns = new JButton[12]; // Botões comuns
        private JButton[] povBtns = new JButton[4];     // Direcionais (POV)
        private JoystickManager joystickManager;
        private int playerId;
        private Map<Integer, Boolean> buttonStates = new HashMap<>();
        private Map<String, Boolean> axisStates = new HashMap<>();
        
        public DevicePanel(JoystickManager manager, int playerId) {
            this.joystickManager = manager;
            this.playerId = playerId;
            
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Título
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            add(new JLabel("Controles - Player " + (playerId + 1), JLabel.CENTER), gbc);
            
            // Separador
            gbc.gridy++;
            add(new JSeparator(), gbc);
            
            // Eixos Analógicos
            gbc.gridy++;
            gbc.gridwidth = 1;
            add(new JLabel("Eixos Analógicos:", JLabel.LEFT), gbc);
            
            gbc.gridy++;
            String[] axisNames = {"Eixo X", "Eixo Y", "Eixo Z", "Eixo RZ"};
            for (int i = 0; i < axisBtns.length; i++) {
                axisBtns[i] = new JButton(axisNames[i] + ": [Não configurado]");
                axisBtns[i].setPreferredSize(new Dimension(180, 30));
                final int axisIdx = i;
                axisBtns[i].addActionListener(e -> configureAxis(axisIdx));
                
                gbc.gridx = i % 2;
                gbc.gridy = 3 + (i / 2);
                add(axisBtns[i], gbc);
            }
            
            // Direcionais (POV/Hat)
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.gridwidth = 3;
            add(new JLabel("Direcionais (POV/Hat):", JLabel.LEFT), gbc);
            
            gbc.gridy++;
            gbc.gridwidth = 1;
            String[] povNames = {"Cima", "Baixo", "Esquerda", "Direita"};
            for (int i = 0; i < povBtns.length; i++) {
                povBtns[i] = new JButton(povNames[i] + ": [Não configurado]");
                povBtns[i].setPreferredSize(new Dimension(150, 30));
                final int povIdx = i;
                povBtns[i].addActionListener(e -> configurePOV(povIdx));
                
                gbc.gridx = i;
                gbc.gridy = 9;
                add(povBtns[i], gbc);
            }
            
            // Botões
            gbc.gridx = 0;
            gbc.gridy = 10;
            gbc.gridwidth = 3;
            add(new JLabel("Botões:", JLabel.LEFT), gbc);
            
            gbc.gridy++;
            gbc.gridwidth = 1;
            for (int i = 0; i < buttonBtns.length; i++) {
                buttonBtns[i] = new JButton("Botão " + (i+1) + ": [Não configurado]");
                buttonBtns[i].setPreferredSize(new Dimension(140, 30));
                final int btnIdx = i;
                buttonBtns[i].addActionListener(e -> configureButton(btnIdx));
                
                gbc.gridx = i % 3;
                gbc.gridy = 12 + (i / 3);
                add(buttonBtns[i], gbc);
            }
            
            devicePanels.add(this);
            updateButtonLabels();
        }
        
        private void configureButton(int buttonIndex) {
            String label = "Botão " + (buttonIndex + 1) + " - Player " + (playerId + 1);
            int keyCode = KeyCaptureDialog.capture(
                ConfigDialog.getCurrentInstance(), label);
            
            if (keyCode > 0) {
                joystickManager.setCustomButtonMapping(buttonIndex, keyCode);
                buttonBtns[buttonIndex].setText("Botão " + (buttonIndex + 1) + ": " + 
                    KeyEvent.getKeyText(keyCode));
                joystickManager.setUseCustomMapping(true);
            }
        }
        
        private void configureAxis(int axisIndex) {
            String[] directions = {"Negativo", "Positivo"};
            String[] axisNames = {"Eixo X", "Eixo Y", "Eixo Z", "Eixo RZ"};
            
            for (int dir = 0; dir < 2; dir++) {
                String label = axisNames[axisIndex] + " (" + directions[dir] + ") - Player " + (playerId + 1);
                int keyCode = KeyCaptureDialog.capture(
                    ConfigDialog.getCurrentInstance(), label);
                
                if (keyCode > 0) {
                    // Mapear eixo (simplificado - na prática precisaria mapear Component.Identifier)
                    JOptionPane.showMessageDialog(this,
                        "Configure " + axisNames[axisIndex] + " " + directions[dir] + " para: " + 
                        KeyEvent.getKeyText(keyCode),
                        "Mapeamento de Eixo",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        private void configurePOV(int povIndex) {
            String[] directions = {"Cima", "Baixo", "Esquerda", "Direita"};
            String label = "POV " + directions[povIndex] + " - Player " + (playerId + 1);
            
            int keyCode = KeyCaptureDialog.capture(
                ConfigDialog.getCurrentInstance(), label);
            
            if (keyCode > 0) {
                // Aqui você precisaria implementar o mapeamento específico para POV
                povBtns[povIndex].setText(directions[povIndex] + ": " + 
                    KeyEvent.getKeyText(keyCode));
                JOptionPane.showMessageDialog(this,
                    "Configure POV " + directions[povIndex] + " para: " + 
                    KeyEvent.getKeyText(keyCode),
                    "Mapeamento POV",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        public void updateUIState() {
            if (!joystickManager.isJoystickEnabled()) return;
            
            // Atualizar estados visuais baseados no polling (se necessário)
            // Esta é uma implementação simplificada - você pode expandir para
            // mostrar feedback visual em tempo real como no JoystickTest
        }
        
        public void updateButtonLabels() {
            // Atualizar labels dos botões baseados no mapeamento atual
            // Esta é uma implementação básica - você pode expandir para mostrar
            // o mapeamento atual de cada botão
        }
        
        // Métodos para feedback visual
        public void highlightButton(int buttonIdx, boolean pressed) {
            if (buttonIdx >= 0 && buttonIdx < buttonBtns.length) {
                if (pressed) {
                    buttonBtns[buttonIdx].setBackground(new Color(144, 238, 144)); // Verde claro
                    buttonBtns[buttonIdx].setOpaque(true);
                    buttonBtns[buttonIdx].setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                } else {
                    buttonBtns[buttonIdx].setBackground(UIManager.getColor("Button.background"));
                    buttonBtns[buttonIdx].setOpaque(false);
                    buttonBtns[buttonIdx].setBorder(UIManager.getBorder("Button.border"));
                }
                buttonBtns[buttonIdx].repaint();
            }
        }
        
        public void highlightAxis(int axisIdx, float value) {
            if (axisIdx >= 0 && axisIdx < axisBtns.length) {
                Color color;
                if (Math.abs(value) > 0.5f) {
                    color = value > 0 ? new Color(173, 216, 230) : new Color(255, 218, 185); // Azul claro ou laranja claro
                    axisBtns[axisIdx].setBackground(color);
                    axisBtns[axisIdx].setOpaque(true);
                } else {
                    axisBtns[axisIdx].setBackground(UIManager.getColor("Button.background"));
                    axisBtns[axisIdx].setOpaque(false);
                }
                axisBtns[axisIdx].repaint();
            }
        }
    }
    
    @Override
    public void dispose() {
        if (uiUpdateTimer != null) {
            uiUpdateTimer.stop();
        }
        JoystickManager.globalCleanup();
        super.dispose();
    }
}
