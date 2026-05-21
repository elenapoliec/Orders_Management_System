package Dao;

import Model.Client;

/**
 * Componenta DAO specializata pentru entitatea {@link Model.Client}.
 * Extinde clasa generica {@link Dao.AbstractDAO} pentru a oferi acces direct la tabelul Client din baza de date.
 * Mostenește automat toate metodele de tip CRUD (Create, Read, Update, Delete) generate dinamic
 * la runtime prin mecanisme de introspectie si Java Streams API.
 * @author Poliec Elena
 * @version 1.0
 */

public class ClientDAO extends AbstractDAO<Client> {

}
