import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import bagel.Image;
import bagel.util.Point;

public class Board{
    private Map<Peg.COLOUR, LinkedList<Peg>> pegs;

    public Board(String pathToCsv){
        pegs = new LinkedHashMap<Peg.COLOUR, LinkedList<Peg>>();
        readFromCSV(pathToCsv);
        convertToRedPeg();
        createGreenBall();
    }

    private void readFromCSV(String pathToCsv){
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

    public void add(Peg p){
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

    public void remove(Peg p){
        Peg.COLOUR colour = p.getColour();
        LinkedList<Peg> pegList = this.pegs.get(colour);
        pegList.remove(p);
    }

    private BluePeg getBlue(){
        List<Peg> bluePeg = pegs.get(Peg.COLOUR.BLUE);
        Random random = new Random();
        int index = random.nextInt(bluePeg.size());
        return (BluePeg) bluePeg.get(index);
    }

    private void convertToRedPeg(){
        List bluePeg = pegs.get(Peg.COLOUR.BLUE);
        int total = bluePeg.size()/15;
        for (int i=0; i<total; i++){
            BluePeg choice = getBlue();
            RedPeg converted = choice.toRed();
            add(converted);
            bluePeg.remove(choice);
        }
    }

    private void createGreenBall(){
        BluePeg choice = getBlue();
        GreenPeg converted = choice.toGreen();
        add(converted);
        remove(choice);
    }

    public void refreshGreenPeg(){
        if (getGreenCount()>0){
            GreenPeg green = (GreenPeg)pegs.get(Peg.COLOUR.GREEN).getFirst();
            BluePeg converted = green.toBlue();
            add(converted);
            remove(green);
        }
        createGreenBall();
    }

    private Peg createPeg(String[] data) {
        Peg.COLOUR colour = Peg.parseColor(data[0]);
        Peg.SHAPE shape = Peg.parseShape(data[0]);
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

//    public Iterator<LinkedList<Peg>> iterPegs(){
//        return this.pegs.values().iterator();
//    }
}
