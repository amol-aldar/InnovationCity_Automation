package org.rakdao.utils;

import java.util.Random;

public class UserGenerator {
    private static final String[] FIRST_NAMES = {
            "Aria", "Brandon", "Caitlyn", "Derek", "Elena", "Felix",
            "Giselle", "Hector", "Isla", "Jasper", "Kara", "Landon",
            "Marina", "Nolan", "Ophelia", "Preston", "Quinn", "Rafael",
            "Selena", "Tobias", "Uma", "Victor", "Wendy", "Xander",
            "Yara", "Zane", "Alden", "Bianca", "Colton", "Dahlia",
            "Ethan", "Fiona", "Griffin", "Hazel", "Ian", "Juliana",
            "Kai", "Livia", "Milo", "Nia", "Orion", "Paige",
            "Quincy", "Riley", "Soren", "Thalia", "Ulric", "Vera",
            "Wyatt", "Ximena"
    };

    private static final String[] LAST_NAMES = {
            "Armstrong", "Bennett", "Carver", "Dalton", "Everett", "Fletcher",
            "Grayson", "Hawkins", "Iverson", "Jacobs", "Kingsley", "Lang",
            "Montgomery", "Nash", "Oakley", "Prescott", "Quinn", "Ramsey",
            "Sterling", "Thorne", "Underwood", "Vance", "Weston", "Yates",
            "Zimmerman", "Ashford", "Barker", "Chandler", "Donovan", "Ellis",
            "Frost", "Garrison", "Huxley", "Ingram", "Jefferson", "Kendrick",
            "Lockhart", "Maddox", "North", "Ortega", "Patterson", "Quigley",
            "Rowan", "Sinclair", "Tucker", "Upton", "Valentine", "Winslow"
    };

    private static final String[] COMPANY_NAMES = {
            "AeroTech", "BrightLabs", "CloudVista", "DataForge", "EcoLogic",
            "FutureWave", "GreenByte", "Hyperion", "InnovaWorks", "JetStream",
            "KineticSoft", "LuminaTech", "MetaWorks", "NeoLogic", "OptimaCore",
            "PixelRise", "QuantumSoft", "RapidSolutions", "SkylineTech", "TechNova",
            "UltraByte", "VectorLabs", "Wavefront", "XenoTech", "YieldSoft",
            "ZenLabs", "AlphaWave", "BlueMatrix", "CoreVista", "DeltaByte",
            "EvoTech", "FluxLogic", "GlowSoft", "HorizonLabs", "IntelliCore",
            "JadeSoft", "KryptonTech", "LightWave", "MindCore", "NovaSphere",
            "OnyxLabs", "PrimeSoft", "QuantumWave", "RedPixel", "SolarTech",
            "TitanLabs", "UnityWorks", "VortexSoft", "WhiteWave", "ZenithLabs"
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

