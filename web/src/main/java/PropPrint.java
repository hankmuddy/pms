import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * @author vomel
 * @since 29.01.14 13:13
 * Useful tool to play around with messages
 */
public class PropPrint {
    public static void main(String[] args) throws UnsupportedEncodingException {
/*        String[] split = props.split("\n");
        Set<String> set = new TreeSet<>(Arrays.asList(split));
        for (String s : set) {
            System.out.println(s);
        }*/
//        System.out.println("System.getenv() = " + System.getenv());
//        System.out.println("System.getenv() = " + System.getProperties());
        System.out.println("Locale.getDefault() = " + Locale.getDefault());
        System.out.println("Charset.defaultCharset() = " + Charset.defaultCharset());
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag("rsu"));

        Enumeration<String> keys = bundle.getKeys();
        Map<String, Object> map = new TreeMap<>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String object = (String) bundle.getObject(key);
//            String object = new String(String.valueOf(bundle.getObject(key)).getBytes("ISO-8859-1"));

//            System.out.println(key+"=" + object);
            if (object.toCharArray().length > 0 && object.toCharArray()[object.toCharArray().length - 1] >= 1000)
//            if (key.startsWith("facility"))
                map.put(key, object);
        }
        int id = 1;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            //INSERT INTO logins.facility (type, id, active, createddate, updateddate, name, chargefree) VALUES ('HOTEL', 1, true, null, null, 'qwe', 'N_A');
//            System.out.println("INSERT INTO logins.facility (type, id, active, createddate, updateddate, name, chargefree) VALUES ('HOTEL', " + id++ + ", true, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', '" + entry.getKey() + "', 'N_A');");
//            System.out.println("('ROOM_TYPE', " + id++ + ", true, '2014-02-19 10:09:24.0', '2014-02-19 10:09:24.0', '" + entry.getKey() + "', null),");
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }


}
