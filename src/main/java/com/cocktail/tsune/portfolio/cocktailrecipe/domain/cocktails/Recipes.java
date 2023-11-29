package com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails;

import lombok.Data;

@Data
public class Recipes {
    // 材料ID
    private String ingredient_id;
    // 材料名
    private String ingredient_name;
    // 分量
    private String amount;
    // 単位
    private String unit;
}
