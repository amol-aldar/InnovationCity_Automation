package org.rakdao.utils;

import java.util.Random;

public class UserGenerator {
    private static final String[] FIRST_NAMES = {
            "Aarav", "Ananya", "Rohan", "Isha", "Vivaan", "Saanvi",
            "Arjun", "Diya", "Kabir", "Mira", "Aditya", "Kavya",
            "Neil", "Tanya", "Riya", "Shaurya", "Anika", "Krishna",
            "Sai", "Meera", "Vishal", "Pooja", "Aryan", "Ishita",
            "Dev", "Naina", "Karan", "Sneha", "Raghav", "Tanvi",
            "Yash", "Shreya", "Ritvik", "Priya", "Om", "Aishwarya",
            "Dhruv", "Anjali", "Sameer", "Radhika", "Harsh", "Nidhi",
            "Pranav", "Ira", "Jay", "Tara", "Manav", "Aditi", "Kshitij", "Suhana"
    };

    private static final String[] LAST_NAMES = {
            "Sharma", "Verma", "Patel", "Reddy", "Iyer", "Kapoor",
            "Singh", "Mehta", "Chopra", "Gupta", "Joshi", "Desai",
            "Nair", "Bhatia", "Kumar", "Mukherjee", "Rao", "Saxena",
            "Malhotra", "Chatterjee", "Jain", "Tripathi", "Aggarwal", "Shetty",
            "Bose", "Thakur", "Pillai", "Bhattacharya", "Menon", "Ranganathan",
            "Kaul", "Tandon", "Ghosh", "Chakraborty", "Singhal", "Varma",
            "Mishra", "Nanda", "Puri", "Shukla", "Chaudhary", "Yadav",
            "Sinha", "Dutt", "Pandey", "Mahajan", "Arora", "Lal"
    };

    private static final String[] COMPANY_NAMES = {
            "AaravTech", "BanyanSoft", "ChetanLabs", "DesiWorks", "EcoBharat",
            "FusionIndia", "GreenPixel", "HorizonTech", "IndiNova", "JewelSoft",
            "KiranTech", "LuminaIndia", "NexaLabs", "OmniCore", "PragatiTech",
            "QuantumBharat", "RapidLabs", "SkylineIndia", "TechSutra", "UltraByte",
            "VedaWorks", "WaveLabs", "XploreTech", "YuktiSoft", "ZenithIndia",
            "AlphaBharat", "BlueLotusTech", "CoreVeda", "DeltaLabs", "EvoTech",
            "FusionWorks", "GlowLabs", "HorizonCore", "IntelliIndia", "JadeSoft",
            "KryptonLabs", "LightWaveTech", "MindCore", "NovaSphere", "OnyxLabs",
            "PrimeSoft", "QuantumWave", "RedPixel", "SolarTech", "TitanLabs",
            "UnityWorks", "VortexSoft", "WhiteWave", "ZenLabs"
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

