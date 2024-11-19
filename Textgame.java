import java.util.*;

public class Textgame {
    static Map<String, String> rooms = new HashMap<>();
    static Map<String, List<String>> roomExits = new HashMap<>();
    static Map<String, String> roomItems = new HashMap<>();
    static String currentRoom = "Entrance";
    static List<String> inventory = new ArrayList<>();
    static int playerHealth = 100;
    static boolean gameRunning = true;
    static boolean skeletonDefeated = false;

    public static void main(String[] args) {
        initializeGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Enhanced Adventure Game!");
        System.out.println("Type commands like 'go north', 'check inventory', 'use potion', 'talk', 'attack', or 'run'.");
        System.out.println("Type 'help' for a list of commands.");

        while (gameRunning) {
            System.out.println("\n========================");
            System.out.println("You are in the " + currentRoom + ".");
            System.out.println(rooms.get(currentRoom));
            System.out.println("Your health: " + playerHealth);
            System.out.print("Exits: ");
            System.out.println(roomExits.get(currentRoom));
            System.out.print("> ");
            String command = scanner.nextLine().toLowerCase();

            handleCommand(command);
        }
        scanner.close();
    }

    static void initializeGame() {
        rooms.put("Entrance", "You are at the entrance of a mysterious land.");
        rooms.put("Forest", "You are in a dark forest. You see something shiny on the ground.");
        rooms.put("Dungeon", "You are in a cold, dark dungeon. A skeleton blocks your path!");
        rooms.put("Treasure Room", "This is the treasure room. You see a chest glittering with gold!");

        roomExits.put("Entrance", Arrays.asList("north"));
        roomExits.put("Forest", Arrays.asList("south", "east"));
        roomExits.put("Dungeon", Arrays.asList("west", "north"));
        roomExits.put("Treasure Room", Arrays.asList("south"));

        roomItems.put("Forest", "key");
        roomItems.put("Treasure Room", "treasure");

        inventory.add("potion");
    }

    static void handleCommand(String command) {
        if (command.startsWith("go ")) {
            String direction = command.split(" ")[1];
            move(direction);
        } else if (command.equals("check inventory")) {
            checkInventory();
        } else if (command.equals("use potion")) {
            usePotion();
        } else if (command.equals("talk")) {
            talk();
        } else if (command.equals("attack")) {
            attack();
        } else if (command.equals("run")) {
            run();
        } else if (command.equals("help")) {
            displayHelp();
        } else if (command.equals("quit")) {
            System.out.println("Thanks for playing! Goodbye!");
            gameRunning = false;
        } else {
            System.out.println("Invalid command. Type 'help' for a list of commands.");
        }
    }

    static void move(String direction) {
        if (currentRoom.equals("Entrance") && direction.equals("north")) {
            currentRoom = "Forest";
        } else if (currentRoom.equals("Forest") && direction.equals("south")) {
            currentRoom = "Entrance";
        } else if (currentRoom.equals("Forest") && direction.equals("east")) {
            currentRoom = "Dungeon";
        } else if (currentRoom.equals("Dungeon") && direction.equals("west")) {
            currentRoom = "Forest";
        } else if (currentRoom.equals("Dungeon") && direction.equals("north")) {
            if (!skeletonDefeated) {
                System.out.println("The skeleton blocks your way! Defeat it first.");
            } else if (inventory.contains("key")) {
                System.out.println("You use the key to unlock the Treasure Room!");
                currentRoom = "Treasure Room";
            } else {
                System.out.println("The door is locked. You need a key to enter.");
            }
        } else {
            System.out.println("You can't go that way.");
            return;
        }
        checkForItem();
    }

    static void checkForItem() {
        if (roomItems.containsKey(currentRoom)) {
            String item = roomItems.get(currentRoom);
            System.out.println("You found a " + item + "!");
            inventory.add(item);
            roomItems.remove(currentRoom);

            if (item.equals("treasure")) {
                System.out.println("Congratulations! You found the treasure and won the game!");
                gameRunning = false;
            }
        }
    }

    static void checkInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory: " + inventory);
        }
    }

    static void usePotion() {
        if (inventory.contains("potion")) {
            System.out.println("You use a potion to heal 30 health points.");
            playerHealth = Math.min(playerHealth + 30, 100);
            System.out.println("Your health is now " + playerHealth + ".");
            inventory.remove("potion");
        } else {
            System.out.println("You don't have any potions.");
        }
    }

    static void talk() {
        if (currentRoom.equals("Forest")) {
            System.out.println("A friendly bird chirps: 'The key you found might open a door!'");
        } else {
            System.out.println("There's no one to talk to here.");
        }
    }

    static void attack() {
        if (currentRoom.equals("Dungeon") && !skeletonDefeated) {
            System.out.println("You attack the skeleton!");
            playerHealth -= 20;
            System.out.println("The skeleton strikes back! Your health is now " + playerHealth + ".");
            if (playerHealth <= 0) {
                System.out.println("You have been defeated. Game Over!");
                gameRunning = false;
            } else {
                System.out.println("The skeleton is defeated!");
                skeletonDefeated = true;
                rooms.put("Dungeon", "You are in a cold, dark dungeon. The skeleton lies in a heap.");
            }
        } else if (skeletonDefeated) {
            System.out.println("The skeleton has already been defeated.");
        } else {
            System.out.println("There's nothing to attack here.");
        }
    }

    static void run() {
        if (currentRoom.equals("Dungeon")) {
            System.out.println("You run back to the forest!");
            currentRoom = "Forest";
        } else {
            System.out.println("There's no need to run here.");
        }
    }

    static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("go [direction] - Move in a direction (north, south, east, west).");
        System.out.println("check inventory - Check your inventory.");
        System.out.println("use potion - Use a potion to restore health.");
        System.out.println("talk - Talk to an NPC if one is present.");
        System.out.println("attack - Attack an enemy if one is present.");
        System.out.println("run - Flee from an enemy encounter.");
        System.out.println("help - Display this help message.");
        System.out.println("quit - Quit the game.");
    }
}