import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import bagel.Image;
import bagel.util.Point;

public class Board{
    /*
    An Map storing pegs of different colour where the key is Peg.Colour
    value is an ArrayList of peg of that specific colour.
     */
    private Map<Peg.Colour, ArrayList<Peg>> pegs;
    /* How many percentage of blue pegs will become red peg */
    private final static float RED_RATIO = 0.2f;

    /**
     * Board Constructor
     * @param pathToCsv the file path of the csv file to be read
     */
    public Board(String pathToCsv){
        pegs = new LinkedHashMap<>();
        readFromCSV(pathToCsv);
        convertToRedPeg();
        createGreenBall();
    }

    /**
     * Read a CSV file and add corresponding pegs to the board
     * @param pathToCsv the file path of the csv file to be read
     */
    private void readFromCSV(String pathToCsv){
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
            String row;
            while ((row = csvReader.readLine()) != null) {
                // Split a line by comma
                String[] data = row.split(",");
                Peg.Colour colour = Peg.parseColor(data[0]);
                Peg.Shape shape = Peg.parseShape(data[0]);
                Point pos = new Point(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                add(createPeg(colour, shape, pos));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a peg to the board.
     * @param p peg you want to add.
     */
    public void add(Peg p){
        Peg.Colour colour = p.getColour();
        ArrayList<Peg> pegList = this.pegs.get(colour);

        // Initialise the colour-pegs pair if it hasn't been created
       if (pegList == null){
            pegList = new ArrayList<>();
            pegList.add(p);
            pegs.put(colour, pegList);
        }
       // Add the peg to the HashMap
        else{
            pegList.add(p);
        }
    }

    /**
     * Get the number of green pegs on the current board
     * @return the number of green pegs on the board.
     */
    private int getGreenCount(){
        return pegs.get(Peg.Colour.GREEN).size();
    }

    /**
     * Get the number of red pegs on the current board
     * @return the number of red pegs on the board.
     */
    public int getRedCount(){
        return pegs.get(Peg.Colour.RED).size();
    }

    /**
     * Remove a given peg from the board, return false if the given peg doesn't exist in the board.
     * @param p the peg you want to remove
     */
    public void remove(Peg p){
        ArrayList<Peg> pegList = this.pegs.get(p.getColour());
        pegList.remove(p);
    }

    /**
     * Randomly choose a blue peg on the board
     * @return the blue peg picked
     */
    private BluePeg getBlue() throws NoBluePegException {
        // Retrieve the ArrayList of blue peg from pegs
        ArrayList<Peg> bluePeg = pegs.get(Peg.Colour.BLUE);
        if (bluePeg.size() == 0){
            throw new NoBluePegException(String.format("%d blue peg remains\n", bluePeg.size()));
        }
        else {
            Random random = new Random();
            // Randomly pick a blue peg by using a random index
            int index = random.nextInt(bluePeg.size());
            return (BluePeg) bluePeg.get(index);
        }
    }

    /**
     * Convert a given percentage specified by RED_RATIO of blue pegs to red pegs
     */
    private void convertToRedPeg(){
        List bluePeg = pegs.get(Peg.Colour.BLUE);
        // Determine the number of red pegs to create
        float total = bluePeg.size() * RED_RATIO;
        for (int i=0; i<total; i++){
            try {
                BluePeg choice = getBlue();
                // Convert the selected peg to red
                add(choice.toRed());
                // Remove the lucky blue peg from the board
                bluePeg.remove(choice);
            }
            catch (NoBluePegException ignore){
                ShadowBounce.LOGGER.log(Level.WARNING,
                        String.format("Failed to convert %d/%d BluePeg to RedPeg: NoBluePegException\n", i, (int)total));
                break;
            }

        }
    }

    /**
     * Select a blue peg from the board and convert it to a green peg
     */
    private void createGreenBall(){
        try {
            BluePeg choice = getBlue();
            add(choice.toGreen());
            remove(choice);
        }
        catch (NoBluePegException ignored){
            ShadowBounce.LOGGER.log(Level.WARNING, "Failed to convert a blue peg to green peg: NoBluePegException");
        }


    }

    /**
     * Change the position of green peg on the board. An
     */
    public void refreshGreenPeg(){
        /* Convert the green peg back to blue if it
           hasn't been destroyed in the previous round */
        if (getGreenCount()>0){
            GreenPeg green = (GreenPeg)pegs.get(Peg.Colour.GREEN).get(0);
            add(green.toBlue());
            remove(green);
        }
        /* Pick a blue peg and convert it to green */
        createGreenBall();
    }

    private Peg createPeg(Peg.Colour colour, Peg.Shape shape, Point pos) {

        String imagePath = Peg.imagePath(colour, shape);
        if (colour == Peg.Colour.BLUE) {
            return new BluePeg(pos, new Image(imagePath), shape);
        } else {
            return new GreyPeg(pos, new Image(imagePath), shape);
        }
    }

    /**
     * Represent all pegs on the board as a ArrayList
     * @return Return an ArrayList of Pegs representing all pegs on the board.
     */
    public ArrayList<Peg> asList() {
        ArrayList<Peg> list = new ArrayList<>();
        for (Peg.Colour colour : Peg.Colour.values()) {
            ArrayList<Peg> pegList = this.pegs.get(colour);
            if (pegList != null) {
                list.addAll(pegList);
            }
        }
        return list;
    }
}