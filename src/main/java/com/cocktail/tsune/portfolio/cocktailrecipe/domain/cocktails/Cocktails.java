package com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails;

import java.util.List;

import lombok.Data;

@Data
public class Cocktails {
    // カクテルID
    private int cocktail_id;
    // カクテル名
    private String cocktail_name;
    // カクテル英語名
    private String cocktail_name_english;
    // ベース
    private String base_name;
    // 技法
    private String technique_name;
    // 味わい
    private String taste_name;
    // スタイル
    private String style_name;
    // アルコール度数
    private int alcohol;
    // TOP
    private String top_name;
    // グラス
    private String glass_name;
    // タイプ
    private String type_name;
    // カクテル要約
    private String cocktail_digest;
    // カクテル説明
    private String cocktail_desc;
    // レシピ説明
    private String recipe_desc;
    // レシピ情報リスト
    private List<Recipes> recipes;
    // カクテル画像リスト
    private String images;

}