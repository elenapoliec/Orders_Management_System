package Sstart;

import Presentation.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Clasa de pornire (Entry Point) a aplicației Warehouse Management System.
 * Mostenește clasa {@link javafx.application.Application} si configureaza containerul
 * grafic principal (fereastra de tip Stage si structura de navigare pe baza de Tab-uri).
 * * @author Poliec Elena
 * @version 1.0
 */

public class Start extends Application {


    /**
     * Initializeaza si afiseaza fereastra principala a interfetei grafice.
     * Creeaza dinamic panourile (tab-urile) dedicate operatiilor pentru clienti,
     * produse si plasarea de comenzi, atașandu-le containerului principal de navigare.
     *@param primaryStage Scena principala (fereastra) oferita de runtime-ul JavaFX.
     */

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Warehouse Management System");

        TabPane mainTabPane = new TabPane();

        // generam dinamic cele 3 ecrane cerute folosind aceeași clasa de baza
        Tab clientsTab = new Tab("Clients Section", new MainView("Client"));
        Tab productsTab = new Tab("Products Section", new MainView("Product"));
        Tab ordersTab = new Tab("New Order", new MainView("Order"));

        // blochez inchiderea tab-urilor individual
        clientsTab.setClosable(false);
        productsTab.setClosable(false);
        ordersTab.setClosable(false);

        mainTabPane.getTabs().addAll(clientsTab, productsTab, ordersTab);

        Scene scene = new Scene(mainTabPane, 850, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Metoda principala executabila a aplicației (main).
     * @param args argumentele transmise aplicatiei din linia de comanda la runtime.
     */
    public static void main(String[] args) {
        launch(args);
    }
}