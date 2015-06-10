package Interface;

import Transfert.Envoi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by SuRvYv0r on 10/06/2015.
 */
public class Interface extends JFrame  {
    private JTextField textAdresse;
    private JButton localHostButton;
    private JTextArea textArea1;
    private JButton fichierButton;
    private JButton envoyerButton;
    private JTextArea textDossier;
    private JTextArea textFichier;
    private JButton dossierButton;
    private JButton recevoirButton;
    private JPanel rootPanel;

    JFileChooser fce;
    JFileChooser fcr;

    public Interface() {
        super();
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create a file chooser
        fce = new JFileChooser();
        fcr = new JFileChooser();

        fcr.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        fichierButton.addActionListener(e -> {
            int returnVal = fce.showOpenDialog(Interface.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fce.getSelectedFile();
                //This is where a real application would open the file.
                textArea1.setText(file.getAbsolutePath());
            }
            textArea1.setCaretPosition(textArea1.getDocument().getLength());
        });

        dossierButton.addActionListener(e -> {
            int returnVal = fcr.showOpenDialog(Interface.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcr.getSelectedFile();
                //This is where a real application would open the file.
                textDossier.setText(file.getAbsolutePath());
            }
            textDossier.setCaretPosition(textDossier.getDocument().getLength());
        });

        localHostButton.addActionListener(e -> {
            try {
                textAdresse.setText(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException ex) {

            }
        });

        envoyerButton.addActionListener(e -> {
            Envoi envoi = new Envoi();

            File f = new File(textArea1.getText());
            int a;
            if (f.exists()) {
                a = envoi.sendFile(textArea1.getText(), textAdresse.getText());
            } else {
                System.out.println("Fichier absent");
            }
        });

        recevoirButton.addActionListener(e -> {

        });
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    // If Nimbus is not available, you can set the GUI to another look and feel.
                }
                JFrame frame = new Interface();
                frame.setVisible(true);
            }
        });
    }
}
