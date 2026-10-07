import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScoreWriterAndDisplayer {
    
    private static final String FILE_NAME = "score.reg";


    /*
        Écrit le score de la partie actuelle dans le fichier des scores.

        Insère le score à la bonne position en triant :
            - d'abord par score décroissant (du plus grand au plus petit)
            - puis, en cas d'égalité, par durée de partie croissante (du plus court au plus long).
        Réécrit ensuite entièrement le fichier avec le nouveau classement

        Paramètres :
            newUserName     – nom du joueur
            newScore        – score obtenu durant la partie
            newGameDuration – durée de la partie en secondes
    */
    public static void writeScoreIntoScoreFile(String newUserName, int newScore, long newGameDuration) {

        ArrayList<String> lines = new ArrayList<>();
        String newLine = newUserName + "," + newScore + "," + newGameDuration;

        try {
            //creer le fichier
            File myFile = new File(FILE_NAME);
             if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            }

            //lire le fichier
            if (myFile.exists()) {
                try (Scanner myReader = new Scanner(myFile)) {
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        lines.add(data);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }

            // Trouver la bonne position
            int insertIndex = lines.size(); // par défaut à la fin

            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");

                //parseint() pour cast le string en int pour apres comparer
                int fileScore = Integer.parseInt(parts[1]);
                int fileGameDuration = Integer.parseInt(parts[2]);

                //si le score a ajouter est plus grand OU si le score est == ET que le chrono est plus court, on break et on met le num de ligne dans 'insertIndex'
                if (newScore > fileScore || 
                   (newScore == fileScore && newGameDuration < fileGameDuration)) {
                    insertIndex = i;
                    break;
                }
            }

            //insere la nouvelle ligne a la bonne pos/index
            lines.add(insertIndex, newLine);

            // Réécrire le fichier
            try (FileWriter myWriter = new FileWriter(FILE_NAME)) {
                for (String line : lines) {
                    myWriter.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /*
        Affiche les scores contenus dans le fichier des scores dans le terminal.
        Indique le classement et met en évidence le score de la partie actuelle.
        
        Paramètres :
            currentName     – nom du joueur de la partie actuelle
            currentScore    – score obtenu durant la partie actuelle
            currentDuration – durée de la partie actuelle
    */
    public static void displayScore(String currentName, int currentScore, long currentDuration) {

        File myFile = new File(FILE_NAME);
        int ranking=1;
        System.out.println("--- NAME | SCORE | TIME ---");
        try (Scanner myReader = new Scanner(myFile)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(",");

                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                int duration = Integer.parseInt(parts[2]);

                System.out.print("#"+ranking+". ");

                if (name.equals(currentName)
                    && score == currentScore
                    && duration == currentDuration){

                    System.out.println(data + " <<<=---:YOU");
                } else {
                    System.out.println(data);
                }
                ranking++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}