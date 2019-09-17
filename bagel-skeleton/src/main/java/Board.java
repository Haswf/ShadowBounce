import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import bagel.Image;
import bagel.util.Point;

public class Board {
    private int numberOfBluePegs = 0;
    private int numberOfGrayPegs = 0;
    public ArrayList<Peg> pegs;


    public Board(String pathToCsv){
        pegs = new ArrayList<Peg>();;
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                this.pegs.add(createPeg(data));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Peg createPeg(String[] data) {
        String imageName = data[0].replace("_", "-");
        String imagePath = "res/" + imageName + ".png";
        double x, y;
        Peg newPeg = new Peg();
        try {
            x = Double.parseDouble(data[1]);
            y = Double.parseDouble(data[2]);
            newPeg = new Peg(new Point(x, y), new Image(imagePath), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newPeg;
    }
}
