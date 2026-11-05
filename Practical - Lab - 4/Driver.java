// File: TemplateFiller.java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// File: Driver.java
public class Driver {

    public static void main(String[] args) {

        String template =
                "Dear {name}, order {id} ships {date}.";

        String[] names = {
                "name",
                "id"
        };

        String[] values = {
                "Riya",
                "A07"
        };

        String result = TemplateFiller.fillTemplate(
                template,
                names,
                values
        );

        System.out.println(result);
    }
}

