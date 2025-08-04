package test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import sqa.main.TVPlan;

class TVPackageTestEED {

	  static Stream<Arguments> provideEEDCases() {
	        return Stream.of(
	            // rule, EC1, EC2, EC3, expected
	            Arguments.of("R1",  "STANDARD", "none",   "N", 150.0),
	            Arguments.of("R2",  "STANDARD", "offline","N", 250.0),
	            Arguments.of("R3",  "STANDARD", "live",   "N", 250.0),
	            Arguments.of("R4",  "STANDARD", "offline/live",   "N", 350.0),
	            Arguments.of("R5",  "STANDARD", "none",   "Y", 100.0),
	            Arguments.of("R6",  "STANDARD", "offline","Y", 200.0),
	            Arguments.of("R7",  "STANDARD", "live",   "Y", 200.0),
	            Arguments.of("R8",  "STANDARD", "offline/live",   "Y", 300.0),

	            Arguments.of("R9",  "PREMIUM",  "none",   "N", 350.0),
	            Arguments.of("R10", "PREMIUM",  "offline","N", 450.0),
	            Arguments.of("R11", "PREMIUM",  "live",   "N", 450.0),
	            Arguments.of("R12", "PREMIUM",  "offline/live",   "N", 550.0),
	            Arguments.of("R13", "PREMIUM",  "none",   "Y", 300.0),
	            Arguments.of("R14", "PREMIUM",  "offline","Y", 400.0),
	            Arguments.of("R15", "PREMIUM",  "live",   "Y", 400.0),
	            Arguments.of("R16", "PREMIUM",  "offline/live",   "Y", 500.0),

	            Arguments.of("R17", "FAMILY",   "none",   "N", 450.0),
	            Arguments.of("R18", "FAMILY",   "offline","N", Double.NaN), // Impossible
	            Arguments.of("R19", "FAMILY",   "live",   "N", 550.0),
	            Arguments.of("R20", "FAMILY",   "offline/live",   "N", Double.NaN), // Impossible
	            Arguments.of("R21", "FAMILY",   "none",   "Y", 400.0),
	            Arguments.of("R22", "FAMILY",   "offline","Y", Double.NaN), // Impossible
	            Arguments.of("R23", "FAMILY",   "live",   "Y", 500.0),
	            Arguments.of("R24", "FAMILY",   "offline/live",   "Y", Double.NaN)  // Impossible
	        );
	    }

	    @ParameterizedTest
	    @MethodSource("provideEEDCases")
	    void testByEC(String rule, String ec1, String ec2, String ec3, double expected) throws Exception {
	        TVPlan.TVPackage tvPackage = ec1.equalsIgnoreCase("NONE") ? null : TVPlan.TVPackage.valueOf(ec1);
	        boolean offline = false, live = false;

	        switch (ec2.toLowerCase()) {
	            case "offline": offline = true; break;
	            case "live":    live = true;    break;
	            case "both":    offline = true; live = true; break;
	        }

	        boolean discount = ec3.equalsIgnoreCase("Y");

	        var plan = new TVPlan(offline, live, discount);

	        double actual;
	        if (tvPackage == null) {
	            actual = 0.0;
	        } else {
	            Method method = TVPlan.class.getDeclaredMethod("pricePerMonth", TVPlan.TVPackage.class);
	            method.setAccessible(true);
	            actual = (double) method.invoke(plan, tvPackage);
	        }

	        if (Double.isNaN(expected)) {
	            fail(String.format("[%s] Impossible case: EC1=%s EC2=%s EC3=%s", rule, ec1, ec2, ec3));
	        } else {
	            assertEquals(expected, actual, 0.001,
	                    String.format("[%s] EC1=%s EC2=%s EC3=%s", rule, ec1, ec2, ec3));
	        }
	    }
}
