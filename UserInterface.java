//class dealing with user interaction
//by Kei Wang
import java.util.*;
/**
 *  FILE: UserInterface.java <br>
 *  PURPOSE: class for user interaction <br>
 *
 *  @author Kei Sum Wang - 19126089
 */
public class UserInterface
{
    private String menu;
    private FileIO f;
    private boolean itinConstructed;

    /**
     * DEFAULT Constructor for UserInterface
     */
    public UserInterface()
    {
        this.menu = "(1)    -List Nominees\n" +
                    "(2)    -Nominee Search\n" +
                    "(3)    -List by Margin\n" +
                    "(4)    -Itinerary by Margin\n" +
                    "(0)    -Quit\n" +
                    "Choice:>";
        this.f =  new FileIO();
        itinConstructed = false;
    }
  
    //present functions and allows user to use functionality
    /**
     * method to begin menu selection
     */ 
    public void run()
    {
        int choose, op, partyOrState, listFilt;
        String fileName, partyAbrev, sname, itPlace;
        
        //search and read require files in current directory
        readFiles();
         
        do
        {
            //System.out.print(menu);
            choose = intPut(menu);
            switch(choose)
            {
                //exit
                case 0:
                    System.out.println("Bye");
                break;
                case 1:
                    //listFilt = intPut("==list based on==\n(1) -State\n(2) -Party\n(3) -Division\nchoice:>");
                    listNominees();//listFilt);         
                break;
                case 2:
                    sname = stringInput("Enter first few letters or complete surname of nominee:> ");
                    //partyOrState = intPut("=Filter by=\n(1)state\n(2)party\nchoice:>");
                    searchNominees(sname);//, partyOrState);          
                break;
                case 3:
                    partyAbrev = stringInput("Enter party abbreviation:> ");
                    listMarginByParty(partyAbrev);
                break;
                case 4:
                    if(this.itinConstructed)
                    {
                        displayItinerary();
                        this.itinConstructed = false;
                    }
                    else
                    {
                        System.out.println("Please list margin before displaying itinerary");
                    }
                break;
                default:
                    System.out.println("invalid choice");
                break;
            }

        }while(choose != 0);

    }

    //integer input
    /**
     * method for integer input from user.
     * @param prompt string by implementer
     * @return integer from input
     */
    private int intPut(String prompt)
    {
        int input;
        Scanner sc = new Scanner(System.in);

        System.out.print(prompt);
        input = sc.nextInt();

        return input;
    }

    //double input
    /**
     * method for real input from user.
     * @param prompt string by implementer
     * @return real from input
     */
    private double realInput(String prompt)
    {
        double input;
        Scanner sc = new Scanner(System.in);

        System.out.print(prompt);
        input = sc.nextDouble();

        return input;
    }

    //string input
    /**
     * method for String input from user.
     * @param prompt string by implementer
     * @return String from input
     */
    private String stringInput(String prompt)
    {
        String input;
        Scanner sc = new Scanner(System.in);

        System.out.print(prompt);
        input = sc.nextLine();

        return input;
    }

    /**
     * method to handle listing of nominees
     */
    public void listNominees()//int option)
    {
        String state, party, div;
        boolean zeroResults;

        state = stringInput("Enter a states' abbreviation or Enter ALL to filter by all states:> ");
        party = stringInput("Enter the abbreviation for the party or Enter ALL to filter by all parties:> ");
        div = stringInput("Enter name of a division or Enter ALL to filter by all divisions:> ");

        orderByNomFields();//method that call file to sort nominee list

        zeroResults = f.listNoms(state, party, div);
        if(!zeroResults)
        {
            writeFile("listNominees.csv"); 
        }
        else
        {
            System.out.println("No results found...");
        }
    }

    /**
     * methods to handle search of nominees
     * @param sname surname string, can contain few or full characters of string
     */
    public void searchNominees(String sname)//, int option)
    {
        String state, party;
        boolean zeroResults = true;

        state = stringInput("Enter a states' abbreviation or Enter 'ALL' to search all states:> ");
        party = stringInput("Enter the abbreviation for the party or Enter 'ALL' to search all parties:> ");
        zeroResults = f.searchNomBySname(sname, state, party);
        if(!zeroResults)
        {
            writeFile("searchNominees.csv");                     
        }
        else
        {
            System.out.println("No results found...");
        }
    }

    /**
     * method to handle listing of margins from party
     * @param party abbreviation 
     */
    public void listMarginByParty(String party)
    { 
        double threshold, defaultThreshold;
        int custom;
        boolean noDisplay = true;
        
        custom = intPut("Enter 0 to use default threshold\nEnter 1 to use custom threshold\nchoice:>");

        defaultThreshold = 6;
        if(custom == 1)
        {
            threshold = realInput("Enter threshold:");
            noDisplay = f.listMargin(party, threshold);
            this.itinConstructed = true;
            if(noDisplay)
            {
                System.out.println("No margins to display for party: " + party + " with in custom threshold range..");
                this.itinConstructed = false;
            }
        }
        else if(custom == 0)
        {
            noDisplay = f.listMargin(party, defaultThreshold);
            this.itinConstructed = true;
            if(noDisplay)
            {
                System.out.println("No margins to display for the " + party + " party with in default threshold range...");
                this.itinConstructed = false;
            }
        }
        else
        {
            System.out.println("Invalid choice");
        }

        if(!noDisplay)
        {
            writeFile("partyMargins.csv");                     
        }
    }

    /**
     * method to handle sorting of nominees
     */
    private void orderByNomFields()
    {
        int op;

        op = intPut("==Order By==\n(1) -Surname" +
                                "\n(2) -State" +
                                "\n(3) -Party" +
                                "\n(4) -Division" +
                                "\n(5) -All" + 
                                "\nchoice:> "); 

        switch(op)
        {
            case 1:
                f.sortList("sname");
            break;
            case 2:
                f.sortList("statename");
            break;
            case 3:
                f.sortList("partysname");
            break;
            case 4:
                f.sortList("divname");
            break;
            case 5:
                f.sortList("ALL");
            break;
            default:
                System.out.println("invalid order by");
            break;
        }

    }

    /**
     * method to display intinerary
     */
    private void displayItinerary()
    {
        String itPlace;
        try
        {
            itPlace = stringInput("Enter a division name from itinerary list:> ");
            f.displayItinerary(itPlace);
            writeFile("DefaultCampaignResult.csv");
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * method to handle the reading of files
     */
    private void readFiles()
    {
        boolean hasFiles;

        hasFiles = f.readDirFiles();

        if(hasFiles)
        {
            System.out.println("required files found and sucessfully read");
        }
        else
        {
            System.out.println("required files not found");
        }
    }

    /**
     * method to handle the writing of files
     */
    private void writeFile(String defaultfile)
    {
        String choice, filename;

        choice = stringInput("Save to file? y or n: ");
        if(choice.equals("y"))
        {
            filename = stringInput("Enter file name or hit enter to save to default file(" + defaultfile + ")\nfilename:> " );
            if(filename.trim().isEmpty() || filename == null)
            {
                filename = defaultfile;
            }

            f.writeToFile(filename);
        }
    }

}
