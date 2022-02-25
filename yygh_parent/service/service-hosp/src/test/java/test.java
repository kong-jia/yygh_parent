import com.kong.yygh.task.utils.MD5;

import java.util.Date;
import java.util.Random;

public class test {
    public static void main(String[] args) {
        Random random=new Random();
        Date d=new Date();
        String a= MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000));
        System.out.println(a);
        System.out.println(d);
    }
}
