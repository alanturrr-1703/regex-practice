package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Solution.extractIpAddresses().
 * All tests FAIL until the method is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testTwoIpsInText() {
        List<String> result = solution.extractIpAddresses("server at 192.168.1.1 and 10.0.0.1");
        assertEquals(Arrays.asList("192.168.1.1", "10.0.0.1"), result);
    }

    @Test
    void testNoIps() {
        List<String> result = solution.extractIpAddresses("no ips here");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testSingleIp() {
        List<String> result = solution.extractIpAddresses("127.0.0.1");
        assertEquals(Collections.singletonList("127.0.0.1"), result);
    }

    @Test
    void testMultipleIps() {
        List<String> result = solution.extractIpAddresses("1.2.3.4 and 255.255.255.0");
        assertEquals(Arrays.asList("1.2.3.4", "255.255.255.0"), result);
    }

    @Test
    void testEmptyString() {
        List<String> result = solution.extractIpAddresses("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testFiveOctetGroupNotMatched() {
        // "1.2.3.4.5" has 5 groups — should not produce a match
        List<String> result = solution.extractIpAddresses("1.2.3.4.5");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testAllZeros() {
        List<String> result = solution.extractIpAddresses("default: 0.0.0.0");
        assertEquals(Collections.singletonList("0.0.0.0"), result);
    }

    @Test
    void testIpInContext() {
        List<String> result = solution.extractIpAddresses("Connected from 10.20.30.40 at port 8080");
        assertEquals(Collections.singletonList("10.20.30.40"), result);
    }

    @Test
    void testNoDotsNoMatch() {
        // No literal dots → no match
        List<String> result = solution.extractIpAddresses("1234567890");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testOnlyThreeGroupsNoMatch() {
        List<String> result = solution.extractIpAddresses("1.2.3 is not an IP");
        assertEquals(Collections.emptyList(), result);
    }
}
