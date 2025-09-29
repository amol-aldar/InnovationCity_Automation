package org.rakdao.utils;

import java.util.Random;

public class UserGenerator {
    private static final String[] FIRST_NAMES = {
            "John", "Alice", "Michael", "Emma", "David", "Sophia",
            "Liam", "Olivia", "Noah", "Ava", "Elijah", "Isabella",
            "James", "Mia", "William", "Charlotte", "Benjamin", "Amelia",
            "Lucas", "Harper", "Henry", "Evelyn", "Alexander", "Abigail",
            "Daniel", "Emily", "Matthew", "Ella", "Jackson", "Elizabeth",
            "Sebastian", "Camila", "Jack", "Luna", "Owen", "Sofia",
            "Gabriel", "Avery", "Carter", "Mila", "Jayden", "Aria",
            "Grayson", "Scarlett", "Leo", "Penelope", "Julian", "Chloe",
            "Hudson", "Layla"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Miller",
            "Davis", "Garcia", "Rodriguez", "Wilson", "Martinez", "Anderson",
            "Taylor", "Thomas", "Hernandez", "Moore", "Martin", "Jackson",
            "Thompson", "White", "Lopez", "Lee", "Gonzalez", "Harris",
            "Clark", "Lewis", "Robinson", "Walker", "Perez", "Hall",
            "Young", "Allen", "Sanchez", "Wright", "King", "Scott",
            "Green", "Baker", "Adams", "Nelson", "Hill", "Ramirez",
            "Campbell", "Mitchell", "Roberts", "Carter", "Phillips", "Evans",
            "Turner", "Torres"
    };

    private static final String[] COMPANY_NAMES = {
            "TechCorp", "InnoSoft", "AlphaDev", "BrightWorks", "NextGen",
            "CyberLogic", "DataNimbus", "NetFusion", "CloudMatrix", "SoftNova",
            "QuantumLeap", "DevSolutions", "NovaEdge", "BlueWave", "SkyLabs",
            "ZenithCode", "PixelHub", "CoreApps", "NexaTech", "SoftCircuit",
            "InnovaTech", "RapidWare", "SmartLogic", "CodeStream", "FlexSystems",
            "ByteForge", "LogicPulse", "MatrixWare", "GridNet", "InfoSpark",
            "DataBridge", "BrightPixel", "CodeHive", "BlueNova", "SoftEdge",
            "NextLogic", "VisionSoft", "Cloudify", "DeltaApps", "ProximaTech",
            "SynapseSoft", "QuantumCore", "TechNest", "PrimeLogic", "SoftSphere",
            "NeoWare", "InnoByte", "OptiTech", "CoreFusion", "CyberNest"
    };

    public static User generateUser() {
        Random random = new Random();

        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String companyName = COMPANY_NAMES[random.nextInt(COMPANY_NAMES.length)];
        String mobileNumber = generateRandomMobileNumber();

        int count = CounterUtil.getNextCount();
        String email = "a.aldar+" + count + "@innovationcity.com";

        return new User(firstName, lastName, companyName, mobileNumber, email);
    }

    private static String generateRandomMobileNumber() {
        Random random = new Random();
        StringBuilder mobile = new StringBuilder("05");
        for (int i = 0; i < 8; i++) {
            mobile.append(random.nextInt(10));
        }
        return mobile.toString();
    }
}

