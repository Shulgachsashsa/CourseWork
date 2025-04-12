package frames;

import commands.CommandType;
import connect.Response;
import dto.RegistrationDTO;
import connect.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static frames.MainFrame.createAndShowGUI;
import static mainclient.ClientMain.getSocket;

public class RegistrationFrame {

    protected static void createFrameRegistration() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(700, 600);

        frame.setLayout(new GridLayout(4, 2, 10, 10)); // Сетка 4 строки, 2 столбца

        JLabel nicknameLabel = new JLabel("Введите никнейм:");
        JTextField nicknameField = new JTextField();
        JLabel passwordLabel = new JLabel("Введите пароль:");
        JPasswordField passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Подтвердите пароль:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton submitButton = new JButton("Создать");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nicknameField.getText();
                String password = new String(passwordField.getPassword());
                RegistrationDTO registrationDTO = new RegistrationDTO(username, password);
                Request request = new Request(CommandType.REGISTRATION, registrationDTO);
                try (ObjectOutputStream out = new ObjectOutputStream(getSocket().getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(getSocket().getInputStream())) {

                    out.writeObject(request);
                    Response response = (Response) in.readObject();

                    if (response.getState() == 1) {
                        JOptionPane.showMessageDialog(frame, "Вы успешно зарегистрировали учетную запись!");
                        frame.dispose();
                        createAndShowGUI();
                    } else if (response.getState() == 0) {
                        JOptionPane.showMessageDialog(frame, "Введенный пользователь уже существует!");
                        return;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Произошла неизвестная ошибка!");
                        return;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Возникла ошибка отправки данных!" + ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        frame.add(nicknameLabel);
        frame.add(nicknameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(confirmPasswordLabel);
        frame.add(confirmPasswordField);
        frame.add(submitButton);

        frame.setVisible(true);
    }
}
