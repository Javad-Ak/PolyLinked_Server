import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.aut.utils.JwtHandler.generateToken;
import static org.aut.utils.JwtHandler.verifyToken;

@DisplayName("------ testing general features...")
public class GeneralTests {
    @Test
    @DisplayName("---- JWT test")
    public void jwt() throws Exception {
        String subject = "user123";
        // Generate token
        String token = generateToken(subject);
        System.out.println("Generated Token: " + token);

        // Verify token
        Claims claims = verifyToken(token);
        if (claims == null) throw new Exception("test failed");
        System.out.println(claims);
    }
}