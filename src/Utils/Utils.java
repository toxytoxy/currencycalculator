package Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import GUI.GUI;
import Utils.Data.Calculations;
import Utils.Data.ExchangeRateFetcher;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

/*
 * In dieser Klasse sind alle Tools (Werkzeuge) verfügbar
 * Diese werden in einer anderen Klasse verwendet
 */
public class Utils {

    /*
     * Diese Methode stellt das End ergebnis auf zwei (nach) Kommastellen um
     * 
     * bsp: 12.04405
     * 
     * -> 12.04
     */
    public static double adjustDecimal(double x, int decimalPlaces) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');

        StringBuilder pattern = new StringBuilder("#.");
        for (int i = 0; i < decimalPlaces; i++) {
            pattern.append("#");
        }

        DecimalFormat dfConverted = new DecimalFormat(pattern.toString(), symbols);

        return Double.parseDouble(dfConverted.format(x));
    }

    /*
     * TODO Kommentar
     */
    public static Set<Entry<String, String>> getAllCurrencies() {
        Map<String, String> currencies = new HashMap<>();

        for (Currency currency : Currency.getAvailableCurrencies()) {
            String currencyCode = currency.getCurrencyCode();
            String displayName = currency.getDisplayName();
            currencies.put(currencyCode, displayName);
        }

        return currencies.entrySet();
    }

    /*
     * Diese Methode ist der Hauptprozess für die Rechnung
     */
    public static void runCalcThread() {
        // lambda funktion in der runCalcThread() funktion um asynchrones ausführen zu
        // ermöglichen (=> GUI kann sich dadurch updaten)
        Thread thread = new Thread(() -> {
            GUI.displayAsLoading(true);

            /*
             * TODO: GUI.getUserInput() in ConvertCurrencies ?
             */

            String resultAsString = ""
                    + Calculations.convertCurrencies(GUI.getBaseCur(), GUI.getTargetCur(), GUI.getAmount());

            GUI.setOuput("Eingetippt: " + GUI.getAmount() + " " + GUI.getBaseCur() + "\n" +
                    " Das Ergebnis ist " + resultAsString.replace('.', ',') + " " + GUI.getTargetCur() + "\n" +
                    " Die Operation dauerte: " + ExchangeRateFetcher.getLastFetchTime() + "ms");

            GUI.displayAsLoading(false);
        });

        thread.start();
    }

    /*
     * Diese Methode kopiert das Ergebnis in den Clipboard
     * 
     * GUI -> siehe GUI.addCopyOutputButton
     */
    public static void copyToClipboard() {
        double umrechnung = Calculations.endErgebnis;
        if (umrechnung != 0.0) {
            String myString = String.valueOf(umrechnung);
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
}
