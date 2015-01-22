package calculateSystem;

import java.io.*;
import java.util.*;
import static calculateSystem.Branch.writeOutputBranchFile;
import static calculateSystem.Commodity.writeOutputCommodityFile;
import static calculateSystem.Definition.calculateSales;
import static calculateSystem.Sales.searchSalesFile;
import static calculateSystem.Sales.readSalesFile;

public class CalculateSales {
	public static void main(String... args) throws IOException{
		Definition defBranch = new Branch();
		Definition defCommodity = new Commodity();
		try{
			// 支店定義ファイルの読み込み
			List<Definition> branch = defBranch.readDefinitionFile(args[0], "branch");
			// 商品定義ファイルの読み込み
			List<Definition> commodity = defCommodity.readDefinitionFile(args[0], "commodity");
			// 売上ファイルを検索
			List<File> fileList = searchSalesFile(args[0]);
			// 売上ファイルの読み込み
			List<Sales> readSales = readSalesFile(fileList);
			// 支店売上ファイルの集計
			//defBranch.calculateSales(branch, readSales);
			// 商品売上ファイルの集計
			//defCommodity.calculateSales(commodity, readSales);
			calculateSales(branch, commodity, readSales);
			// 支店売上ファイル書き出し
			writeOutputBranchFile(args[0], branch, "branch");
			// 商品売上ファイル書き出し
			writeOutputCommodityFile(args[0], commodity, "commodity");
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			System.out.println("処理が終了しました。");
		}
	}
}
