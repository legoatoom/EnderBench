/*
 * Copyright (C) 2020  legoatoom
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.legoatoom.enderbench;

import net.fabricmc.loader.api.FabricLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("SameParameterValue")
public class ModConfigs {
    public static final double EnderBenchRange;
    public static final int EnderBenchSize = 15;

    public static final String configFileLoc = "../src/main/resources/configs.properties";

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(configFileLoc));
        } catch (IOException ignored) { }
        EnderBenchRange = getDoubleProperty(prop, "enderBenchRange", 32D);
    }

    private static double getDoubleProperty(Properties properties, String key, Double defaultProperty){
        String value = properties.getProperty(key, Double.toString(defaultProperty));
        double d_value;
        try {
            d_value = Double.parseDouble(value);
        } catch (NumberFormatException n){
            d_value = defaultProperty;
        }
        return d_value;
    }
}
