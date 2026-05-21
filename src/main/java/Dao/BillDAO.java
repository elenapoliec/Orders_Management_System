package Dao;

import Model.Bill;

/**
 * Componenta (DAO) specializata pentru entitatea {@link Model.Bill}.
 * Extinde clasa generica {@link Dao.AbstractDAO} configurand tipul de date ca fiind un Java Record (Bill).
 * Corpul clasei este gol deoarece mostenește nativ toate operatiile de citire
 * si inserare implementate prin reflexie si stream-uri.
 * Modificarile si stergerile vor arunca exceptii la runtime.
 * @author Poliec Elena
 * @version 1.0
 */

public class BillDAO extends AbstractDAO<Bill> {

}