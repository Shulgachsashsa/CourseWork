package frames;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static frames.RegistrationFrame.createFrameRegistration;

public class MainFrame {

    public static void createAndShowGUI() throws IOException {
        JFrame frame = new JFrame("FrenzyWW");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(700, 600);

        frame.setLayout(new GridLayout(10, 10));

        JButton registerButton = new JButton("Зарегистрироваться");
        JButton loginButton = new JButton("Авторизоваться");
        configureButton(registerButton);
        configureButton(loginButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                createFrameRegistration();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        frame.add(new JPanel(), gbcTextWelcome());
        frame.add(setStyleTextWelcome(), gbcTextWelcome());

        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.add(loginButton, gbc);

        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.add(registerButton, gbc);

        frame.setVisible(true);
    }

    protected static JLabel setStyleTextWelcome() {
        String textWelcomeWithColor = "<html>Добро пожаловать на <font color='#87CEEB'>безумную</font> фабрику одежды - <font color='#87CEEB'>FrenzyWW</font> </html>";
        JLabel textWelcome = new JLabel(textWelcomeWithColor, SwingConstants.CENTER);
        return textWelcome;
    }

    protected static GridBagConstraints gbcTextWelcome() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        return gbc;
    }

    private static void configureButton(JButton button) {
        button.setBorder(new LineBorder(Color.GRAY, 2, true));
        button.setContentAreaFilled(false); // Задание прозрачного фона
        button.setFocusPainted(false); // Убираем выделение
        button.setOpaque(true); // Делаем кнопку непрозрачной
        button.setBackground(Color.LIGHT_GRAY); // Задаем цвет фона кнопки
        button.setPreferredSize(new Dimension(150, 40)); // Устанавливаем размер
    }

}
