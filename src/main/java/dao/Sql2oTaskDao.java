package dao;

import models.Task;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import org.sql2o.Connection;
import java.util.List;

/**
 * Created by Guest on 1/16/18.
 */
public class Sql2oTaskDao implements TaskDao {

    private final Sql2o sql2o;

    public Sql2oTaskDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Task task) {
        String sql = "INSERT INTO tasks (description) VALUES (:description)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(task)
                    .executeUpdate()
                    .getKey();
            task.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Task> getAll() {
        try(Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM tasks") //raw sql
                    .executeAndFetch(Task.class); //fetch a list
        }
    }

    @Override
    public Task findById(int id) {
        try(Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM tasks WHERE id = :id")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Task.class); //fetch an individual item
        }
    }

    @Override
    public void update(int id, String newDescription) {
        String sql = "UPDATE tasks SET description = :description WHERE id=:id";
        try(Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("description", newDescription)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from tasks WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllTasks() {
        String sql = "DELETE from tasks";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

}