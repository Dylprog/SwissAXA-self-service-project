package com.example.axaproject;

import java.util.ArrayList;
import java.util.List;

public final class PolicyRepository {
    private static List<Policy> policies;

    private PolicyRepository() {
    }

    public static List<Policy> getPolicies() {
        if (policies == null) {
            policies = new ArrayList<>();
            policies.add(new Policy("Life Insurance", "AXA-CH-1001", "2035-01-15", false));
            policies.add(new Policy("Car Insurance", "AXA-CH-1002", "2025-12-15", false));
            policies.add(new Policy("House Insurance", "AXA-CH-1003", "2026-06-30", false));
            policies.add(new Policy("Health Insurance", "AXA-CH-1004", "2026-03-01", false));
            policies.add(new Policy("Travel Insurance", "AXA-CH-1005", "2025-08-21", false));
        }
        return policies;
    }
}
