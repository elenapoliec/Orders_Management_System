package Dao;

import Model.Product;

/**
 * Component Data Access Object (DAO) specializata pentru entitatea {@link Model.Product}.
 * Extinde clasa generica {@link Dao.AbstractDAO} pentru a asigura maparea corecta a obiectelor de tip Product
 * pe randurile tabelului corespunzator din PostgreSQL.
 * Gestionarea instructiunilor SQL si popularea obiectelor se realizeaza automat prin mostenirea logicii din clasa parinte.
 * @author Poliec Elena
 * @version 1.0
 */

public class ProductDAO extends AbstractDAO<Product> {

}
