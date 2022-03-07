package com.aghs.mobilevaccination.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class AlgorithmDeployService {
    @Value("${algorithm.domain}") private String domain;
    @Value("${algorithm.port}") private String port;
    @Value("${algorithm.path}") private String path;

    private <T> T getResponse(String url, JSONObject object, Class<T> responseType) throws ConnectException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(object.toString(), header);
        RestTemplate request = new RestTemplate();
        ResponseEntity<T> result =  request.postForEntity(url,entity, responseType);
        return result.getBody();
    }

    public List<List<?>> getSlotDistribution(Integer dosesPerVan, HashMap<Long, Long> slotsPerSpot)
            throws ConnectException {
        String url = String.format("http://%s:%s%s", domain, port, path);
        System.out.println("url: " + url);
        System.out.println(slotsPerSpot);
        JSONObject slotDataJson = new JSONObject();
        slotDataJson.put("dosesPerVan", dosesPerVan);
        JSONObject slotsPerSpotJson = new JSONObject();
        slotDataJson.put("slotsPerSpot", slotsPerSpotJson);
        for(Long spot: slotsPerSpot.keySet()) {
            if (slotsPerSpot.get(spot) > 0)
                slotsPerSpotJson.put(spot.toString(), slotsPerSpot.get(spot));
        }
        System.out.println(slotDataJson.toString());
        List result = getResponse(url, slotDataJson, List.class);
        if(result.get(0) instanceof List<?> result2) {
            System.out.println(result.get(0));
            if(result2.get(0) instanceof Long || result2.get(0) instanceof Integer) {
                return (List<List<?>>) result;
            }
        }
        return null;
    }


}
