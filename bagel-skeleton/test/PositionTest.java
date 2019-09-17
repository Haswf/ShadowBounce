import bagel.util.Point;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @BeforeAll
    static void setup(){
        //System.out.println("@BeforeAll executed");
    }

    @BeforeEach
    void setupThis(){
        //System.out.println("@BeforeEach executed");
    }

    @Test
    void testGetCentre(){
        Position position = new Position(new Point(50,50), new Point(0, 0));
        Assertions.assertEquals( 50 , position.getCentre().x);
        Assertions.assertEquals( 50 , position.getCentre().y);
        //Assertions.assertEquals(new Point(50, 50) , position.getCentre());
    }


    @Test
    void testGetTopLeft(){
        Position position = new Position(new Point(50,50), new Point(0, 0));
        Assertions.assertEquals( 0 , position.getTopLeft().x);
        Assertions.assertEquals( 0 , position.getTopLeft().y);
        //Assertions.assertEquals(new Point(50, 50) , position.getCentre());
    }

    @Test
    void testSetCentre()
    {
        Position position = new Position(new Point(50,50), new Point(0, 0));
        position.setCentre(new Point(100,100));
        System.out.println("======TEST testSetCentre EXECUTED=======");
        Assertions.assertEquals( 100 , position.getCentre().x);
        Assertions.assertEquals( 100 , position.getCentre().y);
        Assertions.assertEquals( 50 , position.getTopLeft().x);
        Assertions.assertEquals( 50 , position.getTopLeft().y);
        //Assertions.assertEquals( new Point(100, 100) , position.getCentre());
        //Assertions.assertEquals(new Point(50, 50) , position.getTopLeft());
    }

    @Test
    void testSetTopLeft(){
        Position position = new Position(new Point(50,50), new Point(0, 0));
        position.setTopLeft(new Point(100,100));
        System.out.println("======TEST testSetTopLeft EXECUTED=======");
        Assertions.assertEquals( 100 , position.getTopLeft().x);
        Assertions.assertEquals( 100 , position.getTopLeft().y);
        Assertions.assertEquals( 150 , position.getCentre().x);
        Assertions.assertEquals( 150 , position.getCentre().y);
        //Assertions.assertEquals( new Point(100, 100) , position.getTopLeft());
        //Assertions.assertEquals( new Point(150, 150) , position.getCentre());
    }

    @Test
    void testToString() {
        Position position = new Position(new Point(50, 50), new Point(0, 0));
        System.out.println("======TEST testToString EXECUTED=======");
        Assertions.assertEquals("Point center at (50.000000, 50.000000)", position.toString());
        position.setCentre(new Point(100, 100));
        Assertions.assertEquals("Point center at (100.000000, 100.000000)", position.toString());
    }

    @Disabled
    @Test
    void testCopyConstructor(){
        Position position = new Position(new Point(50,50), new Point(0, 0));
        Position copy = new Position(position);

    }

    @Disabled
    @Test
    void testCalcTwo()
    {
        //System.out.println("======TEST TWO EXECUTED=======");
        //Assertions.assertEquals( 6 , Calculator.add(2, 4));
    }

    @AfterEach
    void tearThis(){
        //System.out.println("@AfterEach executed");
    }

    @AfterAll
    static void tear(){
        //System.out.println("@AfterAll executed");
    }
}