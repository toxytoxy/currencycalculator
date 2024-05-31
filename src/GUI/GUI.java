package GUI;

import javax.swing.*;

import java.awt.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import GUI.Components.InputOutput;
import GUI.Components.Miscellaneous;
import GUI.Components.SettingsGUI;
import GUI.Popups.PopupDisplay;
import Main.CurrencyCalculator;
import Utils.Utils;
import Utils.Data.Config.Settings.AppTheme;
import Utils.Data.Config.Settings.AppTheme.Theme;
import lang.Language;

/*
 * Diese Klasse erstellt das "Graphical User Interface"
 * Dadurch muss der Nutzer nicht auf dem Terminal arbeiten
 */
public class GUI {

    // Grundinformationen für das JFrame, wie die Größe und das Icon
    private static final int FRAME_WIDTH = 900, FRAME_HEIGHT = 600;
    private static final ImageIcon icon = new ImageIcon(GUI.class.getResource("/resources/app_icon/app_icon.png"));

    // Komponenten
    public static JFrame frame = new JFrame();
    private static JLabel headlineLabel = new JLabel(Language.getLangStringByKey("title"));

    // Diese Variable wird für das Headline benutzt.
    private static int textWidth;

    public static JFrame getAppWindow() {
        return frame;
    }

    public static int getWindowWidth() {
        return FRAME_WIDTH;
    }

    public static int getWindowHeight() {
        return FRAME_HEIGHT;
    }

    /*
     * Diese Methode fügt andere GUI Komponenten hinzu
     */
    public static void drawGUI() {
        setBasicFrameProps();

        SettingsGUI.addAllComponents();
        InputOutput.addAllComponents();
        Miscellaneous.addAllComponents();

        setAppTheme(AppTheme.getConfigAppTheme());
        updateDisplayedLanguage(true);

        getAppWindow().requestFocus();
        getAppWindow().setVisible(true);
    }

    /*
     * Legt den Fenstertitel fest
     */
    public static void updateTitle(JFrame jframe) {
        jframe.setTitle(Language.getLangStringByKey("title") + " " + CurrencyCalculator.getAppVersion());
    }

    public static void updateTitle(JFrame jframe, String rawTitleAddition) {
        String titleAddition = " - " + rawTitleAddition;
        jframe.setTitle(Language.getLangStringByKey("title") + " " + CurrencyCalculator.getAppVersion()
                + (rawTitleAddition != "" ? titleAddition : ""));
    }

    public static void setAppIcon(JFrame jframe) {
        jframe.setIconImage(icon.getImage());
    }

    /*
     * Erstellt das Hauptfenster für die GUI
     */
    private static void setBasicFrameProps() {
        updateTitle(frame, "");
        setAppIcon(frame);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        headlineLabel.setFont(headlineLabel.getFont().deriveFont(30f));

        // Durch die Länge der Headline wird das Label mittig gesetzt
        headlineLabel.setBounds((FRAME_WIDTH - getTextWidth()) / 2, 25, getTextWidth(), 50);

        frame.add(headlineLabel);
    }

    // Diese Methode setzt den Theme fest
    public static void setAppTheme(Theme theme) {
        try {
            if (theme == Theme.DARK_MODE) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                AppTheme.setConfigAppTheme(Theme.DARK_MODE);
            } else if (theme == Theme.LIGHT_MODE) {
                UIManager.setLookAndFeel(new FlatLightLaf());
                AppTheme.setConfigAppTheme(Theme.LIGHT_MODE);
            }
            SwingUtilities.updateComponentTreeUI(frame);

        } catch (Exception e) {
            PopupDisplay.throwErrorPopup(Language.getLangStringByKey("error_theme_get") + "\n" +
                    Language.getLangStringByKey("error_theme_look"), e.getMessage());
        }
    }

    /*
     * Aktualisiert die dargestellte Sprache auf die GUI Komponenten
     */
    public static void updateDisplayedLanguage(boolean calledOnStartup) {
        // Aktualisiert den Programm Titel
        updateTitle(frame);

        // Aktualisiert alle Hauptkomponenten
        headlineLabel.setText(Language.getLangStringByKey("title"));
        InputOutput.getSearchBarBaseCur().setText(Language.getLangStringByKey("searchBar"));
        InputOutput.getSearchBarTargetCur().setText(Language.getLangStringByKey("searchBar"));
        InputOutput.getCalculateButton().setText(Language.getLangStringByKey("calculateBtn"));
        SettingsGUI.getConfigResetBtn().setText(Language.getLangStringByKey("reset"));
        Miscellaneous.getPresetLbl().setText(Language.getLangStringByKey("presetLabel"));
        Miscellaneous.getSaveBtn().setText(Language.getLangStringByKey("saveBtn"));
        Miscellaneous.getLoadBtn().setText(Language.getLangStringByKey("loadBtn"));
        Miscellaneous.getFadeLbl().setText(Language.getLangStringByKey("fadeLabel"));

        /*
         * Aktualisiert die Dropdowns mit den Währungen falls die Funktion
         * nicht bei Programmstart aufgerufen wurde
         */
        if (!calledOnStartup) {
            Utils.refreshCurrencyDropdowns();
        }
    }

    /*
     * Diese Methode nimmt die Länge der Headline
     * und setzt es in eine Variable. Dies wird verwendet, um den Label
     * mittig zu halten.
     */
    private static int getTextWidth() {
        FontMetrics fontMetrics = headlineLabel.getFontMetrics(headlineLabel.getFont());
        textWidth = fontMetrics.stringWidth(headlineLabel.getText());
        return textWidth;
    }
}
