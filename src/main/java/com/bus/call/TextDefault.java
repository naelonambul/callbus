package com.bus.call;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TextDefault {
    private Map<String, String> accountNames = new HashMap<>();

    protected TextDefault() {
        this.accountNames.put("LESSOR", "임대인");
        this.accountNames.put("REALTOR", "공인중개사");
        this.accountNames.put("LESSEE", "임차인");
    }

    public String nameToHan(String key){
        return accountNames.get(key);
    }
}
