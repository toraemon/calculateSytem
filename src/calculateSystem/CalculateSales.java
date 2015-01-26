package calculateSystem;

import java.io.*;
import java.util.*;

import static calculateSystem.Sales.*;

public class CalculateSales {
	public static void main(String... args){
		try{
			// インスタンス生成
			Branch def1 = new Branch();
			Commodity def2 = new Commodity();
			// 定義ファイル読み込み
			def1.readDefinitionFile(args[0], "branch");
			def2.readDefinitionFile(args[0], "commodity");

			// 売上ファイルを検索
			List<File> fileList = searchSalesFile(args[0]);
			// 売上ファイル読み込み
			List<Sales> readSalesFile = readSalesFile(fileList);
			// 売上ファイルを集計する処理
			def1.calculateSales(readSalesFile);	// 支店別
			def2.calculateSales(readSalesFile);	// 商品別
			// 集計ファイルに出力する処理
			def1.writeOutputFile(args[0], "branch");		// 支店別
			def2.writeOutputFile(args[0], "commodity");	// 商品別
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("エラーが発生しました。\n処理を中断します。");
			System.exit(1);
		}
		System.out.println("終了");
	}
}