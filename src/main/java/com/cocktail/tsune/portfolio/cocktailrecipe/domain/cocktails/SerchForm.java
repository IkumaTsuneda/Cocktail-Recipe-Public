package com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SerchForm {
    @Size(max = 256, message = "カクテル名は256桁以内で入力してください")
    private String word;

    // @NotEmpty(message = "ベースを選択してください")
    private int base;

    // @NotEmpty(message = "度数の下限を選択してください")
    private int alcohol_from;

    // @NotEmpty(message = "度数の上限を選択してください")
    private int alcohol_to;

}
