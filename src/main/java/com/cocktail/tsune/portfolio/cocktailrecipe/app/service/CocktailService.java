package com.cocktail.tsune.portfolio.cocktailrecipe.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails.Cocktails;
import com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails.SerchForm;
import com.cocktail.tsune.portfolio.cocktailrecipe.domain.cocktails.SerchResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CocktailService {

    private String apiUrl = "https://cocktail-f.com/api/v1/cocktails";

    /**
     * 初回検索処理。
     * 
     * @param serchForm 検索フォームオブジェクト
     * @return 検索結果リスト
     * @throws IOException
     */
    public SerchResult serch(SerchForm serchForm)
            throws IOException {
        String url = createUrl(serchForm);

        return requestApi(url);
    }

    /**
     * 2回目以降の検索処理
     * 
     * @param serchForm 検索フォームオブジェクト
     * @param page      次のページ数
     * @return 検索結果リスト
     * @throws IOException
     */
    public SerchResult serch(SerchForm serchForm, int page)
            throws IOException {
        String url = createUrl(serchForm) + "&page=" + Integer.toString(page);

        return requestApi(url);
    }

    /**
     * APIを実行するメソッド。
     * 
     * @param url リクエストするURL
     * @return serchResult 検索結果リスト
     */
    private SerchResult requestApi(String url) {

        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> response = rest.getForEntity(url, String.class);
        String json = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        SerchResult serchResult = new SerchResult();

        try {
            serchResult = mapper.readValue(json, SerchResult.class);
            serchResult = serchImages(serchResult);
        } catch (Exception e) {
            System.err.println("カクテル情報の取得に失敗しました。" + e);
            e.printStackTrace();
        }

        return serchResult;
    }

    /**
     * APIにリクエストするためのURLを作成するメソッド。
     * 
     * @param cocktail データ定義クラス
     * @return URL
     */
    private String createUrl(SerchForm cocktail) {
        // べ―ス
        String base = Integer.toString(Integer.valueOf(cocktail.getBase()));
        // 度数の下限
        String alcohol_from = Integer.toString(Integer.valueOf(cocktail.getAlcohol_from()));
        // 度数の上限
        String alcohol_to = Integer.toString(Integer.valueOf(cocktail.getAlcohol_to()));
        // 表示件数の上限
        String limit = "10";

        StringBuilder sb = new StringBuilder();
        sb.append(apiUrl + "?");

        // カクテル名の入力チェック。
        if (!cocktail.getWord().isEmpty() || cocktail.getWord() == null) {
            sb.append("word=" + cocktail.getWord());
        }

        // ベースの入力チェック。
        if (!base.isEmpty() || base == null) {
            if (!cocktail.getWord().isEmpty() || cocktail.getWord() == null) {
                // カクテル名が入力されている場合、"&"を追加する。
                sb.append("&");
            }
            sb.append("base=" + base);
        }

        // 度数の下限の入力チェック。
        if (!alcohol_from.isEmpty() || alcohol_from == null) {
            if (!base.isEmpty() || base == null) {
                // ベースが選択されている場合、"&"を追加する。
                sb.append("&");
            }
            sb.append("alcohol_from=" + alcohol_from);
        }

        // 度数の上限の入力チェック。
        if (!alcohol_to.isEmpty() || alcohol_to == null) {
            if (!alcohol_from.isEmpty() || alcohol_from == null) {
                // 度数の下限が選択されている場合、"&"を追加する。
                sb.append("&");
            }
            sb.append("alcohol_to=" + alcohol_to);
        }

        //
        sb.append("&limit=" + limit);

        return sb.toString();
    }

    /**
     * カクテル画像を取得するメソッド。
     * 
     * @param SerchResult レシピの検索結果
     * @return imagesList 画像URLリスト
     */
    public SerchResult serchImages(SerchResult serchResult) throws IOException {
        String key = "***************************************";
        String cx = "*****************";
        String link = null;
        int displayNum = 1;
        List<Cocktails> cocktailsList = serchResult.getCocktails();
        List<String> nameList = new ArrayList<String>();
        URL url = null;
        HttpURLConnection conn = null;

        // カクテル名リストを取得。
        for (int i = 0; i < cocktailsList.size(); i++) {
            nameList.add(cocktailsList.get(i)
                    .getCocktail_name_english()
                    .replace(" ", "+"));
        }

        // Google Custom Search APIにリクエストを投げる。
        for (int i = 0; i < nameList.size(); i++) {

            String urlString = "https://www.googleapis.com/customsearch/v1?key=" + key
                    + "&cx=" + cx
                    + "&q=" + nameList.get(i) + "+cocktail"
                    + "&searchType=image"
                    + "&num=" + displayNum;

            // Google Custom Search APIにリクエスト。
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            // jsonからURLを検出。
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {

                if (output.contains("\"link\": \"")) {

                    link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(),
                            output.indexOf("\","));
                    cocktailsList.get(i).setImages(link);
                    break;
                }
            }
            serchResult.setCocktails(cocktailsList);
        }
        conn.disconnect();
        return serchResult;
    }
}
