import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import bagel.Image;
import bagel.util.Point;

public class Board{
    private Map<Peg.Colour, LinkedList<Peg>> pegs;

    public Board(String pathToCsv){
        pegs = new LinkedHashMap<>();
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
        Peg.Colour colour = p.getColour();
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
        return pegs.get(Peg.Colour.GREEN).size();
    }

    public int getRedCount(){
        return pegs.get(Peg.Colour.RED).size();
    }

    public void remove(Peg p){
        LinkedList<Peg> pegList = this.pegs.get(p.getColour());
        pegList.remove(p);
    }

    private BluePeg getBlue(){
        List<Peg> bluePeg = pegs.get(Peg.Colour.BLUE);
        Random random = new Random();
        int index = random.nextInt(bluePeg.size());
        return (BluePeg) bluePeg.get(index);
    }

    private void convertToRedPeg(){
        List bluePeg = pegs.get(Peg.Colour.BLUE);
        int total = bluePeg.size()/5;
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
            GreenPeg green = (GreenPeg)pegs.get(Peg.Colour.GREEN).getFirst();
            BluePeg converted = green.toBlue();
            add(converted);
            remove(green);
        }
        createGreenBall();
    }

    private Peg createPeg(String[] data) {
        Peg.Colour colour = Peg.parseColor(data[0]);
        Peg.Shape shape = Peg.parseShape(data[0]);
        String imagePath = Peg.imagePath(colour, shape);
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);


        if (colour == Peg.Colour.BLUE) {
            return new BluePeg(new Point(x, y), new Image(imagePath), shape);
        } else {
            return new GreyPeg(new Point(x, y), new Image(imagePath), shape);
        }
    }

    public ArrayList<Peg> asList() {
        ArrayList<Peg> list = new ArrayList<>();
        for (Peg.Colour ty : Peg.Colour.values()) {
            LinkedList<Peg> pegList = this.pegs.get(ty);
            if (pegList != null) {
                list.addAll(pegList);
            }
        }
        return list;
    }
}