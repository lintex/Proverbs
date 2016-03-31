package com.ixxj.proverbs;

/**
 * Created by lintex on 2016/3/24.
 */
public class ItemBean{
    public Integer ItemId;
    public String ItemEnglish;
    public String ItemChinese;

    public ItemBean(Integer itemId, String itemEnglish, String itemChinese) {
        ItemId = itemId;
        ItemEnglish = itemEnglish;
        ItemChinese = itemChinese;
    }
}
