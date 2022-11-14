package com.mysql.example;
import java.sql.*;
import java.util.*;
/* 
 * Project Author: Christopher Wilhite
 * 
 * Project 3 Build 1.0 Full Release
 *
 * Project assumes user knows their login and enables them to insert it manually.
 * Project assumes user environment is stable and set up for use within project conditions.
 * Project assumes users know the ID of the variables they are searching for i.e. card numbers, titles, isbn, etc.
 * Project should be run from the src folder and will not compile or run if done other wise.
 * 
 * Project requires use of MySQL Java connector which can be located and downloaded here:
 * https://dev.mysql.com/downloads/connector/j/
 * 
 * To compile enter the following commands (Copy paste if needed):
 * 1. javac -d bin .\com\mysql\example\Main.java
 * 2. jar cvfm main.jar manifest.mf -C bin .
 * 3. java -jar main.jar
 * 
 * If you are struggling to compile or are getting driver related errors please refer to this github repository:
 * https://github.com/rawii22/Project3-SampleFileStructure/tree/main/src
 * 
 * Project has no implemented continuation function in order to help ensure security. Login required after query results are provided.
 */




public class Main 
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in); // sets up the input varable to take in user input
        boolean login = false;
        boolean cont = false;
        boolean end = false;
        String username = "";
        String password = "";
        Connection conn = null;
        int choice;
        //prompts the user for a username and password
        while(login == false)
        {
            System.out.print("Enter username: ");
            username = input.nextLine();
            System.out.print("Enter password: ");
            password = input.nextLine();
            try
            {
                //Connecting the user to the given database using their provided user and pass
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", username, password);
                login = true;
            }   
            catch (Exception e)
            {
                // if invalid credentials are provided, reprompt the user
                System.out.println("Invalid credentials");
                continue;
            }
        }
        //Printing the menu
        
        System.out.println("--- Welcome to the database " + username + "!: ---");
        System.out.println("1. Retrieve ISBN, title, genre name, publication date, name of the publisher, edition number and description of each book.");
        System.out.println("2. Retrieve for a book first, middle and last names of its authors.");
        System.out.println("3. Retrieve ISBN, title and barcode of every book copy.");
        System.out.println("4. Retrieve card number, first, middle and last name of every member.");
        System.out.println("5. Retieve info (ISBN, title, barcode, date borrowed and number of renewals) of every loan that was not finalized for a chosen member.");
        System.out.println("6. Register in the system the return of a book a chosen member borrowed.");
        System.out.println("7. Borrow a book copy to a chosen member.");
        System.out.println("8. Renew a loan of a book copy to a chosen member.");
        System.out.println("9. Retrieve how much money a chosen member owns to the library.");
        System.out.println("10.Retrieve for a member ISBN, title, barcode, date borrowed, date returned and fee for every book he owns money for to the library.");
        System.out.println("11.Register in the system, for a member, a payment for a loan of a book copy that was overdue");
        System.out.println("12.Exit");
        while(!cont)
        {
            //prompt the user for a menu choice
            System.out.print("Please choose on of the options above: ");
            choice = input.nextInt();
            if (choice < 1 || choice > 12)
            {
                // if invalid input, reprompt the user
                System.out.println("Invalid input. Please enter a value 1-12.");
                continue;
            }
            else 
            {
                cont = true;
            }

            if(choice == 1)
            {
                try 
                {
                   //Creates a statement to be used as query in the next line
                    Statement st = conn.createStatement();
                    //Executes the specified query and saves the results to the ResultSet rs to be viewed in the next line
                    ResultSet rs = st.executeQuery("SELECT b.ISBN, b.title, g.name, b.date_published, b.publisher, b.edition from book b join genre g on b.genre_id = g.genre_id;");
                    while(rs.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("ISBN: ");
                        System.out.println(rs.getString("ISBN"));
                        System.out.println("Title: ");
                        System.out.println(rs.getString("title"));
                        System.out.println("Genre: ");
                        System.out.println(rs.getString("name"));
                        System.out.println("Publishing Date: ");
                        System.out.println(rs.getString("date_published"));
                        System.out.println("Publisher: ");
                        System.out.println(rs.getString("publisher"));
                        System.out.println("Edition: ");
                        System.out.println(rs.getString("edition"));
                        System.out.println("-----------------------------");
                    }
                }
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(choice == 2)
            {
                try
                {
                    System.out.print("Please enter book ISBN: ");
                    String ISBN = input.next();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement pst = conn.prepareStatement("select b.title, a.first_name, a.middle_name, a.last_name from book b join book_author ba on b.isbn = ba.isbn join author a on ba.author_id = a.author_id where b.isbn=?;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    pst.setString(1, ISBN);
                    ResultSet rs = pst.executeQuery();
                    while(rs.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("Book title:");
                        System.out.println(rs.getString("title"));
                        System.out.println("Author First Name: ");
                        System.out.println(rs.getString("first_name"));
                        System.out.println("Author Middle Name:");
                        System.out.println(rs.getString("middle_name"));
                        System.out.println("Author Last Name:");
                        System.out.println(rs.getString("last_name"));
                        System.out.println("-----------------------------");
                    }
                }
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (choice == 3)
            {
                try
                {
                    //Creates a statement to be used as query in the next line
                    Statement st = conn.createStatement();
                    //Executes the specified query and saves the results to the ResultSet rs to be viewed in the next line
                    ResultSet rs = st.executeQuery("select b.isbn, b.title, c.barcode from book b join copy c on b.isbn=c.isbn;");
                    while(rs.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("Book ISBN: ");
                        System.out.println(rs.getString("ISBN"));
                        System.out.println("Book Title:");
                        System.out.println(rs.getString("title"));
                        System.out.println("Barcode:");
                        System.out.println(rs.getString("barcode"));
                        System.out.println("-----------------------------");
                    }
                }
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (choice == 4)
            {
                try 
                {
                    //Creates a statement to be used as query in the next line
                    Statement st = conn.createStatement();
                    //Executes the specified query and saves the results to the ResultSet rs to be viewed in the next line
                    ResultSet rs = st.executeQuery("select card_no, first_name, middle_name, last_name from member");
                    while(rs.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("Member Card Number:");
                        System.out.println(rs.getString("card_no"));
                        System.out.println("Member First name:");
                        System.out.println(rs.getString("first_name"));
                        System.out.println("Member Middle Name:");
                        System.out.println(rs.getString("middle_name"));
                        System.out.println("Member Last Name:");
                        System.out.println(rs.getString("last_name"));
                        System.out.println("-----------------------------");
                    }
                }
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (choice == 5)
            {
                try 
                {
                    System.out.print("Please enter member card number: ");
                    int card_no = input.nextInt();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement pstmt = conn.prepareStatement("select b.isbn,b.title,bo.barcode,bo.date_borrowed,bo.date_returned from book b join copy c on b.isbn=c.isbn join borrow bo on c.barcode=bo.barcode where date_returned is null and bo.card_no=?");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    pstmt.setInt(1, card_no);
                    ResultSet rs = pstmt.executeQuery();
                    while(rs.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("ISBN: ");
                        System.out.println(rs.getString("ISBN"));
                        System.out.println("Title: ");
                        System.out.println(rs.getString("title"));
                        System.out.println("Barcode:");
                        System.out.println(rs.getString("barcode"));
                        System.out.println("Date Borrowed:");
                        System.out.println(rs.getString("date_borrowed"));
                        System.out.println("Date Returned:");
                        System.out.println(rs.getString("date_returned"));
                        System.out.println("-----------------------------");
                    }
                } 
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
            if (choice == 6)
            {
                try 
                {  
                    System.out.print("Please enter user card number: ");
                    int card_no = input.nextInt();
                    System.out.print("Please enter book barcode: ");
                    int barcode = input.nextInt();
                    Statement st = conn.createStatement();
                    System.out.println(" ");
                    System.out.println(" ");
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement pstmt = conn.prepareStatement("UPDATE borrow set date_returned=now() where card_no=? and barcode=?;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    pstmt.setInt(2, card_no);
                    pstmt.setInt(3, barcode);
                    //Rather than executing a query, the update command looks specifically for the UPDATE, INSERT, or DELETE operations.
                    pstmt.executeUpdate();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement ps= conn.prepareStatement("select b.title,bo.card_no,bo.barcode,bo.date_borrowed from borrow bo join copy c on bo.barcode=c.barcode join book b on c.isbn=b.isbn where bo.card_no=? and bo.barcode=?;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    ps.setInt(2, card_no);
                    ps.setInt(3, barcode);
                    ResultSet rs2 = ps.executeQuery();
                    System.out.println(" ");
                    System.out.println("Updated Table:");
                    while(rs2.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("Member Card Number:");
                        System.out.println(rs2.getString("card_no"));
                        System.out.println("Book Title: ");
                        System.out.println(rs2.getString("title"));
                        System.out.println("Book Barcode:");
                        System.out.println(rs2.getString("barcode"));
                        System.out.println("Date Borrowed:");
                        System.out.println(rs2.getString("date_borrowed"));
                        System.out.println("Date Returned:");
                        System.out.println(rs2.getString("date_returned"));
                        System.out.println("Number of Renewals:");
                        System.out.println(rs2.getString("renewals_no"));
                        System.out.println("Paid:");
                        System.out.println(rs2.getString("paid"));
                        System.out.println("-----------------------------");
                    }
                    System.out.print("Updated return date for member with card number " + card_no + " and book with barcode " + barcode + " to current date");
                } 
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e) 
                {
                    e.printStackTrace();;
                }
            }
            if (choice == 7)
            {
                try 
                {  
                    System.out.print("Please enter user card number: ");
                    int card_no = input.nextInt();
                    System.out.print("Please enter book barcode: ");
                    int barcode = input.nextInt();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement pstmt = conn.prepareStatement("insert into BORROW (card_no, barcode, date_borrowed, date_returned, renewals_no, paid) values (?, ?,now(), null, 0, null);");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    pstmt.setInt(1, card_no);
                    pstmt.setInt(2, barcode);
                    pstmt.executeUpdate();
                    System.out.println("-----------------------------");
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement pstmt2 = conn.prepareStatement("select b.title,bo.card_no,bo.barcode,bo.date_borrowed,bo.date_returned,bo.renewals_no,bo.paid from borrow bo join copy c on bo.barcode=c.barcode join book b on c.isbn=b.isbn where bo.card_no=? and bo.barcode=?;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    pstmt2.setInt(1, card_no);
                    pstmt2.setInt(2, barcode);
                    ResultSet rs2 = pstmt2.executeQuery();

                    while(rs2.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("Member Card Number:");
                        System.out.println(rs2.getString("card_no"));
                        System.out.println("Book Title:");
                        System.out.println(rs2.getString("title"));
                        System.out.println("Book Barcode:");
                        System.out.println(rs2.getString("barcode"));
                        System.out.println("Date Borrowed:");
                        System.out.println(rs2.getString("date_borrowed"));
                        System.out.println("Date Returned:");
                        System.out.println(rs2.getString("date_returned"));
                        System.out.println("Number of Renewals:");
                        System.out.println(rs2.getString("renewals_no"));
                        System.out.println("Paid:");
                        System.out.println(rs2.getString("paid"));
                        System.out.println("-----------------------------");
                    }
                } 
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
            if (choice == 8)
            {
                try 
                {
                    /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                    System.out.print("Please enter user card number: ");
                    int card_no = input.nextInt();
                    System.out.print("Please enter book barcode: ");
                    int barcode = input.nextInt();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement pst = conn.prepareStatement("UPDATE borrow set renewals_no=renewals_no+1 where card_no=? and barcode=?;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    pst.setInt(1, card_no);
                    pst.setInt(2, barcode);
                    pst.executeUpdate();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement ps = conn.prepareStatement("select b.title,bo.card_no,bo.barcode,bo.date_borrowed,bo.renewals_no from borrow bo join copy c on bo.barcode=c.barcode join book b on c.isbn=b.isbn where bo.card_no=? and bo.barcode=?;");
                   //Takes the user input previously recieved and provides it to the prepared statement for use.
                    ps.setInt(1, card_no);
                    ps.setInt(2, barcode);
                    ResultSet rs2 = ps.executeQuery();
                    while(rs2.next())
                    {
                        System.out.println("-----------------------------");
                        System.out.println("Member Card Number:");
                        System.out.println(rs2.getString("card_no"));
                        System.out.println("Book Title:");
                        System.out.println(rs2.getString("title"));
                        System.out.println("Book Barcode:");
                        System.out.println(rs2.getString("barcode"));
                        System.out.println("Number of Renewals:");
                        System.out.println(rs2.getString("renewals_no"));                        
                        System.out.println("-----------------------------");
                    }
                } 
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
            if (choice == 9)
            {
                try 
                {
                    System.out.print("Please enter member card number: ");
                    int card_no = input.nextInt();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement ps= conn.prepareStatement("select card_no,abs(sum(datediff(ifnull(date(date_returned), curdate()), date(adddate(date_borrowed, (renewals_no+1)*14))))*0.25) as amount from borrow where card_no=? and date_returned is null group by barcode;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    ps.setInt(1, card_no);
                    ResultSet rs = ps.executeQuery();
                    int sum=0;
                    while(rs.next())
                    {
                        int stored = rs.getInt("amount");
                        sum=sum+stored;
                    }
                    System.out.println("-----------------------------");
                    System.out.println("Amount owed by member: ");
                    System.out.println(sum);
                    System.out.println("-----------------------------");
                    
                } 
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
            if (choice == 10)
            {
                try 
                {
                    System.out.print("Please enter member card number: ");
                    int card_no = input.nextInt();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement ps= conn.prepareStatement("select b.isbn,b.title,bo.barcode,bo.date_borrowed,bo.date_returned,abs(sum(datediff(ifnull(date(date_returned), curdate()), date(adddate(date_borrowed, (renewals_no+1)*14)))))*0.25 from book b join copy c on b.isbn=c.isbn join borrow bo on c.barcode=bo.barcode where date_returned is null and paid is null or paid = 0 and bo.card_no=? group by b.isbn;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    ps.setInt(1, card_no);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("Book ISBN:");
                        System.out.println(rs.getString("ISBN"));
                        System.out.println("Book Title:");
                        System.out.println(rs.getString("title"));
                        System.out.println("Book Barcode:");
                        System.out.println(rs.getInt("barcode"));
                        System.out.println("Date Borrowed:");
                        System.out.println(rs.getDate("date_borrowed"));
                        System.out.println("Date Returned:");
                        System.out.println(rs.getDate("date_returned"));
                        System.out.println("Amount owed:");
                        System.out.println(rs.getInt("abs(sum(datediff(ifnull(date(date_returned), curdate()), date(adddate(date_borrowed, (renewals_no+1)*14)))))*0.25"));
                        System.out.println("-----------------------------");
                    }
                } 
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
            if (choice == 11)
            {
                try 
                {
                    System.out.print("Please enter a member card number: ");
                    int card_no = input.nextInt();
                    System.out.print("Please enter barcode number: ");
                    int barcode = input.nextInt();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement ps = conn.prepareStatement("UPDATE borrow set paid = true where paid=0 or paid is null and card_no=? and barcode=?;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    ps.setInt(1,card_no);
                    ps.setInt(2, barcode);
                    ps.executeUpdate();
                    /* Sets up a prepared statement for the program, allowing the user to provide input
                     * and store it in a variable to be used within the SQL query.
                    */
                    PreparedStatement ps2 = conn.prepareStatement("select card_no, barcode, paid from borrow where card_no=? and barcode=?;");
                    //Takes the user input previously recieved and provides it to the prepared statement for use.
                    ps2.setInt(1,card_no);
                    ps2.setInt(2, barcode);
                    ResultSet rs = ps2.executeQuery();
                    while(rs.next())
                    {
                        /*While the result set still has values to display, locates the desired column name
                         *using .getString() and displays the result that is found to the console for the
                         user to see.
                         */
                        System.out.println("-----------------------------");
                        System.out.println("Member Card Number:");
                        System.out.println(rs.getString("card_no"));
                        System.out.println("\nSetting value paid to true....\n");
                        System.out.println("Is paid: ");
                        System.out.println(rs.getBoolean("paid"));
                        System.out.println("-----------------------------");
                    }
                } 
                // catch statement looks to see if any errors were made in the SQL statement above and prints the stacktrace of the error if found.
                catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
            if (choice == 12)
            {
                System.out.println("Goodbye!");
                end = false;
                input.close();
            }
        }
    }
}