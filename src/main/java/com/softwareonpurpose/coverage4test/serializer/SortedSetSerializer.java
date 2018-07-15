/*Copyright 2018 Craig A. Stockton

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
package com.softwareonpurpose.coverage4test.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.SortedSet;

public class SortedSetSerializer implements JsonSerializer<SortedSet<?>> {
    @Override
    public JsonElement serialize(SortedSet<?> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null || src.isEmpty())
            return null;

        JsonArray array = new JsonArray();

        for (Object child : src) {
            JsonElement element = context.serialize(child);
            array.add(element);
        }

        return array;
    }
}
