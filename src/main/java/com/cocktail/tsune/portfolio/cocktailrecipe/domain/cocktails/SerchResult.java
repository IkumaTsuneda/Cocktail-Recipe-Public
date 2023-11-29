package com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails;

import java.util.List;

import lombok.Data;

@Data
public class SerchResult {

    // ステータス
    private String status;
    // エラー詳細
    private String error_detail;
    // 総ページ数
    private int total_pages;
    // 現在のページ番号
    private int current_page;
    // カクテル情報リスト
    private List<Cocktails> cocktails;
}
