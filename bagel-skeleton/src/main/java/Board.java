import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import bagel.Image;
import bagel.util.Point;

public class Board implements Renderable{
    public Map<Peg.COLOUR, LinkedList<Peg>> pegs;

    public Board(String pathToCsv){
        pegs = new LinkedHashMap<Peg.COLOUR, LinkedList<Peg>>();
        readFromCSV(pathToCsv);
    }

    public void readFromCSV(String pathToCsv){
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                Peg newPeg = createPeg(data);
                Peg.COLOUR colour = parseColor(data);

                LinkedList<Peg> pegList = this.pegs.get(colour);
                if (pegList == null){
                    pegList = new LinkedList<Peg>();
                    pegList.add(newPeg);
                    pegs.put(colour, pegList);
                }
                else{
                    pegList.add(newPeg);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Peg createPeg(String[] data) {
        Peg created;
        String imageName = data[0].replace("_", "-");
        String imagePath = "res/" + imageName + ".png";
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        Peg.COLOUR colour = parseColor(data);
        Peg.SHAPE shape = parseShape(data);

        if (colour == Peg.COLOUR.BLUE){
            created = new Peg(new Point(x, y), new Image(imagePath), shape);
        }
        //else if (colour == Peg.COLOUR.GREY){
        else{
            created = new GreyPeg(new Point(x, y), new Image(imagePath), shape);
        }
        return created;
    }

    private Peg.COLOUR parseColor(String[] data){
        for (Peg.COLOUR colour : Peg.COLOUR.values()){
            if (data[0].contains(colour.toString().toLowerCase())){
                return colour;
            }
        }
        return Peg.COLOUR.NONE;
    }

    private Peg.SHAPE parseShape(String[] data){
        String c;
        for (Peg.SHAPE shape : Peg.SHAPE.values()){
            if (data[0].contains(shape.toString().toLowerCase())){
                return shape;
            }
        }
        return Peg.SHAPE.NONE;
    }

    public List<GameObject> asList() {
        List<GameObject> list = new ArrayList<GameObject>();
        for (Peg.COLOUR ty : Peg.COLOUR.values()) {
            LinkedList<Peg> pegList = this.pegs.get(ty);
            if (pegList != null) {
                list.addAll(pegList);
            }
        }
        return list;
    }
    @Override
    public void render() {
        for (Peg.COLOUR ty : Peg.COLOUR.values()) {
            LinkedList<Peg> pegList = this.pegs.get(ty);
            if (pegList!=null){
                for (Peg p : pegList){
                    p.render();
                }
            }
        }
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
