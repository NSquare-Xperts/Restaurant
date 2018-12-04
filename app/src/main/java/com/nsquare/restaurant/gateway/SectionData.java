package com.nsquare.restaurant.gateway;

import android.util.JsonReader;

import com.nsquare.restaurant.Production;
import com.nsquare.restaurant.Section;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SectionData {
    private Map<String, Production> productionMap = new HashMap();
    private List<Section> sectionList = new LinkedList();

    public List<Section> getSectionList() {
        return this.sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public void setProductionList(List<Production> productionList) {
        this.productionMap = new HashMap();
        for (Production production : productionList) {
            this.productionMap.put(production.getProductionId(), production);
        }
    }

    public Production getProduction(String productionId) {
        return (Production) this.productionMap.get(productionId);
    }

    public int getTotalSize() {
        int total = 0;
        for (Section section : this.sectionList) {
            total += section.getProductionIds().size();
        }
        return total;
    }

    public static SectionData readSectionData(JsonReader reader) throws IOException {
        SectionData sectionData = new SectionData();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("productions")) {
                sectionData.setProductionList(Production.readProductionList(reader));
            } else if (name.equals("sections")) {
                sectionData.setSectionList(readSectionList(reader));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return sectionData;
    }

    public static List<Section> readSectionList(JsonReader reader) throws IOException {
        List<Section> sectionList = new LinkedList();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            Section section = new Section();
            while (reader.hasNext()) {
                String name = reader.nextName();
                Integer obj = Integer.valueOf(-1);
                switch (name.hashCode()) {
                    case -1878095475:
                        if (!name.equals("darkColor")) {
                            break;
                        }
                        obj = Integer.valueOf(1);
                        break;
                    case -234567699:
                        if (!name.equals("lightColor")) {
                            break;
                        }
                        break;
                    case 110371416:
                        if (!name.equals("jhb")) {
                            break;
                        }
                        obj = Integer.valueOf(3);
                        break;
                    case 1638765110:
                        if (!name.equals("iconUrl")) {
                            break;
                        }
                        obj = Integer.valueOf(2);
                        break;
                    case 1668433727:
                        if (!name.equals("productionIds")) {
                            break;
                        }
                        Integer.valueOf(4);
                        break;
                    default:
                        break;
                }
            }
            if (section.getProductionIds().size() > 0) {
                sectionList.add(section);
            }
            reader.endObject();
        }
        reader.endArray();
        return sectionList;
    }

    private static List<String> readProductionIds(JsonReader reader) throws IOException {
        List<String> productionIds = new LinkedList();
        reader.beginArray();
        while (reader.hasNext()) {
            productionIds.add(reader.nextString());
        }
        reader.endArray();
        return productionIds;
    }
}
