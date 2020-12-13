import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Application extends JFrame {


    private JLabel label;

    //Инициализаця интерфейса
    Application() {
        this.initUI();
    }

    //метод чтения файла из архива JAR
    private String readInnerTextFile(String path) {
        String data = "";
        try{
            InputStream is = getClass().getResourceAsStream(path);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null)
            {
                data += line;
            }
            br.close();
            isr.close();
            is.close();
        }
        catch (Exception e){

        }
        return data;
    }

    //метод чтения файла из внешнего файла
    private String readOuterTextFile(String path) {
        String data = "";
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine()+"\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }

    //читаем с базы данных
    private String readFromDB(String path){

        String data = "<html>";
        //читаем конфиг
        String config = readOuterTextFile(path);
        //парсим по строчно
        String[] parseConfig = config.split("\n");
        String port = "";
        String dbName = "";
        String user = "";
        String password = "";
        //для каждой строки методом парс находим ключ значение
        for(int i=0; i<parseConfig.length; i++){
            String[] val = parseConfig[i].split(":");

            //проверяем по ключам и заполняем значения
            switch (val[0]){
                case "port": port = val[1]; break;
                case "user": user = val[1]; break;
                case "dbName": dbName = val[1]; break;
                case "password": password = val[1]; break;
                default: break;
            }
        }

        //формируем строку подключения.
        try{
            String url2 = "jdbc:mysql://127.0.0.1:3307/library";
            user = "marident";
            password= "111";

            String url = "jdbc:mysql://localhost:"+port+"/"+dbName;
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url,user,password);
//here sonoo is database name, root is username and password
            Statement stmt=con.createStatement();
            //читаем из таблицы книг данные
            ResultSet rs=stmt.executeQuery("select * from book");
            while(rs.next())
                data += rs.getString(2)+"<br/>";
            con.close();
        }catch(Exception e){ e.printStackTrace();}
        data+="</html>";
        return data;
    }

    //Инициализация интерфейса
    private void initUI() {

        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);

        label = new JLabel("Text");

        //если читаем внутренний файл
        this.setTitle("Содержимое файла внутри архива");
        label.setText(readInnerTextFile("inner.txt"));

        //если читаем внешний файл
        /*
        this.setTitle("Содержимое файла вне архива");
        label.setText(readOuterTextFile("outer.txt"));
        */

        /*this.setTitle("Содержимое таблицы в БД");
        label.setText(readFromDB("config.txt"));
        setSize(400, 200);
*/
        setSize(400, 200);
        setLayout(new GridLayout(2, 1));
        add(label);


        //Раскоментируйте это чтобы добавить картинку в окно
       /*
        this.setTitle("Содержимое файла внутри архива + картинка");
        ImageIcon icon = new ImageIcon("image.jpg");
        JLabel image = new JLabel(icon);
        add(image);
        setSize(400, 600);
        */

        //что то странное происзодит у вас с ПК
        setVisible(true);
    }

    //основная программа
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final Application ps = new Application();
                ps.setVisible(true);
            }
        });
    }

}
