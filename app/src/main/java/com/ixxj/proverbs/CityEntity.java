package com.ixxj.proverbs;

import com.yokeyword.indexablelistview.IndexEntity;

/**
 * Created by lintex on 2016/3/28.
 */
public class CityEntity extends IndexEntity {
    public Integer ItemId;
    public String ItemEnglish;
    public String ItemChinese;

    public CityEntity(Integer itemId, String itemEnglish, String itemChinese) {
        ItemId = itemId;
        ItemEnglish = itemEnglish;
        ItemChinese = itemChinese;
    }
}
