
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {
    Connection connection = null;
    Statement statement = null;

    public DB() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:it.db");
            this.statement = this.connection.createStatement();
            this.statement.setQueryTimeout(30); // set timeout to 30 sec.
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void creation() {

        try {
            // create a database connection
            this.statement.executeUpdate("drop table if exists pokemons");
            this.statement.executeUpdate(
                    "create table pokemons(Name string, NationalNum INTEGER, Species STRING, PrevEvolution STRING)");

            this.statement.executeUpdate("drop table if exists Types");
            this.statement.executeUpdate(
                    "create table Types(Name string, Type String, FOREIGN KEY(Name) REFERENCES pokemons(Name))");
            this.statement.executeUpdate("drop table if exists Abilities");
            this.statement.executeUpdate(
                    "create table Abilities(Name string, Ability String, FOREIGN KEY(Name) REFERENCES pokemons(Name))");
            this.statement.executeUpdate("drop table if exists nextEvolution");
            this.statement.executeUpdate(
                    "create table nextEvolution(Name string, nextEvolution String, FOREIGN KEY(Name) REFERENCES pokemons(Name))");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            throw new RuntimeException(e);
        }
    }

    // TODO: add more data
    // TODO: nulls gets added as a string
    public void dataFill() throws Exception {
        String[] pokemonsNames = { "bulbasaur", "ivysaur", "charmander", "charmeleon", "eevee", "flareon", "vaporean",
                "electrode" };
        String[] nationalNum = { "1", "2", "3", "4", "133", "136", "134", "101" };
        String[] species = { "seed", "seed", "lizard", "flame", "evolution pokemon", "flame pokemon",
                "bubble jet pokemon", "ball pokemon" };
        String[] next = { "ivysaur", "venesaur", "charmeleon", "charizard",
                "vaporeon,jolteon,flareon,espeon,umbreon,leafeon,glaceon,sylveon", "null", "null", "null" };
        String[] prev = { "null", "bulbasaur", "null", "charmander", "null", "eevee", "eevee", "voltrob" };
        String[] types = { "grass,poison", "grass,poison", "fire", "fire", "normal", "fire", "water", "electric" };
        String[] abilities = { "overgrow,chlorophyll", "overgrow,chlorophyll", "blaze,solar power",
                "blaze,solar power", "run away,adaptability", "flash fire", "water absorb", "soundproof,static" };
        for (int i = 0; i < pokemonsNames.length; i++) {
            // solving the problem of adding null value not as a string
            String p = prev[i];
            if (!prev[i].equals("null")) {
                p = String.format("'%s'", prev[i]);
            }

            this.statement.executeUpdate(String.format("insert into pokemons values('%s', %s, '%s', %s)",
                    pokemonsNames[i], nationalNum[i], species[i], p));
            for (String type : types[i].split(",")) {
                this.statement
                        .executeUpdate(String.format("insert into types values('%s', '%s')", pokemonsNames[i], type));
            }
            for (String ability : abilities[i].split(",")) {
                this.statement
                        .executeUpdate(
                                String.format("insert into abilities values('%s', '%s')", pokemonsNames[i], ability));
            }
            if (next[i].equals("null")) {
                this.statement
                        .executeUpdate(
                                String.format("insert into nextEvolution values('%s', null)", pokemonsNames[i]));
            } else {
                for (String n : next[i].split(",")) {
                    this.statement
                            .executeUpdate(
                                    String.format("insert into nextEvolution values('%s', '%s')", pokemonsNames[i], n));
                }
            }
        }
    }

    // should return an ArrayList
    public void displayNames() throws Exception {
        ResultSet rs = this.statement.executeQuery("select name from pokemons");
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }

    // Beautify data
    public void displayAllInfo(String name) throws Exception {
        ResultSet rs = this.statement.executeQuery(String.format("select * from pokemons where name='%s'", name));
        while (rs.next()) {
            for (int i = 1; i <= 4; i++)
                System.out.println(rs.getString(i));
        }
        System.out.println("Types");
        rs = this.statement.executeQuery(String.format("select type from types where name='%s'", name));
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        System.out.println("Abilities");
        rs = this.statement.executeQuery(String.format("select ability from abilities where name='%s'", name));
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        System.out.println("Evolutions");
        rs = this.statement
                .executeQuery(String.format("select nextEvolution from nextEvolution where name='%s'", name));
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }

    public ArrayList<String> displayAllTypes() throws Exception {
        ArrayList<String> types = new ArrayList<>();
        ResultSet rs = this.statement.executeQuery("select distinct type from types");
        while (rs.next()) {
            types.add(rs.getString(1));
            System.out.println(rs.getString(1));
        }
        return types;
    }

    public void displayTypeNames(String type) throws Exception {
        ResultSet rs = this.statement.executeQuery(String.format("select name from types where type='%s'", type));
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }

    public void deletePokemon(String name) throws Exception {
        this.statement.executeUpdate(String.format("delete from pokemons where name='%s'", name));
        this.statement.executeUpdate(String.format("delete from types where name='%s'", name));
        this.statement.executeUpdate(String.format("delete from abilities where name='%s'", name));
        this.statement.executeUpdate(String.format("delete from nextEvolution where name='%s'", name));
    }

    public void addPokemon(String name, String nationalNum, String types, String species, String abilities,
            String nextEvolution, String prevEvolution) throws Exception {
        // solving the problem of adding null value not as a string
        String p = prevEvolution;
        if (!p.equals("null")) {
            p = String.format("'%s'", p);
        }
        this.statement.executeUpdate(String.format("insert into pokemons values('%s', '%s', '%s', %s)",
                name, nationalNum, species, p));
        for (String type : types.split(",")) {
            this.statement
                    .executeUpdate(String.format("insert into types values('%s', '%s')", name, type));
        }
        for (String ability : abilities.split(",")) {
            this.statement
                    .executeUpdate(
                            String.format("insert into abilities values('%s', '%s')", name, ability));
        }
        if (nextEvolution.equals("null")) {
            this.statement
                    .executeUpdate(
                            String.format("insert into nextEvolution values('%s', null)", name));
        } else {
            for (String n : nextEvolution.split(",")) {
                this.statement
                        .executeUpdate(
                                String.format("insert into nextEvolution values('%s', '%s')", name, n));
            }
        }
    }

    public void updateInfo(String name, String column, String value) throws Exception {
        if (!column.equals("nationalnum")) {
            value = String.format("'%s'", value);
        }
        System.out.println(name);
        System.out.println(column);
        System.out.println(value);

        this.statement.executeUpdate(String.format("update pokemons SET %s=%s where Name='%s'", column, value, name));
    }

    protected void finalize() {
        System.out.println("we are out of here");
        try {
            if (this.connection != null)
                this.connection.close();
        } catch (SQLException e) {
            // connection close failed.
            throw new RuntimeException(e);
        }
    }

}