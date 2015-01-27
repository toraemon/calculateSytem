package com.assignment.calculateSales;

import java.io.File;
import java.util.List;

import static com.assignment.calculateSales.Sales.*;

public class CalculateSales {
	public static void main(String... args){
		try{
			// インスタンス生成
			Branch branch = new Branch();
			Commodity commodity = new Commodity();
			// 定義ファイル読み込み
			branch.readDefinitionFile(args[0]);
			commodity.readDefinitionFile(args[0]);

			// 売上ファイルを検索
			List<File> fileList = searchSalesFile(args[0]);
			// 売上ファイル読み込み
			List<Sales> readSalesFile = readSalesFile(fileList);
			// 売上ファイルを集計する処理
			branch.calculateSales(readSalesFile);	// 支店別
			commodity.calculateSales(readSalesFile);	// 商品別
			// 集計ファイルに出力する処理
			branch.writeOutputFile(args[0]);		// 支店別
			commodity.writeOutputFile(args[0]);	// 商品別
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("エラーが発生しました。\n処理を中断します。");
			System.exit(1);
		}
		System.out.println("終了");
	}
}