import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import bagel.Image;
import bagel.util.Point;

public class Board{
    public Map<Peg.COLOUR, LinkedList<Peg>> pegs;

    public Board(String pathToCsv){
        pegs = new LinkedHashMap<Peg.COLOUR, LinkedList<Peg>>();
        readFromCSV(pathToCsv);
        convertToRedPeg();
        createGreenBall();
    }

    public void readFromCSV(String pathToCsv){
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                add(createPeg(data));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void add(Peg p){
        Peg.COLOUR colour = p.getColour();
        LinkedList<Peg> pegList = this.pegs.get(colour);

        if (pegList == null){
            pegList = new LinkedList<Peg>();
            pegList.add(p);
            pegs.put(colour, pegList);
        }
        else{
            pegList.add(p);
        }
    }

    public int getGreenCount(){
        return pegs.get(Peg.COLOUR.GREEN).size();
    }

    public int getRedCount(){
        return pegs.get(Peg.COLOUR.RED).size();
    }

    private void remove(Peg p){
        Peg.COLOUR colour = p.getColour();
        LinkedList<Peg> pegList = this.pegs.get(colour);
        pegList.remove(p);
    }

    private Peg getBlue(LinkedList<Peg> bluePeg){
        Random random = new Random();
        int index = random.nextInt(bluePeg.size());
        return bluePeg.get(index);
    }

    private void convertToRedPeg(){
        LinkedList<Peg> bluePeg = pegs.get(Peg.COLOUR.BLUE);
        int total = bluePeg.size()/15;
        for (int i=0; i<total; i++){
            Peg choice = getBlue(bluePeg);
            RedPeg converted = RedPeg.convertToRed(choice);
            add(converted);
            bluePeg.remove(choice);
        }
    }

    private void createGreenBall(){
        Peg choice = getBlue(pegs.get(Peg.COLOUR.BLUE));
        GreenPeg converted = GreenPeg.convertToGreen(choice);
        add(converted);
        remove(choice);
    }

    public void refreshGreenPeg(){
        if (pegs.get(Peg.COLOUR.GREEN).size()>0){
            Peg green = pegs.get(Peg.COLOUR.GREEN).getFirst();
            BluePeg converted = BluePeg.convertToBlue(green);
            add(converted);
            remove(green);
        }
        createGreenBall();
    }


    private Peg createPeg(String[] data) {
        Peg.COLOUR colour = Peg.parseColor(data);
        Peg.SHAPE shape = Peg.parseShape(data);
        String imagePath = Peg.imagePath(colour, shape);
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);


        if (colour == Peg.COLOUR.BLUE) {
            return new BluePeg(new Point(x, y), new Image(imagePath), shape);
        } else {
            return new GreyPeg(new Point(x, y), new Image(imagePath), shape);
        }
    }

    public List<GameObject> asList() {
        List<GameObject> list = new ArrayList<>();
        for (Peg.COLOUR ty : Peg.COLOUR.values()) {
            LinkedList<Peg> pegList = this.pegs.get(ty);
            if (pegList != null) {
                list.addAll(pegList);
            }
        }
        return list;
    }

    public Iterator<LinkedList<Peg>> iterPegs(){
        return this.pegs.values().iterator();
    }

    public void destroy(GameObject go) {
        if (go instanceof Peg){
            Peg p = (Peg) go;
            Peg.COLOUR colour = p.getColour();
            LinkedList<Peg> pegList = this.pegs.get(colour);
            pegList.remove(p);
        }
    }
}
