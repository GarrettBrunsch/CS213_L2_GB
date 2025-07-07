// Garrett Brunsch
// Lab #2
// Due 7/6/25

import java.util.Scanner;
import java.util.Random;

public class Main
{
    enum MenuOptions
    {
        INVALID, BATTLE, QUIT
    }

    private static final int MAX_STRENGTH = 25;
    private static final int MAX_HEALTH = 200;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        Creature creature1 = new Creature();
        Creature creature2 = new Creature();

        int choice = 0;
        MenuOptions menuChoice = MenuOptions.INVALID;

        while (menuChoice != MenuOptions.QUIT)
        {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            choice = (choice >= MenuOptions.BATTLE.ordinal() && choice <= MenuOptions.QUIT.ordinal()) ? choice : 0;
            menuChoice = MenuOptions.values()[choice];

            switch (menuChoice)
            {
                case BATTLE:
                    handleBattle(scanner, rand, creature1, creature2);
                    break;
                case QUIT:
                    System.out.println("Now exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid menu option");
                    break;
            }
        }
        scanner.close();
    }

    public static void displayMenu()
    {
        System.out.println("\n\n=== MAIN MENU ===\n" +
                "1. Battle\n" +
                "2. Quit\n" +
                "Choice: ");
    }

    public static void handleBattle(Scanner scanner, Random rand, Creature creature1, Creature creature2) {
        System.out.println("\n--- Setting up Battle ---");

        requestCreatureInfo(scanner, rand, creature1);
        requestCreatureInfo(scanner, rand, creature2);

        System.out.println("Stats before battle:");
        displayCreatureStats(creature1, creature2);

        battle(rand, creature1, creature2);

        System.out.println("Stats after battle:");
        displayCreatureStats(creature1, creature2);

        resetCreatures(creature1, creature2);
    }


    public static void displayCreatureStats(Creature creature1, Creature creature2)
    {
        String separator = "----------------------------------------------------------------";
        String headerFormat = "%-15s | %-15s | %10s | %10s";
        String rowFormat = "%-15s | %-15s | %10d | %10d";

        String output =
                String.format(headerFormat, "Name", "Type", "Strength", "Health") + "\n" + separator + "\n" +
                String.format(rowFormat, creature1.getName(), creature1.getType(), creature1.getStrength(), creature1.getHealth()) + "\n" +
                String.format(rowFormat, creature2.getName(), creature2.getType(), creature2.getStrength(), creature2.getHealth());

        System.out.println(output);
    }

    public static void displayBattleInfo(String round, String attacker, String damage, String defender, String defenderHealth)
    {
        // Determines if data is a header or row then adjusts the alignment accordingly
        if (round.equals("Round"))
        {
            System.out.printf("%-5s | %-20s | %-6s | %-20s | %-15s%n", round, attacker, damage, defender, defenderHealth);
            System.out.println("------------------------------------------------------------------------");
        }
        else
        {
            System.out.printf("%-5s | %-20s | %6s | %-20s | %15s%n", round, attacker, damage, defender, defenderHealth);
        }
    }

    public static void requestCreatureInfo(Scanner scanner, Random rand, Creature creature)
    {
        System.out.print("Enter a new creature name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the creature's type: ");
        String type = scanner.nextLine();
        System.out.print("\n");

        int strength = rand.nextInt(MAX_STRENGTH) + 1;
        int health = rand.nextInt(MAX_HEALTH) + 1;
        creature.setCreature(name, type, strength, health);
    }

    public static void battle(Random rand, Creature creature1, Creature creature2)
    {
        System.out.println("\n--- BATTLE BEGINS ---");

        int currentTurn = rand.nextInt(2);
        int round = 1;

        displayBattleInfo("Round", "Attacker", "Damage", "Defender", "Defender Health");

        while (creature1.getHealth() > 0 && creature2.getHealth() > 0)
        {
            Creature attacker = (currentTurn == 0) ? creature1 : creature2;
            Creature defender = (currentTurn == 0) ? creature2 : creature1;

            int damage = attacker.getDamage();

            int newHealth = defender.getHealth() - damage;
            if (newHealth < 0)
            {
                newHealth = 0;
            }
            defender.setHealth(newHealth);

            displayBattleInfo(String.valueOf(round), attacker.getNameAndType(), String.valueOf(damage), defender.getNameAndType(), String.valueOf(defender.getHealth()));

            currentTurn = (currentTurn + 1) % 2;
            round++;
        }

        announceWinner(creature1, creature2, round - 1);
    }

    public static void announceWinner(Creature creature1, Creature creature2, int totalRounds)
    {
        System.out.println("\n\n=== VICTORY ===");

        Creature winner = creature1.getHealth() > 0 ? creature1 : creature2;
        Creature loser = creature1.getHealth() > 0 ? creature2 : creature1;

        System.out.printf("%s defeated %s in %d rounds!%n",
                winner.getNameAndType(), loser.getNameAndType(), totalRounds);

        System.out.print("\n");
    }

    public static void resetCreatures(Creature creature1, Creature creature2)
    {
        creature1.reset();
        creature2.reset();
        System.out.println("Creatures have been reset for another battle");
    }
}

