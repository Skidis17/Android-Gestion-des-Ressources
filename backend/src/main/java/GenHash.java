import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenHash {

        public static void main(String[] args) {
            String hash = new BCryptPasswordEncoder().encode("aya123");
            System.out.println(hash);
        }


}
