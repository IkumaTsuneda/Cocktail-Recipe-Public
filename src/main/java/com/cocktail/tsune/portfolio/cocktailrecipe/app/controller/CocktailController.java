package com.cocktail.tsune.portfolio.cocktailrecipe.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cocktail.tsune.portfolio.cocktailrecipe.app.service.CocktailService;
import com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails.Cocktails;
import com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails.Recipes;
import com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails.SerchForm;
import com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails.SerchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CocktailController {

    // サービスクラス
    private final CocktailService cocktailService;

    /**
     * 初期表示処理。
     * 
     * @param serchForm 検索フォームオブジェクト
     * @param result    バリデーションの検証結果
     * @return 検索画面
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String init(
            @ModelAttribute @Validated SerchForm serchForm,
            BindingResult result) {
        return "index";
    }

    /**
     * 検索処理。
     * 
     * @param cocktail 検索フォームオブジェクト
     * @param result   バリデーションの検証結果
     * @param model    モデル
     * @return 検索結果表示画面
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PostMapping("/serch")
    public String serch(
            @ModelAttribute @Validated SerchForm serchForm,
            BindingResult result,
            Model model)
            throws JsonMappingException, JsonProcessingException, IOException {

        if (result.hasErrors()) {
            return "redirect:/";
        }

        // API実行結果
        SerchResult sr = cocktailService.serch(serchForm);

        // カクテル情報リスト
        List<Cocktails> cocktailsList = sr.getCocktails();

        model.addAttribute("serchResult", sr);
        model.addAttribute("serchForm", serchForm);
        model.addAttribute("cocktailsList", cocktailsList);

        return "cocktails/serch-result";
    }

    /**
     * 「次へ」ボタン押下時の検索処理。
     * 
     * @param serchForm   検索フォームオブジェクト
     * @param result      バリデーションの検証結果
     * @param serchResult 検索結果オブジェクト
     * @param model       モデル
     * @return 次ページの検索結果
     * @throws IOException
     */
    @RequestMapping(value = "/paging", params = "next", method = RequestMethod.POST)
    public String nextPage(
            @ModelAttribute @Validated SerchForm serchForm,
            BindingResult result,
            @ModelAttribute SerchResult serchResult,
            Model model)
            throws IOException {

        if (result.hasErrors()) {
            return "redirect:/";
        }

        // API実行結果
        SerchResult sr = cocktailService.serch(serchForm, serchResult.getCurrent_page() + 1);

        // カクテル情報リスト
        List<Cocktails> cocktailsList = sr.getCocktails();
        // レシピ情報リスト
        List<Recipes> recipesList = sr.getCocktails().get(sr.getCocktails().size() - 1).getRecipes();

        /*
         * 動作確認
         */
        System.out.println(sr.getCurrent_page());
        System.out.println(serchForm.getBase());

        model.addAttribute("serchResult", sr);
        model.addAttribute("serchForm", serchForm);
        model.addAttribute("cocktailsList", cocktailsList);
        model.addAttribute("recipesList", recipesList);

        return "cocktails/serch-result";
    }

    /**
     * 「前へ」ボタン押下時の検索処理。
     * 
     * @param serchForm   検索フォームオブジェクト
     * @param result      バリデーションの検証結果
     * @param serchResult 検索結果オブジェクト
     * @param model       モデル
     * @return 前ページの検索結果
     * @throws InterruptedException
     * @throws IOException
     */
    @RequestMapping(value = "/paging", params = "back", method = RequestMethod.POST)
    public String backPage(
            @ModelAttribute @Validated SerchForm serchForm,
            BindingResult result,
            @ModelAttribute SerchResult serchResult,
            Model model)
            throws InterruptedException, IOException {

        if (result.hasErrors()) {
            return "redirect:/";
        }

        // API実行結果
        SerchResult sr = cocktailService.serch(serchForm, serchResult.getCurrent_page() - 1);

        // カクテル情報リスト
        List<Cocktails> cocktailsList = sr.getCocktails();
        // レシピ情報リスト
        List<Recipes> recipesList = cocktailsList.get(sr.getCocktails().size() - 1).getRecipes();

        model.addAttribute("serchResult", sr);
        model.addAttribute("serchForm", serchForm);
        model.addAttribute("cocktailsList", cocktailsList);
        model.addAttribute("recipesList", recipesList);

        return "cocktails/serch-result";
    }
}
