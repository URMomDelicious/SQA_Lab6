package test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import sqa.main.TVPlan;
import sqa.main.TVPlan.TVPackage;

public class TVPackageTestLED {

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
            // pkg, offline, live, discount, expectedPrice
            Arguments.of("STANDARD", false, false, false, 150),
            Arguments.of("STANDARD", true,  false, false, 250),
            Arguments.of("STANDARD", false, true,  false, 250),
            Arguments.of("STANDARD", true,  true,  false, 350),
            Arguments.of("STANDARD", false, false, true,  100),
            Arguments.of("STANDARD", true,  false, true,  200),
            Arguments.of("STANDARD", false, true,  true,  200),
            Arguments.of("STANDARD", true,  true,  true,  300),

            Arguments.of("PREMIUM",  false, false, false, 350),
            Arguments.of("PREMIUM",  true,  false, false, 450),
            Arguments.of("PREMIUM",  false, true,  false, 450),
            Arguments.of("PREMIUM",  true,  true,  false, 550),
            Arguments.of("PREMIUM",  false, false, true,  300),
            Arguments.of("PREMIUM",  true,  false, true,  400),
            Arguments.of("PREMIUM",  false, true,  true,  400),
            Arguments.of("PREMIUM",  true,  true,  true,  500),

            Arguments.of("FAMILY",   false, false, false, 450),
            Arguments.of("FAMILY",   true,  false, false, Double.NaN), // Impossible
            Arguments.of("FAMILY",   false, true,  false, 550),
            Arguments.of("FAMILY",   true,  true,  false, Double.NaN), // Impossible
            Arguments.of("FAMILY",   false, false, true,  400),
            Arguments.of("FAMILY",   true,  false, true,  Double.NaN), // Impossible
            Arguments.of("FAMILY",   false, true,  true,  500),
            Arguments.of("FAMILY",   true,  true,  true,  Double.NaN)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testDecision(String pkg, boolean offline, boolean live, boolean discount, double expectedPrice) throws Exception {
        if (Double.isNaN(expectedPrice)) {
            fail("Impossible case: FAMILY package cannot have offline watching");
        } else {
            TVPlan plan = new TVPlan(offline, live, discount);
            Method m = TVPlan.class.getDeclaredMethod("pricePerMonth", TVPackage.class);
            m.setAccessible(true);
            double actual = (double) m.invoke(plan, TVPackage.valueOf(pkg));
            assertEquals(expectedPrice, actual, 0.001);
        }
    }
   }



