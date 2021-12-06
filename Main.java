
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        DB db = new DB();
        db.creation();
        db.dataFill();
        System.out.println("Welcome to the world of pain and bad programming");
        while (true) {
            System.out.println("So what do you wanna do, oh good person");
            System.out.println("1. Display all pokemons");
            System.out.println("2. Display info on a single pokemon");
            System.out.println("3. Display all pokemon types");
            System.out.println("4. Display all Pokemon of Particular type");
            System.out.println("5. Delete a pokemon");
            System.out.println("6. Add a pokemon");
            System.out.println("7. Update info on a pokemon");
            System.out.println("Type q if you want to leave this place");
            String input = scan.nextLine();
            if (isInNumericRange(input)) {
                Integer inp = Integer.parseInt(input);
                switch (inp) {
                    case 1:
                        db.displayNames();
                        break;
                    case 2:
                        // get input of the name that users wants to get and check if in all names?
                        System.out.println("So what is the name of this pokemon?");
                        String name = scan.nextLine();
                        db.displayAllInfo(name.toLowerCase());
                        break;
                    case 3:
                        db.displayAllTypes();
                        break;
                    case 4:
                        // check if the type is actually in types;
                        System.out.println("So what is your type?");
                        String type = scan.nextLine();
                        db.displayTypeNames(type.toLowerCase());
                        break;
                    case 5:
                        System.out.println("So what is the name of this pokemon?");
                        // check if pokemon is in db
                        String deleteName = scan.nextLine();
                        db.deletePokemon(deleteName.toLowerCase());
                        break;
                    case 6:
                        System.out.println(
                                "please enter the info on a pokemon you want to add, if field empty type in null");
                        System.out.println(
                                "Example: pokemonName nationalNum type1,type2 species ability1,ability2 nextEvolution1,nextEvolution2 prevEvolution");
                        String[] data = scan.nextLine().split(" ");
                        // check if data is of len 7
                        if (data.length != 7) {
                            System.out.println("sorry but your input is not correct");
                        } else if (!isNumeric(data[1])) {
                            System.out.println("NationalNum must be an Integer");
                        } else {
                            // add info and do checks on the info
                            db.addPokemon(data[0].toLowerCase(), data[1].toLowerCase(), data[2].toLowerCase(),
                                    data[3].toLowerCase(), data[4].toLowerCase(), data[5].toLowerCase(),
                                    data[6].toLowerCase());
                        }
                        break;
                    case 7:
                        System.out.println("What is the name of the pokemon you wish to update");
                        String pokemonName = scan.nextLine().toLowerCase();
                        System.out.println("What is the column you wish to update");
                        String column = scan.nextLine().toLowerCase();
                        System.out.println("What is the value you wish to update");
                        String value = scan.nextLine().toLowerCase();
                        db.updateInfo(pokemonName, column, value);
                        break;
                }

            } else if (input.equals("q")) {
                break;
            } else {
                System.out.println("Try again with the correct input pretty please");
            }
        }
    }

    // dont really need this switch is enough but realized it way too late
    public static boolean isInNumericRange(String str) {
        return str.matches("[0-7]");
    }

    public static boolean isNumeric(String str) {
        return str.matches("[0-9]{1,20}");
    }

}