class Creature
{
    private String name = "Unknown";
    private String type = "Unknown";
    private int strength = 0;
    private int health = 0;
    private static final Random rand = new Random();

    public Creature()
    {
        setCreature("Unknown", "Unknown", 0, 0);
    }

    public Creature(String name, String type, int strength, int health)
    {
        setCreature(name, type, strength, health);
    }

    public void setCreature(String creatureName, String creatureType, int creatureStrength, int creatureHealth)
    {
        name = creatureName;
        type = creatureType;
        strength = (creatureStrength < 1) ? 1 : creatureStrength;
        health = (creatureHealth < 0) ? 0 : creatureHealth;
    }

    public void reset()
    {
        setCreature("Unknown", "Unknown", 0, 0);
    }

    public void setHealth(int newHealth)
    {
        health = (newHealth < 0) ? 0 : newHealth;
    }

    public int getHealth()
    {
        return health;
    }

    public int getStrength()
    {
        return strength;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getNameAndType()
    {
        return name + " the " + type;
    }

    public int getDamage()
    {
        int damage = 0;
        if (strength > 0)
        {
            damage = rand.nextInt(strength) + 1;
        }
        return damage;
    }

    public String toString()
    {
        return name + " the " + type + " " + strength + " " + health;
    }
}

/*


=== MAIN MENU ===
1. Battle
2. Quit
Choice:
1

--- Setting up Battle ---
Enter a new creature name: Gandalf
Enter the creature's type: Wizard

Enter a new creature name: Frodo
Enter the creature's type: Hobbit

Stats before battle:
Name            | Type            |   Strength |     Health
----------------------------------------------------------------
Gandalf         | Wizard          |          5 |        179
Frodo           | Hobbit          |         25 |        168

--- BATTLE BEGINS ---
Round | Attacker             | Damage | Defender             | Defender Health
------------------------------------------------------------------------
1     | Frodo the Hobbit     |     13 | Gandalf the Wizard   |             166
2     | Gandalf the Wizard   |      1 | Frodo the Hobbit     |             167
3     | Frodo the Hobbit     |     18 | Gandalf the Wizard   |             148
4     | Gandalf the Wizard   |      3 | Frodo the Hobbit     |             164
5     | Frodo the Hobbit     |     18 | Gandalf the Wizard   |             130
6     | Gandalf the Wizard   |      4 | Frodo the Hobbit     |             160
7     | Frodo the Hobbit     |      7 | Gandalf the Wizard   |             123
8     | Gandalf the Wizard   |      4 | Frodo the Hobbit     |             156
9     | Frodo the Hobbit     |      8 | Gandalf the Wizard   |             115
10    | Gandalf the Wizard   |      2 | Frodo the Hobbit     |             154
11    | Frodo the Hobbit     |     12 | Gandalf the Wizard   |             103
12    | Gandalf the Wizard   |      5 | Frodo the Hobbit     |             149
13    | Frodo the Hobbit     |     15 | Gandalf the Wizard   |              88
14    | Gandalf the Wizard   |      2 | Frodo the Hobbit     |             147
15    | Frodo the Hobbit     |     21 | Gandalf the Wizard   |              67
16    | Gandalf the Wizard   |      2 | Frodo the Hobbit     |             145
17    | Frodo the Hobbit     |     23 | Gandalf the Wizard   |              44
18    | Gandalf the Wizard   |      4 | Frodo the Hobbit     |             141
19    | Frodo the Hobbit     |      1 | Gandalf the Wizard   |              43
20    | Gandalf the Wizard   |      1 | Frodo the Hobbit     |             140
21    | Frodo the Hobbit     |      1 | Gandalf the Wizard   |              42
22    | Gandalf the Wizard   |      3 | Frodo the Hobbit     |             137
23    | Frodo the Hobbit     |     19 | Gandalf the Wizard   |              23
24    | Gandalf the Wizard   |      5 | Frodo the Hobbit     |             132
25    | Frodo the Hobbit     |      2 | Gandalf the Wizard   |              21
26    | Gandalf the Wizard   |      5 | Frodo the Hobbit     |             127
27    | Frodo the Hobbit     |      2 | Gandalf the Wizard   |              19
28    | Gandalf the Wizard   |      3 | Frodo the Hobbit     |             124
29    | Frodo the Hobbit     |     23 | Gandalf the Wizard   |               0


=== VICTORY ===
Frodo the Hobbit defeated Gandalf the Wizard in 29 rounds!

Stats after battle:
Name            | Type            |   Strength |     Health
----------------------------------------------------------------
Gandalf         | Wizard          |          5 |          0
Frodo           | Hobbit          |         25 |        124
Creatures have been reset for another battle


=== MAIN MENU ===
1. Battle
2. Quit
Choice:
1

--- Setting up Battle ---
Enter a new creature name: Pikachu
Enter the creature's type: Electric

Enter a new creature name: Squirtle
Enter the creature's type: Water

Stats before battle:
Name            | Type            |   Strength |     Health
----------------------------------------------------------------
Pikachu         | Electric        |         18 |         39
Squirtle        | Water           |         10 |         78

--- BATTLE BEGINS ---
Round | Attacker             | Damage | Defender             | Defender Health
------------------------------------------------------------------------
1     | Pikachu the Electric |      4 | Squirtle the Water   |              74
2     | Squirtle the Water   |      9 | Pikachu the Electric |              30
3     | Pikachu the Electric |      4 | Squirtle the Water   |              70
4     | Squirtle the Water   |      8 | Pikachu the Electric |              22
5     | Pikachu the Electric |      2 | Squirtle the Water   |              68
6     | Squirtle the Water   |      6 | Pikachu the Electric |              16
7     | Pikachu the Electric |     12 | Squirtle the Water   |              56
8     | Squirtle the Water   |      4 | Pikachu the Electric |              12
9     | Pikachu the Electric |     16 | Squirtle the Water   |              40
10    | Squirtle the Water   |      1 | Pikachu the Electric |              11
11    | Pikachu the Electric |      9 | Squirtle the Water   |              31
12    | Squirtle the Water   |      1 | Pikachu the Electric |              10
13    | Pikachu the Electric |      8 | Squirtle the Water   |              23
14    | Squirtle the Water   |     10 | Pikachu the Electric |               0


=== VICTORY ===
Squirtle the Water defeated Pikachu the Electric in 14 rounds!

Stats after battle:
Name            | Type            |   Strength |     Health
----------------------------------------------------------------
Pikachu         | Electric        |         18 |          0
Squirtle        | Water           |         10 |         23
Creatures have been reset for another battle


=== MAIN MENU ===
1. Battle
2. Quit
Choice:
2
Now exiting program...

Process finished with exit code 0


 */
