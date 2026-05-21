package Dao;

import Model.Orders;

/**
 * Componenta DAO specializata pentru entitatea {@link Model.Orders}.
 * Extinde clasa generica {@link Dao.AbstractDAO} pentru a asigura maparea corecta a obiectelor de tip Orders
 * pe randurile tabelului de comenzi din baza de date PostgreSQL.
 * Gestionarea instructiunilor SQL si popularea obiectelor se realizeaza automat prin mostenirea logicii din clasa parinte.
 * @author Poliec Elena
 * @version 1.0
 */

public class OrdersDAO extends AbstractDAO<Orders> {
}
