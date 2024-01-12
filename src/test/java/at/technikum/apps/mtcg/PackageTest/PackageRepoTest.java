package at.technikum.apps.mtcg.PackageTest;

import at.technikum.apps.mtcg.entity.Pack;
import at.technikum.apps.mtcg.repository.PackRepository;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PackageRepoTest {
    @Test
    void save_CreatePackage() {
        PackRepository packageRepository = new PackRepository();
        Pack newPack = new Pack();
        newPack.setPackId(UUID.randomUUID()); // Generating a random UUID

        Pack savedPackage = packageRepository.save(newPack);
        assertNotNull(savedPackage, "Package should not be null");
        System.out.println("Package created: " + savedPackage);
    }
}
